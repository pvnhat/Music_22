package com.framgia.music_22.screen.song_by_genre_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.local.SongLocalDataSource;
import com.framgia.music_22.data.source.remote.SongRemoteDataSource;
import com.framgia.music_22.screen.play_music_screen.PlayMusicFragment;
import com.framgia.vnnht.music_22.R;
import java.util.List;

public class SongByGenreFragment extends Fragment
        implements OnItemClickListener, SongByGenreContract.View, View.OnClickListener {

    private static final String EXTRA_LINK_SONG = "EXTRA_LINK_SONG";
    private static final String EXTRA_MORE_SONG = "EXTRA_MORE_SONG";

    private SongByGenreAdapter mAdapter;
    private List<Song> mSongList;
    private TextView mTextGenreName;

    public static SongByGenreFragment newInstance(String genreLink, String genre) {
        SongByGenreFragment fragment = new SongByGenreFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_LINK_SONG, genreLink);
        args.putString(EXTRA_MORE_SONG, genre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_by_genre, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerSongList = view.findViewById(R.id.recycler_songs_by_genre);
        mTextGenreName = view.findViewById(R.id.text_genre_name);
        ImageButton buttonBack = view.findViewById(R.id.button_back);
        recyclerSongList.setHasFixedSize(true);
        mAdapter = new SongByGenreAdapter(getContext());
        recyclerSongList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    private void initData() {
        Bundle bundle = getArguments();
        assert bundle != null;
        String genreLink = bundle.getString(EXTRA_LINK_SONG);
        String genre = bundle.getString(EXTRA_MORE_SONG);
        mTextGenreName.setText(genre);
        SongRepository songRepository =
                SongRepository.getsInstance(SongRemoteDataSource.getInstance(),
                        SongLocalDataSource.getInstance(getContext().getApplicationContext()));
        SongByGenreContract.Presenter presenter = new SongByGenrePresenter(this, songRepository);
        presenter.getSongByGenres(genreLink);
    }

    @Override
    public void onItemClick(int position) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom)
                .replace(R.id.frame_layout_main,
                        PlayMusicFragment.newOnlineInstance(mSongList, position))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onGetSongByGenreSuccess(MoreSong moreSong) {
        mSongList = moreSong.getSongsList();
        if (moreSong.getSongsList() != null) {
            mAdapter.updateSongList(moreSong.getSongsList());
        }
    }

    @Override
    public void onError(Exception ex) {
        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }
}
