package com.framgia.music_22.utils;

import android.support.annotation.IntDef;

public class Constant {

    public static final int TIME_SLEEP = 3000;
    public static final int TAB_COUNT = 4;

    public static final int NUMBER_OF_BANNER = 6;
    public static final int MARGIN_LEFT_RIGHT_DOTS = 4;
    public static final String BANNER_ITEM1 =
            "https://i.ytimg.com/vi/U5ruMh6_Jwk/maxresdefault.jpg";
    public static final String BANNER_ITEM2 =
            "https://i.ytimg.com/vi/M8o6VioxY6s/maxresdefault.jpg";
    public static final String BANNER_ITEM3 =
            "https://img.youtube.com/vi/omM5XAN6m70/mqdefault.jpg";
    public static final String BANNER_ITEM4 = "http://s.nhac.vn/v1/seo/song/s1/0/0/463/474207.jpg";
    public static final String BANNER_ITEM5 =
            "https://i.ytimg.com/vi/UCXao7aTDQM/maxresdefault.jpg";
    public static final String BANNER_ITEM6 =
            "http://img.hplus.com.vn/460x260/poster/2015/04/21/194089-DOAN_DUONG_VANG.jpg";

    @IntDef({ Tab.TAB_HOME, Tab.TAB_OFFLINE, Tab.TAB_ARTIST, Tab.TAB_GENRES })
    public @interface Tab {
        int TAB_HOME = 0;
        int TAB_OFFLINE = 1;
        int TAB_ARTIST = 2;
        int TAB_GENRES = 3;
    }
}
