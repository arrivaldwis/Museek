package id.odt.museek.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import id.odt.museek.R;
import id.odt.museek.activity.PlayerActivity;
import id.odt.museek.model.Card;
import id.odt.museek.model.CardRealm;
import id.odt.museek.ui.RealmRecyclerViewAdapter;
import id.odt.museek.viewholder.VHPlaylist;
import io.realm.RealmResults;

/**
 * Created by arrival on 4/29/17.
 */

public class PlaylistAdapter extends RealmRecyclerViewAdapter<CardRealm, VHPlaylist> {
    RealmResults<CardRealm> realmResults;
    Context context;

    public PlaylistAdapter(Context context, RealmResults<CardRealm> realmResults) {
        super(context, realmResults);
        this.realmResults = realmResults;
        this.context = context;
    }

    @Override
    public VHPlaylist onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        VHPlaylist holder = new VHPlaylist(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VHPlaylist holder, final int position) {
        holder.title.setText(realmResults.get(position).getTitle());
        holder.desc.setText(realmResults.get(position).getArtist());
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PlayerActivity.class);
                i.putExtra("artist", realmResults.get(position).getArtist());
                i.putExtra("title", realmResults.get(position).getTitle());
                Log.d("dataStream", realmResults.get(position).getArtist());
                Log.d("dataStream2", realmResults.get(position).getTitle());
                context.startActivity(i);
            }
        });
        Picasso.with(context).load(realmResults.get(position).getCover_img()).into(holder.imgCover);
        animatedItem(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void animatedItem(RecyclerView.ViewHolder viewHolder) {

        final Animation anima = AnimationUtils.loadAnimation(context, R.anim.activity_open_translate_from_bottom);
        viewHolder.itemView.setAnimation(anima);
        //viewHolder.itemView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3.f)).setDuration(700).start();
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }
}
