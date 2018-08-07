package com.framgia.music_22.data;

import com.framgia.music_22.data.model.MoreSong;

public interface GetSongDataSource {
    interface RemoteDataSource {
        void getSongByGenre(String genre, RequestCallbackData<MoreSong> callbackData);
    }

    interface LocalDataSource {
        void getLocalMusic(RequestCallbackData<MoreSong> callbackData);
    }
}
