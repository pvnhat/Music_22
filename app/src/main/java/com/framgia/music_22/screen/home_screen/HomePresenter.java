package com.framgia.music_22.screen.home_screen;

import com.framgia.music_22.data.RequestCallbackData;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.repository.SongRepository;

public class HomePresenter implements HomePageContract.Presenter {

    private HomePageContract.View mView;
    private SongRepository mSongRepository;

    public HomePresenter(HomePageContract.View view, SongRepository songRepository) {
        mView = view;
        mSongRepository = songRepository;
    }

    @Override
    public void getSongByGenres(String genre) {
        mSongRepository.getSongByGenre(genre, new RequestCallbackData<MoreSong>() {
            @Override
            public void onGetDataSuccess(MoreSong moreSong) {
                mView.onGetSongByGenreSuccess(moreSong);
            }
            @Override
            public void onGetDataError(Exception e) {
                mView.onError(e);
            }
        });
    }
}
