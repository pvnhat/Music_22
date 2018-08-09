package com.framgia.music_22.screen.offline_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.local.SongLocalDataSource;
import com.framgia.music_22.data.source.remote.SongRemoteDataSource;
import com.framgia.vnnht.music_22.R;
import java.util.List;

public class OfflineFragment extends Fragment implements OfflineContract.View {

    private List<OfflineSong> mOfflineSongList;

    public static OfflineFragment newInstance() {
        return new OfflineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline, container, false);
        initData();
        return view;
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
        }
    }

    @Override
    public void onError(Exception ex) {
        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
