package com.framgia.music_22.screen.offline_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.local.SongLocalDataSource;
import com.framgia.music_22.data.source.remote.SongRemoteDataSource;
import com.framgia.music_22.screen.play_music_screen.PlayMusicFragment;
import com.framgia.music_22.screen.song_by_genre_screen.OnItemClickListener;
import com.framgia.vnnht.music_22.R;
import java.util.List;

public class OfflineFragment extends Fragment implements OfflineContract.View, OnItemClickListener {

    private List<OfflineSong> mOfflineSongList;
    private RecyclerView mRecyclerSongList;
    private OfflineSongAdapter mAdapter;

    public static OfflineFragment newInstance() {
        return new OfflineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mRecyclerSongList = view.findViewById(R.id.recycler_offline_song);
        mRecyclerSongList.setHasFixedSize(true);
        mAdapter = new OfflineSongAdapter(getContext());
        mRecyclerSongList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        SongRemoteDataSource songRemoteDataSource = SongRemoteDataSource.getInstance();
        SongLocalDataSource songLocalDataSource =
                SongLocalDataSource.getInstance(getActivity().getApplicationContext());
        SongRepository songRepository =
                SongRepository.getsInstance(songRemoteDataSource, songLocalDataSource);
        OfflinePresenter presenter = new OfflinePresenter(this, songRepository);
        presenter.getSong();
    }

    @Override
    public void onGetSongOfflineSuccess(List<OfflineSong> offlineSongs) {
        if (offlineSongs.size() > 0) {
            mOfflineSongList = offlineSongs;
            mAdapter.updateSongList(mOfflineSongList);
        }
    }

    @Override
    public void onError(Exception ex) {
        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        getActivity().getSupportFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom)
                .replace(R.id.frame_layout_main,
                        PlayMusicFragment.newOfflineInstance(mOfflineSongList, position, true))
                .addToBackStack(null)
                .commit();
    }
}
