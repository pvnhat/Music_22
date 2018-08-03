package com.framgia.music_22.screen.home_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.framgia.music_22.data.model.MoreSong;
import com.framgia.music_22.data.repository.SongRepository;
import com.framgia.music_22.data.source.ParseRemoteJsonData;
import com.framgia.music_22.screen.song_by_genre_screen.SongByGenreActivity;
import com.framgia.music_22.utils.Constant;
import com.framgia.music_22.utils.TypeGenre;
import com.framgia.vnnht.music_22.R;

public class HomeFragment extends Fragment
        implements HomePageContract.View, ViewPager.OnPageChangeListener, View.OnClickListener {

    private LinearLayout mLinearDots;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ViewPager viewPagerBanner = view.findViewById(R.id.viewpager_banner);
        mLinearDots = view.findViewById(R.id.linear_dots);
        ImageButton buttonAllAudio = view.findViewById(R.id.button_all_audios);
        ImageButton buttonAllSong = view.findViewById(R.id.button_all_song);
        ImageButton buttonAlternativeRock = view.findViewById(R.id.button_alternativerock);
        ImageButton buttonAmbient = view.findViewById(R.id.button_ambient);
        ImageButton buttonClassic = view.findViewById(R.id.button_classic);

        viewPagerBanner.setOnPageChangeListener(this);
        buttonAllAudio.setOnClickListener(this);
        buttonAllSong.setOnClickListener(this);
        buttonAlternativeRock.setOnClickListener(this);
        buttonAmbient.setOnClickListener(this);
        buttonClassic.setOnClickListener(this);

        SlidePagerAdapter slidePagerAdapter = new SlidePagerAdapter(getActivity());
        viewPagerBanner.setAdapter(slidePagerAdapter);
        onCreateDots(0);
    }

    public void onCreateDots(int current_possion) {
        if (mLinearDots != null) {
            mLinearDots.removeAllViews();
        }
        ImageView[] imageDots = new ImageView[Constant.NUMBER_OF_BANNER];

        for (int i = 0; i < Constant.NUMBER_OF_BANNER; i++) {
            imageDots[i] = new ImageView(getActivity());
            if (i == current_possion) {
                imageDots[i].setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.active_dots));
            } else {
                imageDots[i].setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.unactive_dots));
            }
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(Constant.MARGIN_LEFT_RIGHT_DOTS, 0,
                    Constant.MARGIN_LEFT_RIGHT_DOTS, 0);
            mLinearDots.addView(imageDots[i], layoutParams);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_all_audios:
                startActivity(new Intent(
                        SongByGenreActivity.getInstance(getContext(), TypeGenre.ALL_AUDIO)));
                break;
            case R.id.button_all_song:
                startActivity(new Intent(
                        SongByGenreActivity.getInstance(getContext(), TypeGenre.ALL_MUSIC)));
                break;
            case R.id.button_alternativerock:
                startActivity(new Intent(
                        SongByGenreActivity.getInstance(getContext(), TypeGenre.ALTERNATIVEROCK)));
                break;
            case R.id.button_ambient:
                startActivity(new Intent(
                        SongByGenreActivity.getInstance(getContext(), TypeGenre.AMBIENT)));
                break;
            case R.id.button_classic:
                startActivity(new Intent(
                        SongByGenreActivity.getInstance(getContext(), TypeGenre.CLASSICAL)));
                break;
        }
    }
}
