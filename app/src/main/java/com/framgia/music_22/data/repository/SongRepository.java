package com.framgia.music_22.data.repository;

import com.framgia.music_22.data.GetSongDataSource;
import com.framgia.music_22.data.RequestCallbackData;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.source.ParseRemoteJsonData;

public class SongRepository implements GetSongDataSource.RemoteDataSource {
    private static SongRepository sInstance;
    private ParseRemoteJsonData mParseRemoteJsonData;

    private SongRepository(ParseRemoteJsonData parseRemoteJsonData) {
        mParseRemoteJsonData = parseRemoteJsonData;
    }

    public static synchronized SongRepository getsInstance(
            ParseRemoteJsonData parseRemoteJsonData) {
        if (sInstance == null) {
            sInstance = new SongRepository(parseRemoteJsonData);
        }
        return sInstance;
    }

    @Override
    public void getSongByGenre(String genre, RequestCallbackData<MoreSong> callbackData) {
        mParseRemoteJsonData.getSongByGenre(genre, callbackData);
    }
}
