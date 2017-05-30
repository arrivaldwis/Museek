package id.odt.museek.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.odt.museek.R;
import id.odt.museek.model.CardRealm;
import io.realm.Realm;
import io.realm.RealmResults;

public class PlayerActivity extends AppCompatActivity {
    RealmResults<CardRealm> realmResults;
    MediaPlayer mediaPlayer;
    public Realm realm;
    private CardRealm musicPlayed;

    @BindView(R.id.next)
    public ImageView next;

    @BindView(R.id.back)
    public ImageView back;

    @BindView(R.id.btnBack)
    public ImageView btnBack;

    @BindView(R.id.play)
    public ImageView play;

    @BindView(R.id.pause)
    public ImageView pause;

    @BindView(R.id.imgCover)
    public ImageView imgCover;

    @BindView(R.id.tvCurrentDuration)
    public TextView tvCurrentDuration;

    @BindView(R.id.tvTotalDuration)
    public TextView tvTotalDuration;

    @BindView(R.id.tvTitle)
    public TextView tvTitle;

    @BindView(R.id.tvArtist)
    public TextView tvArtist;

    @BindView(R.id.flPlay)
    public FrameLayout flPlay;

    @BindView(R.id.progressBar1)
    public ProgressBar progressBar;
    private int currentPosition = 0;
    private Handler handler;
    private int musicPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        realm = Realm.getDefaultInstance();
        handler = new Handler();

        if (getIntent().getExtras() != null) {
            realmResults = realm.where(CardRealm.class).findAll();
            musicPlayed = realm.where(CardRealm.class)
                    .equalTo("artist", getIntent().getStringExtra("artist"))
                    .equalTo("title", getIntent().getStringExtra("title"))
                    .findFirst();

            musicPosition = realmResults.indexOf(musicPlayed);
            fetchMusicInfo(musicPlayed);
        }

        flPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.performClick();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    musicPosition--;
                    musicPlayed = realmResults.get(musicPosition);
                    fetchMusicInfo(musicPlayed);
                } catch (Exception ex) {
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    musicPosition++;
                    musicPlayed = realmResults.get(musicPosition);
                    fetchMusicInfo(musicPlayed);
                } catch (Exception ex) {
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.pause();
                    }
                });
                mediaPlayer.pause();
            }
        });
    }

    private void playMusic(CardRealm music) {
        try {
            handler = new Handler();
        } catch (Exception ex) {
        }

        mediaPlayer.reset();
        try {
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
            mediaPlayer.setDataSource(music.getStream_url());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    musicRun();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchMusicInfo(CardRealm music) {
        Picasso.with(this).load(music.getCover_img()).resize(400, 310).into(imgCover);
        tvTitle.setText(music.getTitle());
        tvArtist.setText(music.getArtist());
        playMusic(music);
    }

    private String secToTime(int seconds) {
        String min = "";
        String sec = "";

        long minutes = seconds / 60;
        if (minutes < 10) {
            min = "0" + minutes;
        } else {
            min = minutes+"";
        }

        seconds = seconds % 60;
        if (seconds < 10) {
            sec = "0" + seconds;
        } else {
            sec = seconds+"";
        }

        return min + ":" + sec;
    }

    int total = 0;

    private void musicRun() {
        total = mediaPlayer.getDuration()/1000;
        progressBar.setMax(total);
        tvTotalDuration.setText(secToTime(total));

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    if(mCurrentPosition < total) {
                        progressBar.setProgress(mCurrentPosition);
                        tvCurrentDuration.setText(secToTime(mCurrentPosition));
                    } else {
                        mediaPlayer.reset();
                        next.performClick();
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });

        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null && currentPosition < total) {
                    try {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        tvCurrentDuration.setText(currentPosition + "");
                    } catch (Exception e) {
                        return;
                    }
                    progressBar.setProgress(currentPosition);
                }
            }
        }, 1000);*/
    }
}
