package com.framgia.music_22.screen.song_by_genre_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.local.SongLocalDataSource;
import com.framgia.music_22.data.source.remote.SongRemoteDataSource;
import com.framgia.music_22.screen.play_music_screen.PlayMusicActivity;
import com.framgia.vnnht.music_22.R;
import java.util.List;

public class SongByGenreActivity extends AppCompatActivity
        implements OnItemClickListener, SongByGenreContract.View {

    private static final String EXTRA_LINK_SONG = "EXTRA_LINK_SONG";
    private static final String EXTRA_MORE_SONG = "EXTRA_MORE_SONG";

    private SongByGenreAdapter mAdapter;
    private String mGenreLink;
    private String mGenre;
    private List<Song> mSongList;

    public static Intent getInstance(Context context, String genreLink, String genre) {
        Intent intent = new Intent(context, SongByGenreActivity.class);
        intent.putExtra(EXTRA_LINK_SONG, genreLink);
        intent.putExtra(EXTRA_MORE_SONG, genre);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_by_genre);
        initView();
    }

    private void initView() {
        RecyclerView recyclerSongList = findViewById(R.id.recycler_songs_by_genre);
        recyclerSongList.setHasFixedSize(true);
        mAdapter = new SongByGenreAdapter(this);
        mAdapter.setOnItemClickListener(this);
        recyclerSongList.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        mGenreLink = getIntent().getStringExtra(EXTRA_LINK_SONG);
        mGenre = getIntent().getStringExtra(EXTRA_MORE_SONG);
        onSetActionBar(mGenre);
        SongRepository songRepository =
                SongRepository.getsInstance(SongRemoteDataSource.getInstance(),
                        SongLocalDataSource.getInstance(getApplicationContext()));
        SongByGenreContract.Presenter presenter = new SongByGenrePresenter(this, songRepository);
        presenter.getSongByGenres(mGenreLink);
    }

    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(PlayMusicActivity.getOnlineInstance(this, mSongList, position)));
    }

    @Override
    public void onGetSongByGenreSuccess(MoreSong moreSong) {
        mSongList = moreSong.getSongsList();
        if (moreSong != null && moreSong.getSongsList() != null) {
            mAdapter.updateSongList(moreSong.getSongsList());
        }
    }

    @Override
    public void onError(Exception ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void onSetActionBar(String genre) {
        getSupportActionBar().setTitle(genre);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
