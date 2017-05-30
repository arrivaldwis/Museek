package id.odt.museek.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import id.odt.museek.model.Card;
import id.odt.museek.R;
import id.odt.museek.model.CardRealm;

/**
 * Created by Arrival on 4/25/16.
 */
public class CardsAdapter extends ArrayAdapter<CardRealm> {
  private final ArrayList<CardRealm> cards;
  private final LayoutInflater layoutInflater;
  private Context mContext;
  private boolean play = false;
  private boolean pause = false;
  MediaPlayer mediaPlayer;

  public CardsAdapter(Context context, ArrayList<CardRealm> cards) {
    super(context, -1);
    this.mContext = context;
    this.cards = cards;
    this.layoutInflater = LayoutInflater.from(context);
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final CardRealm card = cards.get(position);
    View view = layoutInflater.inflate(R.layout.item, parent, false);
    ImageView cardImage = (ImageView) view.findViewById(R.id.card_image);
    final ImageView btnAction = (ImageView) view.findViewById(R.id.btnAction);
    TextView artist = (TextView) view.findViewById(R.id.helloText);
    TextView title = (TextView) view.findViewById(R.id.titleText);
    btnAction.setImageResource(R.drawable.ic_play);

    mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    Picasso.with(mContext).load(card.cover_img).resize(360,360).into(cardImage);
    artist.setText(card.getArtist());
    title.setText(card.getTitle());

    btnAction.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if(!play && !pause) {
          btnAction.setImageResource(R.drawable.ic_pause);
          try {
            mediaPlayer.setDataSource(card.stream_url);
            mediaPlayer.prepareAsync();
          } catch (IOException e) {
            e.printStackTrace();
          }

          mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
              mp.start();
            }
          });
          play = true;
        } else {
          if(play && !pause) {
            btnAction.setImageResource(R.drawable.ic_play);
            pause = true;
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
              @Override
              public void onPrepared(MediaPlayer mp) {
                mp.pause();
              }
            });
            mediaPlayer.pause();
          } else {
            btnAction.setImageResource(R.drawable.ic_pause);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
              @Override
              public void onPrepared(MediaPlayer mp) {
                mp.start();
              }
            });
            mediaPlayer.start();
            pause = false;
          }
        }
      }
    });
    return view;
  }

  @Override public CardRealm getItem(int position) {
    return cards.get(position);
  }

  @Override public int getCount() {
    return cards.size();
  }
}
