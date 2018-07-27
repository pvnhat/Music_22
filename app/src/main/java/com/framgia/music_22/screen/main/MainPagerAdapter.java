package com.framgia.music_22.screen.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.framgia.music_22.screen.gernes_screen.GenreFragment;
import com.framgia.music_22.screen.home_screen.HomeFragment;
import com.framgia.music_22.screen.offline_screen.OfflineFragment;
import com.framgia.music_22.screen.singer_screen.SingerFragment;
import com.framgia.music_22.utils.Constant;

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constant.Tab.TAB_HOME:
                return HomeFragment.newInstance();
            case Constant.Tab.TAB_OFFLINE:
                return OfflineFragment.newInstance();
            case Constant.Tab.TAB_ARTIST:
                return SingerFragment.newInstance();
            case Constant.Tab.TAB_GENRES:
                return GenreFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return Constant.TAB_COUNT;
    }
}
