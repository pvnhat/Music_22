package com.framgia.music_22.utils;

import android.support.annotation.StringDef;

@StringDef({
        TypeGenre.ALL_MUSIC, TypeGenre.ALL_AUDIO, TypeGenre.ALTERNATIVEROCK, TypeGenre.AMBIENT,
        TypeGenre.CLASSICAL, TypeGenre.COUNTRY
})
public @interface TypeGenre {
    String ALL_MUSIC = "allmusic";
    String ALL_AUDIO = "audio";
    String ALTERNATIVEROCK = "alternativerock";
    String AMBIENT = "ambient";
    String CLASSICAL = "classical";
    String COUNTRY = "country";
}
