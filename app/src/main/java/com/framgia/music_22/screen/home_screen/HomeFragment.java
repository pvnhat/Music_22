package com.framgia.music_22.screen.home_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.framgia.music_22.data.RequestCallbackData;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.model.Song;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.ParseRemoteJsonData;
import com.framgia.music_22.utils.Constant;
import com.framgia.music_22.utils.TypeGenre;
import com.framgia.vnnht.music_22.R;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class HomeFragment extends Fragment
        implements HomePageContract.View, ViewPager.OnPageChangeListener {

    private ViewPager mViewPagerBanner;
    private LinearLayout mLinearDots;
    private ImageView[] mImageDots;
    private HomePageContract.Presenter mPresenter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initView(view);
        onReceiveSongList();
        return view;
    }

    private void onReceiveSongList() {
        SongRepository songRepository =
                SongRepository.getsInstance(ParseRemoteJsonData.getInstance());
        if (songRepository != null) {
            mPresenter = new HomePresenter(this, songRepository);
            mPresenter.getSongByGenres(TypeGenre.ALTERNATIVEROCK);
        }
    }

    private void initView(View view) {
        mViewPagerBanner = view.findViewById(R.id.viewpager_banner);
        mViewPagerBanner.setOnPageChangeListener(this);
        mLinearDots = view.findViewById(R.id.linear_dots);
        SlidePagerAdapter slidePagerAdapter = new SlidePagerAdapter(getActivity());
        mViewPagerBanner.setAdapter(slidePagerAdapter);
        onCreateDots(0);
    }

    public void onCreateDots(int current_possion) {
        if (mLinearDots != null) {
            mLinearDots.removeAllViews();
        }
        mImageDots = new ImageView[Constant.NUMBER_OF_BANNER];

        for (int i = 0; i < Constant.NUMBER_OF_BANNER; i++) {
            mImageDots[i] = new ImageView(getActivity());
            if (i == current_possion) {
                mImageDots[i].setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.active_dots));
            } else {
                mImageDots[i].setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.unactive_dots));
            }
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(Constant.MARGIN_LEFT_RIGHT_DOTS, 0,
                    Constant.MARGIN_LEFT_RIGHT_DOTS, 0);
            mLinearDots.addView(mImageDots[i], layoutParams);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onCreateDots(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onGetSongByGenreSuccess(MoreSong moreSong) {
        // for testing
        System.out.println(moreSong.getSongsList().get(0).getTitle());
    }

    @Override
    public void onError(Exception ex) {
        // FOR TESTING
        System.out.println(ex.getMessage());
    }
}
