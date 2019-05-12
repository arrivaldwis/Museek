package id.odt.museek.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import id.odt.museek.R;
import id.odt.museek.model.UsersRealm;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog progress;
    private CallbackManager mCallbackManager;
    public FirebaseDatabase database;
    public DatabaseReference user_db;
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        user_db = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("", "onAuthStateChanged:signed_out");
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                progress.dismiss();
                Log.d("", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                progress.dismiss();
                Log.d("", "facebook:onError", error);
                // ...
            }
        });

        ImageView btnFB = (ImageView)findViewById(R.id.btnFB);
        ImageView btnGoogle = (ImageView)findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = ProgressDialog.show(LoginActivity.this, "Login", "Please wait..", true);
                loginButton.performClick();
            }
        });
    }

    private void signIn() {
        progress = ProgressDialog.show(this, "Login", "Please wait..", true);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            //loginButtonTw.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("", "signInWithCredential:onComplete:" + task.isSuccessful());

                        progress.dismiss();
                        FirebaseUser acct = mAuth.getCurrentUser();
                        insertToUsers(acct, "", "Creator", "");

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("fb_token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progress.dismiss();
                            Log.d("fb_login", "signInWithCredential:success");
                            FirebaseUser acct = mAuth.getCurrentUser();
                            insertToUsers(acct, "", "Creator", "");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fb_login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void insertToUsers(FirebaseUser acct, String location, String type, String works) {
        String key = user_db.child("users").push().getKey();
        UsersRealm story = new UsersRealm();
        story.id = key;
        story.email = acct.getEmail()==null?"":acct.getEmail();
        story.firstname = acct.getDisplayName();
        story.lastname = acct.getDisplayName();
        story.location = location;
        story.type = type;
        story.works = works;
        user_db.child("users").child(key).setValue(story);

        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        intent.putExtra("display", acct.getDisplayName());
        intent.putExtra("picture", acct.getPhotoUrl().toString());
        intent.putExtra("id", acct.getUid());
        intent.putExtra("email", acct.getEmail());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
