package com.framgia.music_22.screen.play_music_screen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.framgia.vnnht.music_22.R;

public class MusicNotification extends Notification {

    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_CANCEL = "action_cancel";
    private static final String TICK_KET_NOTIFICATION = "Song Playing";

    private Context mContext;
    protected NotificationManager mNotificationManager;

    public MusicNotification(Context context) {
        super();
        mContext = context;
    }

    public Notification initNotification(String title, String artist, boolean isPlayingMusic) {
        String string = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) mContext.getSystemService(string);
        long when = System.currentTimeMillis();
        Notification.Builder builder = new Notification.Builder(mContext);
        Notification notification = builder.getNotification();
        notification.when = when;
        notification.tickerText = TICK_KET_NOTIFICATION;
        notification.icon = R.drawable.ic_notification_playing;

        RemoteViews remoteViews =
                new RemoteViews(mContext.getPackageName(), R.layout.notification_play_music);
        setListeners(remoteViews, title, artist, isPlayingMusic);

        notification.contentView = remoteViews;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        return notification;
    }

    public void setListeners(RemoteViews view, String title, String artist,
            boolean isPlayingMusic) {
        view.setTextViewText(R.id.text_title_bottom, title);
        view.setTextViewText(R.id.text_artist_bottom, artist);

        //play listener
        Intent playIntent = new Intent(ACTION_PLAY);
        playIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pPlay = PendingIntent.getBroadcast(mContext, 0, playIntent, 0);
        int imageResoure;
        if (isPlayingMusic) {
            imageResoure = R.drawable.ic_pause_button;
        } else {
            imageResoure = R.drawable.ic_play_button;
        }
        view.setImageViewResource(R.id.button_play_bottom, imageResoure);
        view.setOnClickPendingIntent(R.id.button_play_bottom, pPlay);

        //next listener
        Intent nextIntent = new Intent(ACTION_NEXT);
        playIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pNext = PendingIntent.getBroadcast(mContext, 0, nextIntent, 0);
        view.setOnClickPendingIntent(R.id.button_next_bottom, pNext);

        //previous listener
        Intent previousIntent = new Intent(ACTION_PREVIOUS);
        playIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pPrevious = PendingIntent.getBroadcast(mContext, 0, previousIntent, 0);
        view.setOnClickPendingIntent(R.id.button_previous_bottom, pPrevious);

        //previous listener
        Intent cancelIntent = new Intent(ACTION_CANCEL);
        playIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pCancel = PendingIntent.getBroadcast(mContext, 0, cancelIntent, 0);
        if (!isPlayingMusic) {
            view.setOnClickPendingIntent(R.id.button_cancel_notification, pCancel);
        }
    }
}
