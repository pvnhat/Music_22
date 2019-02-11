package com.framgia.music_22.screen.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.framgia.music_22.data.source.local.sqlite.DatabaseSQLite;
import com.framgia.music_22.utils.ConnectionChecking;
import com.framgia.music_22.utils.TypeTab;
import com.framgia.vnnht.music_22.R;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {

    private static final String SONG_ID = "SongId";
    private static final String TITLE = "Title";
    private static final String GENRE = "Genre";
    private static final String SONG_PATH = "SongPath";
    private static final String ARTIST_NAME = "ArtistName";
    private static final String DURATION = "Duration";

    private ViewPager mPagerMain;
    private BottomNavigationView mBottomNavigationViewavigation;
    private MenuItem mPrevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        initDatabase();
    }

    private void initDatabase() {
        DatabaseSQLite databaseSQLite = new DatabaseSQLite(this);
        databaseSQLite.queryData("CREATE TABLE IF NOT EXISTS " + DatabaseSQLite.TABLE_NAME + " ("
                + SONG_ID + " TEXT PRIMARY KEY,"
                + TITLE + " TEXT,"
                + GENRE + " TEXT,"
                + SONG_PATH + " TEXT,"
                + ARTIST_NAME + " TEXT,"
                + DURATION + " INTEGER)");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_homepage:
                mPagerMain.setCurrentItem(TypeTab.TAB_HOME);
                return true;
            case R.id.item_offline:
                mPagerMain.setCurrentItem(TypeTab.TAB_OFFLINE);
                return true;
            case R.id.item_singer:
                mPagerMain.setCurrentItem(TypeTab.TAB_ARTIST);
                return true;
            case R.id.item_gernes:
                mPagerMain.setCurrentItem(TypeTab.TAB_GENRES);
                return true;
        }
        return false;
    }

    private void initFragment() {
        mBottomNavigationViewavigation = findViewById(R.id.navigation);
        mBottomNavigationViewavigation.setOnNavigationItemSelectedListener(this);
        mPagerMain = findViewById(R.id.pager_main);
        mPagerMain.addOnPageChangeListener(this);
        setUpViewPager(mPagerMain);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPrevMenuItem != null) {
            mPrevMenuItem.setChecked(false);
        } else {
            mBottomNavigationViewavigation.getMenu().getItem(0).setChecked(true);
        }
        mBottomNavigationViewavigation.getMenu().getItem(position).setChecked(true);
        mPrevMenuItem = mBottomNavigationViewavigation.getMenu().getItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setUpViewPager(ViewPager viewPager) {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);
        ConnectionChecking connectionChecking = new ConnectionChecking(getApplicationContext());
        if (!connectionChecking.isNetworkConnection()){
            viewPager.setCurrentItem(TypeTab.TAB_OFFLINE);
        }
    }
}
