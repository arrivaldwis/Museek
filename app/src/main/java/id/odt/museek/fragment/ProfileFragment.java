package id.odt.museek.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.odt.museek.R;
import id.odt.museek.activity.AddMusicActivity;
import id.odt.museek.activity.LoginActivity;
import id.odt.museek.activity.SettingActivity;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.btnAdd)
    public ImageView btnAdd;

    @BindView(R.id.btnSetting)
    public ImageView btnSetting;

    @BindView(R.id.profile_image)
    public CircleImageView profile_image;

    @BindView(R.id.tvUsername)
    public TextView tvUsername;

    @BindView(R.id.btnLogout)
    public Button btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Picasso.with(getActivity()).load(user.getPhotoUrl()).noFade().into(profile_image);
                    tvUsername.setText(user.getDisplayName());
                } else {
                    // User is signed out
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    Log.d("", "onAuthStateChanged:signed_out");
                }
            }
        };

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SettingActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Firebase sign out
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddMusicActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
