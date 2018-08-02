package com.framgia.music_22.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class MoreSong implements Parcelable {
    private ArrayList<Song> mSongsList;
    private String mNextHref;

    public MoreSong() {
    }

    public MoreSong(ArrayList<Song> songList, String nextHref) {
        mSongsList = songList;
        mNextHref = nextHref;
    }

    public MoreSong(Parcel in) {
        mSongsList = in.createTypedArrayList(Song.CREATOR);
        mNextHref = in.readString();
    }

    public ArrayList<Song> getSongsList() {
        return mSongsList;
    }

    public void setSongsList(ArrayList<Song> songsList) {
        mSongsList = songsList;
    }

    public String getNextHref() {
        return mNextHref;
    }

    public void setNextHref(String nextHref) {
        mNextHref = nextHref;
    }

    public static final Creator<MoreSong> CREATOR = new Creator<MoreSong>() {
        @Override
        public MoreSong createFromParcel(Parcel in) {
            return new MoreSong(in);
        }

        @Override
        public MoreSong[] newArray(int size) {
            return new MoreSong[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mSongsList);
        parcel.writeString(mNextHref);
    }
}
