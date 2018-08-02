package com.framgia.music_22.utils;

import android.support.annotation.StringDef;

@StringDef({
        TypeGenre.ALL_MUSIC, TypeGenre.ALL_AUDIO, TypeGenre.ALTERNATIVEROCK, TypeGenre.AMBIENT,
        TypeGenre.CLASSICAL, TypeGenre.COUNTRY
})
public @interface TypeGenre {
    String ALL_MUSIC = "Allmusic";
    String ALL_AUDIO = "Audio";
    String ALTERNATIVEROCK = "AlternativeRock";
    String AMBIENT = "Ambient";
    String CLASSICAL = "Classical";
    String COUNTRY = "Country";
}
