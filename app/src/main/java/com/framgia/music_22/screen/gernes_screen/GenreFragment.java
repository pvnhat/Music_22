package com.framgia.music_22.screen.gernes_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.vnnht.music_22.R;

public class GenreFragment extends Fragment {
    public static GenreFragment newInstance() {
        return new GenreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gernes, container, false);
        return view;
    }
}
