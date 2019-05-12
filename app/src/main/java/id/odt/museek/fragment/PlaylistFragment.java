package id.odt.museek.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.odt.museek.R;
import id.odt.museek.adapter.PlaylistAdapter;
import id.odt.museek.model.CardRealm;
import io.realm.Realm;

public class PlaylistFragment extends Fragment {

    private PlaylistAdapter playlistAdapter;
    private int i;

    @BindView(R.id.rvPlaylist)
    public RecyclerView rvPlaylist;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeContainer;

    @BindView(R.id.llEmpty)
    public LinearLayout llEmpty;

    public FirebaseDatabase database;
    public DatabaseReference user_db;
    public Realm realm;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
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
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, view);
        database = FirebaseDatabase.getInstance();
        user_db = database.getReference();
        realm = Realm.getDefaultInstance();

        rvPlaylist.setHasFixedSize(true);
        try {
            int count = (int) realm.where(CardRealm.class).count();
            if(count<=0) {
                progressBar.setVisibility(View.GONE);
            } else {
                llEmpty.setVisibility(View.GONE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rvPlaylist.setLayoutManager(layoutManager);
                loadData();
            }
        } catch (Exception ex) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipeContainer.setRefreshing(false);
            }
        });

        return view;
    }

    public void loadData() {
        playlistAdapter = new PlaylistAdapter(getActivity(), realm.where(CardRealm.class).findAll());
        rvPlaylist.setAdapter(playlistAdapter);
        progressBar.setVisibility(View.GONE);
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }
}
