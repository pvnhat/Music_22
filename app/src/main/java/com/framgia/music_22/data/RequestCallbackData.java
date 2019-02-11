package com.framgia.music_22.data;

import com.framgia.music_22.data.model.Song;
import java.util.ArrayList;

public interface RequestCallbackData<T> {
    void onGetDataSuccess(T songList);

    void onGetDataError(Exception e);
}
