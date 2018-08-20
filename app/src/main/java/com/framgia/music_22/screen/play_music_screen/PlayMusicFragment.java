package com.framgia.music_22.screen.play_music_screen;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.framgia.music_22.data.model.OfflineSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.vnnht.music_22.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicFragment extends Fragment
        implements View.OnClickListener, PlayMusicContract.View, MusicServiceContract,
        SeekBar.OnSeekBarChangeListener {

    private static final String EXTRA_PLAY_SONG_ONLINE_LIST = "EXTRA_PLAY_SONG_ONLINE_LIST";
    private static final String EXTRA_PLAY_SONG_OFFLINE_LIST = "EXTRA_PLAY_SONG_OFFLINE_LIST";
    private static final String EXTRA_ONLINE_SONG_POSITION = "EXTRA_ONLINE_SONG_POSITION";
    private static final String EXTRA_OFFLINE_SONG_POSITION = "EXTRA_OFFLINE_SONG_POSITION";
    private static final String EXTRA_IS_OFFLINE = "EXTRA_PLAY_SONG_POSITION";
    private static final int REQUEST_PERMISSION_CODE = 69;
    private static final String TIME_FORMAT = "mm:ss";
    private static final int CHECK_LOOP_ONE = 4;

    private ImageButton mButtonBack, mButtonPrevious, mButtonPlay, mButtonNext, mButtonLoop,
            mButtonShuffle, mButtonDownload, mButtonPreviousBottom, mButtonPlayBottom,
            mButtonNextBottom;
    private TextView mTextTitle, mTextArtist, mTextTitleBottom, mTextArtistBottom, mTextCurrentTime,
            mTextDuarationTime;
    private CircleImageView mImageAvatar;
    private SeekBar mSeekBarProgressSong;
    private ConstraintLayout mLayoutFull, mLayoutBottom;
    private Animation mAnimation;
    private MediaPlayer mMediaPlayer;
    private int mCheckShuffleLoop = 1;
    private PlayMusicService mPlayMusicService;
    private ServiceConnection mServiceConnection;

    public static PlayMusicFragment newOnlineInstance(List<Song> songList, int position) {
        PlayMusicFragment fragment = new PlayMusicFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_PLAY_SONG_ONLINE_LIST,
                (ArrayList<? extends Parcelable>) songList);
        args.putInt(EXTRA_ONLINE_SONG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public static PlayMusicFragment newOfflineInstance(List<OfflineSong> songList, int position,
            boolean check) {
        PlayMusicFragment fragment = new PlayMusicFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_PLAY_SONG_OFFLINE_LIST,
                (ArrayList<? extends Parcelable>) songList);
        args.putInt(EXTRA_OFFLINE_SONG_POSITION, position);
        args.putBoolean(EXTRA_IS_OFFLINE, check);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mLayoutBottom = view.findViewById(R.id.constrain_buttom);
        mLayoutFull = view.findViewById(R.id.constrain_full_screen);
        mTextTitle = view.findViewById(R.id.text_title);
        mTextArtist = view.findViewById(R.id.text_artist);
        mTextTitleBottom = view.findViewById(R.id.text_title_bottom);
        mTextArtistBottom = view.findViewById(R.id.text_artist_bottom);
        mTextCurrentTime = view.findViewById(R.id.text_current_time);
        mTextDuarationTime = view.findViewById(R.id.text_duaration_time);
        mImageAvatar = view.findViewById(R.id.image_avatar);
        mSeekBarProgressSong = view.findViewById(R.id.seek_bar_playinh_process);
        mButtonBack = view.findViewById(R.id.button_back);
        mButtonPrevious = view.findViewById(R.id.button_previous);
        mButtonPlay = view.findViewById(R.id.button_play);
        mButtonNext = view.findViewById(R.id.button_next);
        mButtonPreviousBottom = view.findViewById(R.id.button_previous_bottom);
        mButtonPlayBottom = view.findViewById(R.id.button_play_bottom);
        mButtonNextBottom = view.findViewById(R.id.button_next_bottom);
        mButtonLoop = view.findViewById(R.id.button_loop);
        mButtonShuffle = view.findViewById(R.id.button_shuffle);
        mButtonDownload = view.findViewById(R.id.button_download);
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotated_avatar_dics);

        mButtonBack.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonPreviousBottom.setOnClickListener(this);
        mButtonNextBottom.setOnClickListener(this);
        mButtonPlayBottom.setOnClickListener(this);
        mButtonLoop.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
        mButtonDownload.setOnClickListener(this);
        mLayoutBottom.setOnClickListener(this);
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
                mPlayMusicService.registerClient(PlayMusicFragment.this);
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
                mLayoutFull.setVisibility(View.GONE);
                mLayoutBottom.setVisibility(View.VISIBLE);
                break;
            case R.id.constrain_buttom:
                mLayoutFull.setVisibility(View.VISIBLE);
                mLayoutBottom.setVisibility(View.GONE);
                break;
            case R.id.button_previous_bottom:
                mPlayMusicService.previousSong();
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
                break;
            case R.id.button_play_bottom:
                if (mPlayMusicService.isMusicPlaying()) {
                    mPlayMusicService.pauseSong();
                    mButtonPlayBottom.setImageResource(R.drawable.ic_play_button);
                    mImageAvatar.setAnimation(null);
                } else {
                    mPlayMusicService.continueSong();
                    mButtonPlayBottom.setImageResource(R.drawable.ic_pause_button);
                    mImageAvatar.setAnimation(mAnimation);
                }
                break;
            case R.id.button_next_bottom:
                mPlayMusicService.nextSong(true);
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
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
                mPlayMusicService.nextSong(true);
                mTextCurrentTime.setText(R.string.text_time);
                mTextDuarationTime.setText(R.string.text_time);
                break;
            case R.id.button_loop:
                setViewLoopButton();
                break;
            case R.id.button_shuffle:
                setViewShuffleButton();
                break;
            case R.id.button_download:
                onRequestStoragePermisson();
                break;
            default:
        }
    }

    private void setViewLoopButton() {
        if (mCheckShuffleLoop == PlayMusicService.CHECK_NO_LOOP) {
            mButtonLoop.setImageResource(R.drawable.ic_repeat_one);
            mCheckShuffleLoop = CHECK_LOOP_ONE;
        } else if (mCheckShuffleLoop == CHECK_LOOP_ONE) {
            mButtonLoop.setImageResource(R.drawable.ic_active_loop_button);
            mCheckShuffleLoop = PlayMusicService.CHECK_LOOP_ALL;
        } else {
            mButtonLoop.setImageResource(R.drawable.ic_unactive_loop_button);
            mCheckShuffleLoop = PlayMusicService.CHECK_NO_LOOP;
        }
        mPlayMusicService.setSuffleLoop(mCheckShuffleLoop);
    }

    private void setViewShuffleButton() {
        if (mCheckShuffleLoop != PlayMusicService.CHECK_SHUFFLE) {
            mButtonShuffle.setImageResource(R.drawable.ic_active_shuffle_button);
            mCheckShuffleLoop = PlayMusicService.CHECK_SHUFFLE;
        } else {
            mButtonShuffle.setImageResource(R.drawable.ic_unactive_shuffle_button);
            mCheckShuffleLoop = PlayMusicService.CHECK_NO_LOOP;
        }
        mPlayMusicService.setSuffleLoop(mCheckShuffleLoop);
    }

    private void initData() {
        Bundle bundle = getArguments();
        boolean isOffline = bundle.getBoolean(EXTRA_IS_OFFLINE, false);
        if (bundle.getParcelableArrayList(EXTRA_PLAY_SONG_ONLINE_LIST) != null) {
            int position = bundle.getInt(EXTRA_ONLINE_SONG_POSITION, -1);
            List<Song> songOnlineList = bundle.getParcelableArrayList(EXTRA_PLAY_SONG_ONLINE_LIST);
            onPlayMusicOnlineControl(songOnlineList, position);
        } else {
            int position = bundle.getInt(EXTRA_OFFLINE_SONG_POSITION, -1);
            List<OfflineSong> songOfflineList =
                    bundle.getParcelableArrayList(EXTRA_PLAY_SONG_OFFLINE_LIST);
            onPlayMusicOfflineControl(songOfflineList, position, isOffline);
        }
    }

    private void onPlayMusicOnlineControl(List<Song> songs, int position) {
        mTextTitle.setText(songs.get(position).getTitle());
        mTextArtist.setText(songs.get(position).getArtist().getSingerName());
        mTextTitleBottom.setText(songs.get(position).getTitle());
        mTextArtistBottom.setText(songs.get(position).getArtist().getSingerName());
        mButtonPlay.setImageResource(R.drawable.ic_pause_button);
        mButtonPlayBottom.setImageResource(R.drawable.ic_pause_button);
        mImageAvatar.setAnimation(mAnimation);
        Glide.with(this).load(songs.get(position).getArtist().getAvatarUrl()).into(mImageAvatar);
        onPlayMusicOnline(songs, position);
    }

    private void onPlayMusicOfflineControl(List<OfflineSong> songs, int position,
            boolean isOffline) {
        mTextTitle.setText(songs.get(position).getTitle());
        mTextArtist.setText(songs.get(position).getArtistName());
        mTextTitleBottom.setText(songs.get(position).getTitle());
        mTextArtistBottom.setText(songs.get(position).getArtistName());
        mButtonPlay.setImageResource(R.drawable.ic_pause_button);
        mButtonPlayBottom.setImageResource(R.drawable.ic_pause_button);
        mImageAvatar.setAnimation(mAnimation);
        mButtonDownload.setVisibility(View.INVISIBLE);
        Glide.with(this).load(R.drawable.default_avatart_song).into(mImageAvatar);
        onPlayMusicOffline(songs, position, isOffline);
    }

    public void onPlayMusicOnline(List<Song> songList, int position) {
        Intent intent = PlayMusicService.getOnlineInstance(getContext(), songList, position);
        getContext().startService(intent);
        getContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onPlayMusicOffline(List<OfflineSong> songList, int position, boolean isOffline) {
        Intent intent =
                PlayMusicService.getOfflineInstance(getContext(), songList, position, isOffline);
        getContext().startService(intent);
        getContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void updateMediaToClient(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        if (mMediaPlayer.isPlaying()) {
            mTextCurrentTime.setText(dateFormat.format(mMediaPlayer.getCurrentPosition()));
            mTextDuarationTime.setText(dateFormat.format(mMediaPlayer.getDuration()));
            mSeekBarProgressSong.setMax(mMediaPlayer.getDuration());
            mButtonPlay.setImageResource(R.drawable.ic_pause_button);
            mButtonPlayBottom.setImageResource(R.drawable.ic_pause_button);
            mImageAvatar.setAnimation(mAnimation);
            mSeekBarProgressSong.setProgress(mediaPlayer.getCurrentPosition());
        } else {
            mButtonPlay.setImageResource(R.drawable.ic_play_button);
            mButtonPlayBottom.setImageResource(R.drawable.ic_play_button);
            mImageAvatar.setAnimation(null);
        }
    }

    @Override
    public void onUpdateOnlineSongDetail(Song song) {
        mTextTitle.setText(song.getTitle());
        mTextArtist.setText(song.getArtist().getSingerName());
        mTextTitleBottom.setText(song.getTitle());
        mTextArtistBottom.setText(song.getArtist().getSingerName());
        Glide.with(this).load(song.getArtist().getAvatarUrl()).into(mImageAvatar);
    }

    @Override
    public void onUpdateOfflineSongDetail(OfflineSong song) {
        mTextTitle.setText(song.getTitle());
        mTextArtist.setText(song.getArtistName());
        mTextTitleBottom.setText(song.getTitle());
        mTextArtistBottom.setText(song.getArtistName());
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
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mPlayMusicService.downloadSong();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_PERMISSION_CODE);
        }
    }
}
