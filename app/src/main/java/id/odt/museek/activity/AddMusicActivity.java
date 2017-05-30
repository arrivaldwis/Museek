package id.odt.museek.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.odt.museek.R;
import id.odt.museek.model.MusicRealm;

public class AddMusicActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    @BindView(R.id.btn_ep)
    public LinearLayout btn_ep;
    @BindView(R.id.btn_next)
    public LinearLayout btn_next;
    @BindView(R.id.tvartist)
    public EditText tvartist;
    @BindView(R.id.tvtitle)
    public EditText tvtitle;
    @BindView(R.id.btnUploadCover)
    public RelativeLayout btnUploadCover;
    @BindView(R.id.btnUploadMusic)
    public RelativeLayout btnUploadMusic;
    public FirebaseStorage storage;
    public StorageReference storageRef;
    public FirebaseDatabase database;
    public DatabaseReference user_db;
    private boolean coverUploaded = false;
    private boolean musicUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);
        ButterKnife.bind(this);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        database = FirebaseDatabase.getInstance();
        user_db = database.getReference();

        btn_ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = tvtitle.getText().toString();
                String artist = tvartist.getText().toString();
                if(title.isEmpty()) {
                    tvtitle.setError("Please fill this field");
                    return;
                }
                if(artist.isEmpty()) {
                    tvartist.setError("Please fill this field");
                    return;
                }
                if(!coverUploaded) {
                    Toast.makeText(AddMusicActivity.this, "Please choose the cover first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!musicUploaded) {
                    Toast.makeText(AddMusicActivity.this, "Please choose the music first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                insertToMusic(artist, coverUrl, "Male", musicUrl, "", title);
            }
        });

        btnUploadCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Cover"), PICK_IMAGE_REQUEST);
            }
        });

        btnUploadMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 10);
            }
        });
    }

    private void insertToMusic(String artist, String cover_img, String gender,
                               String stream_url, String time_length, String title) {
        String key = user_db.child("musicRealm").push().getKey();
        MusicRealm musicRealm = new MusicRealm();
        musicRealm.id = key;
        musicRealm.artist = artist;
        musicRealm.cover_img = cover_img;
        musicRealm.gender = gender;
        musicRealm.stream_url = stream_url;
        musicRealm.time_length = time_length;
        musicRealm.title = title;
        user_db.child("musicRealm").child(key).setValue(musicRealm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imgData = baos.toByteArray();
                uploadCover(imgData, String.valueOf(System.currentTimeMillis()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(resultCode == RESULT_OK && requestCode == 10){
            Uri uriSound = data.getData();
            String path = uriSound.getPath();
            byte[] fileByteArray;
            File file = new File(path);
            fileByteArray = new byte[(int)file.length()];
            uploadMusic(fileByteArray, String.valueOf(System.currentTimeMillis()));
        }
    }

    String coverUrl = "";
    String musicUrl = "";
    private void uploadCover(byte[] data, String filename) {
        StorageReference imagesRef = storageRef.child("cover/"+filename+".jpg");
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                coverUrl = downloadUrl.toString();
                coverUploaded = true;
            }
        });
    }

    private void uploadMusic(byte[] data, String filename) {
        StorageReference imagesRef = storageRef.child("music/"+filename+".jpg");
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                musicUrl = downloadUrl.toString();
                musicUploaded = true;
            }
        });
    }
}
