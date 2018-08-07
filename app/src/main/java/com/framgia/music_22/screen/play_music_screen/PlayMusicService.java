package com.framgia.music_22.screen.play_music_screen;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.utils.Constant;
import com.framgia.vnnht.music_22.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicService extends Service {

    private static final String EXTRA_MUSIC_LIST = "EXTRA_MUSIC_LIST";
    private static final String EXTRA_MUSIC_POSITION = "EXTRA_MUSIC_POSITION";
    private static final int DELAY_TIME = 1000;
    private static final String FILE_DIR = "file://\" + \"/sdcard/Music/";
    private static final int ID_FOREGROUND_SERVICE = 1;

    private MusicServiceContract mView;
    private IBinder mIBinder = new LocalBinder();
    private int mPosition = 0;
    private MediaPlayer mMediaPlayer;
    private List<Song> mSongList = new ArrayList<>();

    public static Intent getInstance(Context context, List<Song> songList, int position) {
        Intent intent = new Intent(context, PlayMusicService.class);
        intent.putParcelableArrayListExtra(EXTRA_MUSIC_LIST,
                (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(EXTRA_MUSIC_POSITION, position);
        return intent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mSongList = intent.getParcelableArrayListExtra(EXTRA_MUSIC_LIST);
        int position = intent.getIntExtra(EXTRA_MUSIC_POSITION, 0);
        onPlayMusicOnlineService(mSongList, position);
        return START_REDELIVER_INTENT;
    }

    public void onPlayMusicOnlineService(List<Song> songList, int position) {
        mPosition = position;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(songList.get(position).getStreamUrl() + Constant.CLIENT_ID);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mView.updateMediaToClient(mMediaPlayer);
                            handler.postDelayed(this, DELAY_TIME);
                            mediaPlayer.setOnCompletionListener(
                                    new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            nextSong();
                                        }
                                    });
                        }
                    }, DELAY_TIME);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        startForeground(ID_FOREGROUND_SERVICE, initForegroundService());
    }

    public Notification initForegroundService() {
        Intent notitificationInten = new Intent(this, PlayMusicService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notitificationInten, 0);
        Notification notification =
                new NotificationCompat.Builder(this).setContentTitle(getString(R.string.text_notification_playing))
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_notification_playing)
                        .setContentTitle(mSongList.get(mPosition).getTitle())
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();
        return notification;
    }

    public void registerClient(Activity activity) {
        mView = (MusicServiceContract) activity;
    }

    class LocalBinder extends Binder {
        public PlayMusicService getServiceInstance() {
            return PlayMusicService.this;
        }
    }

    public boolean isMusicPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pauseSong() {
        mMediaPlayer.pause();
    }

    public void continueSong() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void nextSong() {
        if (mPosition == mSongList.size()) {
            mPosition = 0;
        } else {
            mPosition++;
        }
        mMediaPlayer.release();
        onPlayMusicOnlineService(mSongList, mPosition);
        mView.getUpdateSongDetail(mSongList.get(mPosition));
    }

    public void previousSong() {
        if (mPosition == 0) {
            mPosition = mSongList.size() - 1;
        } else {
            mPosition--;
        }
        mMediaPlayer.release();
        onPlayMusicOnlineService(mSongList, mPosition);
        onPlayMusicOnlineService(mSongList, mPosition);
        mView.getUpdateSongDetail(mSongList.get(mPosition));
    }

    public void downloadSong() {
        DownloadManager downloadManager =
                (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(mSongList.get(mPosition).getStreamUrl() + Constant.CLIENT_ID);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(getString(R.string.text_download) + mSongList.get(mPosition).getTitle());
        request.setDescription(getString(R.string.text_dowloading));
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.parse(FILE_DIR + mSongList.get(mPosition).getTitle()));
        downloadManager.enqueue(request);
    }
}
