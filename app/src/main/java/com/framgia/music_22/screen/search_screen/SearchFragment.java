package com.framgia.music_22.screen.search_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.local.SongLocalDataSource;
import com.framgia.music_22.data.source.remote.SongRemoteDataSource;
import com.framgia.music_22.screen.play_music_screen.PlayMusicActivity;
import com.framgia.music_22.screen.song_by_genre_screen.OnItemClickListener;
import com.framgia.music_22.screen.song_by_genre_screen.SongByGenreAdapter;
import com.framgia.music_22.utils.ConnectionChecking;
import com.framgia.music_22.utils.Constant;
import com.framgia.vnnht.music_22.R;
import java.util.List;

public class SearchFragment extends Fragment
        implements SearchContract.View, View.OnClickListener, OnItemClickListener {

    private static final String LINK_TO_SERACH = "&q=";

    private EditText mEditSearchBox;
    private RecyclerView mRecyclerSearchedList;
    private SearchContract.Presenter mPresenter;
    private SongByGenreAdapter mAdapter;
    private List<Song> mSongList;
    private ConnectionChecking mConnectionChecking;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mEditSearchBox = view.findViewById(R.id.edit_search_box);
        ImageButton buttonSearch = view.findViewById(R.id.button_search);
        mRecyclerSearchedList = view.findViewById(R.id.recycler_searched_song);
        mConnectionChecking = new ConnectionChecking(getContext().getApplicationContext());
        mRecyclerSearchedList.setHasFixedSize(true);
        mAdapter = new SongByGenreAdapter(getContext());
        mAdapter.setOnItemClickListener(this);
        buttonSearch.setOnClickListener(this);

        initData();
    }

    private void initData() {
        SongRepository songRepository =
                SongRepository.getsInstance(SongRemoteDataSource.getInstance(),
                        SongLocalDataSource.getInstance(getContext().getApplicationContext()));
        mPresenter = new SearchPrecenter(this, songRepository);
    }

    @Override
    public void onGetSongByTitle(MoreSong moreSong) {
        if (moreSong != null && moreSong.getSongsList() != null) {
            mSongList = moreSong.getSongsList();
            mAdapter.updateSongList(mSongList);
            mRecyclerSearchedList.setAdapter(mAdapter);
        }
    }

    @Override
    public void onError(Exception ex) {
        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        String keyName = mEditSearchBox.getText().toString();
        if (keyName.isEmpty()) {
            mEditSearchBox.setError(getResources().getString(R.string.text_search_inform));
        } else if (!mConnectionChecking.isNetworkConnection()) {
            Toast.makeText(getContext(), R.string.text_connection_information, Toast.LENGTH_SHORT)
                    .show();
        } else {
            mPresenter.getSongByTitle(Constant.GENRES_URL + LINK_TO_SERACH + keyName);
        }
    }

    @Override
    public void onItemClick(int position) {
        startActivity(
                new Intent(PlayMusicActivity.getOnlineInstance(getContext(), mSongList, position)));
    }
}
