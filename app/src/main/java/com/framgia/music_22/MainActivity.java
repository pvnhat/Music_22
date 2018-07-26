package com.framgia.music_22;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.framgia.music_22.screen.gernes_screen.FragGernesPage;
import com.framgia.music_22.screen.home_screen.FragHomePage;
import com.framgia.music_22.screen.offline_screen.FragOfflinePage;
import com.framgia.music_22.screen.singer_screen.FragSingerPage;
import com.framgia.vnnht.music_22.R;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Fragment mFragment = null;
    private BottomNavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(this);
        mFragment = new FragHomePage();
        fragmentManagerMethod(mFragment);
    }

    private void fragmentManagerMethod(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_homepage:
                mFragment = new FragHomePage();
                fragmentManagerMethod(mFragment);
                return true;
            case R.id.item_offline:
                mFragment = new FragOfflinePage();
                fragmentManagerMethod(mFragment);
                return true;
            case R.id.item_singer:
                mFragment = new FragSingerPage();
                fragmentManagerMethod(mFragment);
                return true;
            case R.id.item_gernes:
                mFragment = new FragGernesPage();
                fragmentManagerMethod(mFragment);
                return true;
        }
        return false;
    }
}
