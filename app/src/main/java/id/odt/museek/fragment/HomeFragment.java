package id.odt.museek.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.odt.museek.R;
import id.odt.museek.activity.HomeActivity;
import id.odt.museek.adapter.CardsAdapter;
import id.odt.museek.model.Card;
import id.odt.museek.model.CardRealm;
import id.odt.swipecardlib.SwipeCardView;
import io.realm.Realm;

public class HomeFragment extends Fragment {

    private ArrayList<CardRealm> al;
    private CardsAdapter arrayAdapter;
    private int i;

    private SwipeCardView swipeCardView;

    @BindView(R.id.btnUnlike)
    public FloatingActionButton btnUnlike;

    @BindView(R.id.btnLike)
    public FloatingActionButton btnLike;

    @BindView(R.id.btnRetry)
    public Button btnRetry;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    public FirebaseDatabase database;
    public DatabaseReference user_db;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        swipeCardView = (SwipeCardView) view.findViewById(R.id.card_stack_view);
        database = FirebaseDatabase.getInstance();
        user_db = database.getReference();

        al = new ArrayList<>();
        arrayAdapter = new CardsAdapter(getActivity(), al);
        swipeCardView.setAdapter(arrayAdapter);
        getMusicData();

        swipeCardView.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override public void onCardExitLeft(Object dataObject) {

            }

            @Override public void onCardExitRight(Object dataObject) {
                insertToPlaylist((CardRealm)dataObject);
            }

            @Override public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override public void onScroll(float scrollProgressPercent) {

            }

            @Override public void onCardExitTop(Object dataObject) {
            }

            @Override public void onCardExitBottom(Object dataObject) {
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMusicData();
            }
        });
        return view;
    }

    private void insertToPlaylist(CardRealm dataObject) {
        Realm realm = Realm.getDefaultInstance();
        long isExist = realm.where(CardRealm.class)
                        .equalTo("title", dataObject.getTitle())
                        .equalTo("artist", dataObject.getArtist())
                        .count();

        if(isExist <= 0) {
            realm.beginTransaction();
            CardRealm user = realm.createObject(CardRealm.class);
            user.setArtist(dataObject.getArtist());
            user.setCover_img(dataObject.getCover_img());
            user.setGender(dataObject.getGender());
            user.setId_user(dataObject.getId_user());
            user.setStream_url(dataObject.getStream_url());
            user.setTime_length(dataObject.getTime_length());
            user.setTitle(dataObject.getTitle());
            realm.commitTransaction();
        }
    }

    private void getMusicData() {
        btnRetry.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        user_db.child("music").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CardRealm userData = postSnapshot.getValue(CardRealm.class);
                    CardRealm card = new CardRealm();
                    card.artist = userData.getArtist();
                    card.cover_img = userData.getCover_img();
                    card.gender = userData.getGender();
                    card.id_user = userData.getId_user();
                    card.stream_url = userData.getStream_url();
                    card.time_length = userData.getTime_length();
                    card.title = userData.getTitle();

                    Log.d("artist", card.artist);
                    Log.d("cover_img", card.cover_img);
                    Log.d("gender", card.gender);
                    Log.d("id_user", card.id_user);
                    Log.d("stream_url", card.stream_url);
                    Log.d("time_length", card.time_length);
                    Log.d("title", card.title);

                    al.add(card);
                    arrayAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                btnRetry.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "Internet Connection unstable", Toast.LENGTH_SHORT).show();
                Log.w("", "Failed to read value.", error.toException());
            }
        });
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnLike)
    public void like(View v) {
        swipeCardView.throwRight();
    }

    @OnClick(R.id.btnUnlike)
    public void unlike(View v) {
        swipeCardView.throwLeft();
    }
}
