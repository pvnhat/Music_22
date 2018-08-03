package com.framgia.music_22.screen.play_music_screen;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.ParseRemoteJsonData;
import com.framgia.vnnht.music_22.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity
        implements View.OnClickListener, PlayMusicContract.View, MusicServiceContract,
        SeekBar.OnSeekBarChangeListener {

    private static final String EXTRA_PLAY_SONG_LIST = "EXTRA_PLAY_SONG_LIST";
    private static final String EXTRA_PLAY_SONG_POSITION = "EXTRA_PLAY_SONG_POSITION";
    private static final int REQUEST_PERMISSION_CODE = 69;
    private static final String TIME_FORMAT = "mm:ss";

    private ImageButton mButtonBack, mButtonPrevious, mButtonPlay, mButtonNext, mButtonLoop,
            mButtonShuffle, mButtonDownload;
    private TextView mTextTitle, mTextArtist, mTextCurrentTime, mTextDuarationTime;
    private CircleImageView mImageAvatar;
    private SeekBar mSeekBarProgressSong;
    private List<Song> mSongList;
    private int mPosition;
    private Animation mAnimation;
    private MediaPlayer mMediaPlayer;
    private PlayMusicService mPlayMusicService;
    private ServiceConnection mServiceConnection;

    public static Intent getInstance(Context context, List<Song> songList, int position) {
        Intent intent = new Intent(context, PlayMusicActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_PLAY_SONG_LIST,
                (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(EXTRA_PLAY_SONG_POSITION, position);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        initView();
    }

    private void initView() {
        mTextTitle = findViewById(R.id.text_title);
        mTextArtist = findViewById(R.id.text_artist);
        mTextCurrentTime = findViewById(R.id.text_current_time);
        mTextDuarationTime = findViewById(R.id.text_duaration_time);
        mImageAvatar = findViewById(R.id.image_avatar);
        mSeekBarProgressSong = findViewById(R.id.seek_bar_playinh_process);
        mButtonBack = findViewById(R.id.button_back);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonPlay = findViewById(R.id.button_play);
        mButtonNext = findViewById(R.id.button_next);
        mButtonLoop = findViewById(R.id.button_loop);
        mButtonShuffle = findViewById(R.id.button_shuffle);
        mButtonDownload = findViewById(R.id.button_download);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.rotated_avatar_dics);

        mButtonBack.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonLoop.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
        mButtonDownload.setOnClickListener(this);
        mSeekBarProgressSong.setOnSeekBarChangeListener(this);
        onConnectToService();
        initData();
    }

    private void onConnectToService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
                mPlayMusicService = binder.getServiceInstance();
                mPlayMusicService.registerClient(PlayMusicActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.button_previous:
                mPlayMusicService.previousSong();
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
                break;
            case R.id.button_play:
                if (mPlayMusicService.isMusicPlaying()) {
                    mPlayMusicService.pauseSong();
                    mButtonPlay.setImageResource(R.drawable.ic_play_button);
                    mImageAvatar.setAnimation(null);
                } else {
                    mPlayMusicService.continueSong();
                    mButtonPlay.setImageResource(R.drawable.ic_pause_button);
                    mImageAvatar.setAnimation(mAnimation);
                }
                break;
            case R.id.button_next:
                mPlayMusicService.nextSong();
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
                break;
            case R.id.button_loop:
                break;
            case R.id.button_shuffle:
                break;
            case R.id.button_download:
                onRequestStoragePermisson();
                break;
        }
    }

    private void initData() {
        mSongList = getIntent().getParcelableArrayListExtra(EXTRA_PLAY_SONG_LIST);
        mPosition = getIntent().getIntExtra(EXTRA_PLAY_SONG_POSITION, -1);
        onPlayMusicControl(mSongList, mPosition);
    }

    private void onPlayMusicControl(List<Song> songs, int position) {
        mTextTitle.setText(songs.get(position).getTitle());
        mTextArtist.setText(songs.get(position).getArtist().getSingerName());
        mButtonPlay.setImageResource(R.drawable.ic_pause_button);
        mImageAvatar.setAnimation(mAnimation);
        Glide.with(this).load(songs.get(position).getArtist().getAvatarUrl()).into(mImageAvatar);
        onPlayMusicOnline(songs, position);
    }

    public void onPlayMusicOnline(List<Song> songList, int position) {
        Intent intent = PlayMusicService.getInstance(this, songList, position);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void updateMediaToClient(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        mTextCurrentTime.setText(dateFormat.format(mMediaPlayer.getCurrentPosition()));
        mTextDuarationTime.setText(dateFormat.format(mMediaPlayer.getDuration()));
        mSeekBarProgressSong.setMax(mMediaPlayer.getDuration());
        mSeekBarProgressSong.setProgress(mediaPlayer.getCurrentPosition());
    }

    @Override
    public void getUpdateSongDetail(Song song) {
        mTextTitle.setText(song.getTitle());
        mTextArtist.setText(song.getArtist().getSingerName());
        Glide.with(this).load(song.getArtist().getAvatarUrl()).into(mImageAvatar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMediaPlayer.seekTo(seekBar.getProgress());
    }

    private void onRequestStoragePermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.text_inform_to_client_permisson, Toast.LENGTH_SHORT)
                    .show();
            mPlayMusicService.downloadSong();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_PERMISSION_CODE);
        }
    }
}
