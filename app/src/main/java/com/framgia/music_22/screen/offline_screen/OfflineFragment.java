package com.framgia.music_22.screen.offline_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.vnnht.music_22.R;

public class OfflineFragment extends Fragment {

    public static OfflineFragment newInstance() {
        return new OfflineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline, container, false);
        return view;
    }
}
