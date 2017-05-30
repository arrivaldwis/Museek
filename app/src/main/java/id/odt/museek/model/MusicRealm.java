package id.odt.museek.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by arrival on 5/2/17.
 */

@RealmClass
public class MusicRealm extends RealmObject {
    public String id;
    public String artist;
    public String cover_img;
    public String gender;
    public String stream_url;
    public String time_length;
    public String title;

    public MusicRealm() {
        this.id = "";
        this.artist = "";
        this.cover_img = "";
        this.gender = "";
        this.stream_url = "";
        this.time_length = "";
        this.title = "";
    }

    public MusicRealm(String id, String artist, String cover_img, String gender,
                      String stream_url, String time_length, String title) {
        this.id = id;
        this.artist = artist;
        this.cover_img = cover_img;
        this.gender = gender;
        this.stream_url = stream_url;
        this.time_length = time_length;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getTime_length() {
        return time_length;
    }

    public void setTime_length(String time_length) {
        this.time_length = time_length;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
