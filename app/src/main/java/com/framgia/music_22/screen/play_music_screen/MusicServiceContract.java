package com.framgia.music_22.screen.play_music_screen;

import android.media.MediaPlayer;
import com.framgia.music_22.data.model.Song;

public interface MusicServiceContract {
    void updateMediaToClient(MediaPlayer mediaPlayer);

    void getUpdateSongDetail(Song song);
}
