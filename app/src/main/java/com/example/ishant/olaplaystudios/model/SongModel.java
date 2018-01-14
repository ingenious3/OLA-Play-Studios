package com.example.ishant.olaplaystudios.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongModel {

    @SerializedName("song")
    @Expose
    private String song = "";
    @SerializedName("url")
    @Expose
    private String url = "";
    @SerializedName("artists")
    @Expose
    private String artists = "";
    @SerializedName("cover_image")
    @Expose
    private String coverImage = "";

    public SongModel() {
        song = "";
        url = "";
        artists = "";
        coverImage = "";
    }

    public SongModel(String song, String url, String artists, String coverImage) {
        this.song = song;
        this.url = url;
        this.artists = artists;
        this.coverImage = coverImage;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

}
