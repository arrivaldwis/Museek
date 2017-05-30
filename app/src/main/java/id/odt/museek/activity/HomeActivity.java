package id.odt.museek.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import id.odt.museek.R;
import id.odt.museek.fragment.HomeFragment;
import id.odt.museek.fragment.PlaylistFragment;
import id.odt.museek.fragment.ProfileFragment;
import io.realm.Realm;

public class HomeActivity extends AppCompatActivity implements SmartTabLayout.TabProvider {

  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthListener;
  public FirebaseUser user;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Realm.init(this);

    mAuth = FirebaseAuth.getInstance();
    mAuthListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
          // User is signed in
        } else {
          // User is signed out
          Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
          startActivity(intent);
          Log.d("", "onAuthStateChanged:signed_out");
        }
      }
    };

    FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
            getSupportFragmentManager(), FragmentPagerItems.with(this)
            .add("Profile", ProfileFragment.class)
            .add("Home", HomeFragment.class)
            .add("Playlist", PlaylistFragment.class)
            .create());

    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(adapter);

    SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
    viewPagerTab.setCustomTabView(this);
    viewPagerTab.setViewPager(viewPager);
    viewPager.setCurrentItem(1, true);
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

  @Override
  public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
    LayoutInflater inflater = LayoutInflater.from(container.getContext());
    Resources res = container.getContext().getResources();
    ImageView icon = (ImageView) inflater.inflate(R.layout.custom_tab_icon, container,
            false);
    switch (position) {
      case 0:
        icon.setImageDrawable(res.getDrawable(R.drawable.ic_profile));
        break;
      case 1:
        icon.setImageDrawable(res.getDrawable(R.drawable.ic_museek));
        break;
      case 2:
        icon.setImageDrawable(res.getDrawable(R.drawable.ic_playlist));
        break;
      default:
        throw new IllegalStateException("Invalid position: " + position);
    }
    return icon;
  }
}
