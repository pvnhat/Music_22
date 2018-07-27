package com.framgia.music_22.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String mSongId;
    private String mTitle;
    private String mGenre;
    private String mUserId;
    private String mStreamUrl;
    private int mDuaration;
    private String mUri;
    private Artist mArtist;

    protected Song(Parcel in) {
    }

    public Song(String songId, String title, String genre, String userId, String streamUrl,
            int duaration, String uri, Artist artist) {
        mSongId = songId;
        mTitle = title;
        mGenre = genre;
        mUserId = userId;
        mStreamUrl = streamUrl;
        mDuaration = duaration;
        mUri = uri;
        mArtist = artist;
    }

    public String getSongId() {
        return mSongId;
    }

    public void setSongId(String songId) {
        mSongId = songId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public int getDuaration() {
        return mDuaration;
    }

    public void setDuaration(int duaration) {
        mDuaration = duaration;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public Artist getArtist() {
        return mArtist;
    }

    public void setArtist(Artist artist) {
        mArtist = artist;
    }

    public static Creator<Song> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mSongId);
        parcel.writeString(mTitle);
        parcel.writeString(mGenre);
        parcel.writeString(mStreamUrl);
        parcel.writeString(mUri);
        parcel.writeString(mUserId);
        parcel.writeParcelable(mArtist, i);
        parcel.writeInt(mDuaration);
    }

    public class APISongProperties {
        public static final String SONG_ID = "ïd";
        public static final String TITLE = "title";
        public static final String GENRE = "genre";
        public static final String USER_ID = "user_id";
        public static final String STREAM_URL = "stream_url";
        public static final String DUARATION = "duration";
        public static final String SONG_URI = "uri";
    }
}
