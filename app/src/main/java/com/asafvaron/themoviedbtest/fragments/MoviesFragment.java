package com.asafvaron.themoviedbtest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asafvaron.themoviedbtest.R;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MoviesFragment extends Fragment {


    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movies_frag_layout, container, false);

        return root;
    }
}
