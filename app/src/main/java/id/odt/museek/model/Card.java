package id.odt.museek.model;

import io.realm.RealmObject;

/**
 * Created by Arrival on 4/25/16.
 */
public class Card {
  public String artist;
  public String cover_img;
  public String gender;
  public String id_user;
  public String stream_url;
  public String time_length;
  public String title;

  public Card() {
    this.artist = "";
    this.cover_img = "";
    this.gender = "";
    this.id_user = "";
    this.stream_url = "";
    this.time_length = "";
    this.title = "";
  }

  public Card(String artist, String cover_img, String gender, String id_user, String stream_url, String time_length, String title) {
    this.artist = artist;
    this.cover_img = cover_img;
    this.gender = gender;
    this.id_user = id_user;
    this.stream_url = stream_url;
    this.time_length = time_length;
    this.title = title;
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

  public String getId_user() {
    return id_user;
  }

  public void setId_user(String id_user) {
    this.id_user = id_user;
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
