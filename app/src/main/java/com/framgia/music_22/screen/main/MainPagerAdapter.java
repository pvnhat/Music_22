package com.framgia.music_22.screen.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.framgia.music_22.screen.gernes_screen.GenreFragment;
import com.framgia.music_22.screen.home_screen.HomeFragment;
import com.framgia.music_22.screen.offline_screen.OfflineFragment;
import com.framgia.music_22.screen.search_screen.SearchFragment;
import com.framgia.music_22.utils.Constant;
import com.framgia.music_22.utils.TypeTab;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TypeTab.TAB_HOME:
                return HomeFragment.newInstance();
            case TypeTab.TAB_OFFLINE:
                return OfflineFragment.newInstance();
            case TypeTab.TAB_ARTIST:
                return SearchFragment.newInstance();
            case TypeTab.TAB_GENRES:
                return GenreFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return Constant.TAB_COUNT;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
