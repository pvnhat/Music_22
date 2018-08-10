package com.framgia.music_22.screen.play_music_screen;

import android.Manifest;
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
import com.bumptech.glide.Glide;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.vnnht.music_22.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity
        implements View.OnClickListener, PlayMusicContract.View, MusicServiceContract,
        SeekBar.OnSeekBarChangeListener {

    private static final String EXTRA_PLAY_SONG_ONLINE_LIST = "EXTRA_PLAY_SONG_ONLINE_LIST";
    private static final String EXTRA_PLAY_SONG_OFFLINE_LIST = "EXTRA_PLAY_SONG_OFFLINE_LIST";
    private static final String EXTRA_ONLINE_SONG_POSITION = "EXTRA_ONLINE_SONG_POSITION";
    private static final String EXTRA_OFFLINE_SONG_POSITION = "EXTRA_OFFLINE_SONG_POSITION";
    private static final String EXTRA_IS_OFFLINE = "EXTRA_PLAY_SONG_POSITION";
    private static final int REQUEST_PERMISSION_CODE = 69;
    private static final String TIME_FORMAT = "mm:ss";

    private ImageButton mButtonBack, mButtonPrevious, mButtonPlay, mButtonNext, mButtonLoop,
            mButtonShuffle, mButtonDownload;
    private TextView mTextTitle, mTextArtist, mTextCurrentTime, mTextDuarationTime;
    private CircleImageView mImageAvatar;
    private SeekBar mSeekBarProgressSong;
    private Animation mAnimation;
    private MediaPlayer mMediaPlayer;
    private boolean mIsOffline;
    private PlayMusicService mPlayMusicService;
    private ServiceConnection mServiceConnection;

    public static Intent getOnlineInstance(Context context, List<Song> songList, int position) {
        Intent intent = new Intent(context, PlayMusicActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_PLAY_SONG_ONLINE_LIST,
                (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(EXTRA_ONLINE_SONG_POSITION, position);
        return intent;
    }

    public static Intent getOfflineInstance(Context context, List<OfflineSong> songList,
            int position, boolean check) {
        Intent intent = new Intent(context, PlayMusicActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_PLAY_SONG_OFFLINE_LIST,
                (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(EXTRA_OFFLINE_SONG_POSITION, position);
        intent.putExtra(EXTRA_IS_OFFLINE, check);
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
        mIsOffline = getIntent().getBooleanExtra(EXTRA_IS_OFFLINE, false);
        if (getIntent().getParcelableArrayListExtra(EXTRA_PLAY_SONG_ONLINE_LIST) != null) {
            int position = getIntent().getIntExtra(EXTRA_ONLINE_SONG_POSITION, -1);
            List<Song> songOnlineList =
                    getIntent().getParcelableArrayListExtra(EXTRA_PLAY_SONG_ONLINE_LIST);
            onPlayMusicOnlineControl(songOnlineList, position);
        } else {
            int position = getIntent().getIntExtra(EXTRA_OFFLINE_SONG_POSITION, -1);
            List<OfflineSong> songOfflineList =
                    getIntent().getParcelableArrayListExtra(EXTRA_PLAY_SONG_OFFLINE_LIST);
            onPlayMusicOfflineControl(songOfflineList, position, mIsOffline);
        }
    }

    private void onPlayMusicOnlineControl(List<Song> songs, int position) {
        mTextTitle.setText(songs.get(position).getTitle());
        mTextArtist.setText(songs.get(position).getArtist().getSingerName());
        mButtonPlay.setImageResource(R.drawable.ic_pause_button);
        mImageAvatar.setAnimation(mAnimation);
        Glide.with(this).load(songs.get(position).getArtist().getAvatarUrl()).into(mImageAvatar);
        onPlayMusicOnline(songs, position);
    }

    private void onPlayMusicOfflineControl(List<OfflineSong> songs, int position,
            boolean isOffline) {
        mTextTitle.setText(songs.get(position).getTitle());
        mTextArtist.setText(songs.get(position).getArtistName());
        mButtonPlay.setImageResource(R.drawable.ic_pause_button);
        mImageAvatar.setAnimation(mAnimation);
        mButtonDownload.setVisibility(View.INVISIBLE);
        Glide.with(this).load(R.drawable.default_avatart_song).into(mImageAvatar);
        onPlayMusicOffline(songs, position, isOffline);
    }

    public void onPlayMusicOnline(List<Song> songList, int position) {
        Intent intent = PlayMusicService.getOnlineInstance(this, songList, position);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onPlayMusicOffline(List<OfflineSong> songList, int position, boolean isOffline) {
        Intent intent = PlayMusicService.getOfflineInstance(this, songList, position, isOffline);
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
    public void onUpdateOnlineSongDetail(Song song) {
        mTextTitle.setText(song.getTitle());
        mTextArtist.setText(song.getArtist().getSingerName());
        Glide.with(this).load(song.getArtist().getAvatarUrl()).into(mImageAvatar);
    }

    @Override
    public void onUpdateOfflineSongDetail(OfflineSong song) {
        mTextTitle.setText(song.getTitle());
        mTextArtist.setText(song.getArtistName());
        Glide.with(this).load(R.drawable.default_avatart_song).into(mImageAvatar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    private void onRequestStoragePermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mPlayMusicService.downloadSong();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_PERMISSION_CODE);
        }
    }
}
