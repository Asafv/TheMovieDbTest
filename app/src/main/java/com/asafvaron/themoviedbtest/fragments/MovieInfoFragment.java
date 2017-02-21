package com.asafvaron.themoviedbtest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by asafvaron on 20/02/2017.
 */
// TODO: 21/02/2017 add menu - add to favs
public class MovieInfoFragment extends Fragment {
    private static final String TAG = MovieInfoFragment.class.getSimpleName();
    private Movie mMovie;

    public static MovieInfoFragment newInstance(Movie movie) {
        MovieInfoFragment fragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments()!=null)
            mMovie = (Movie) getArguments().getSerializable("movie");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movie_info_layout, container, false);

        Glide.with(getActivity())
                .load(mMovie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) root.findViewById(R.id.iv_poster));

        ((TextView)root.findViewById(R.id.tv_title)).setText(mMovie.getTitle());
        ((TextView)root.findViewById(R.id.tv_overview)).setText(mMovie.getOverview());
        ((TextView)root.findViewById(R.id.tv_release_date)).setText(mMovie.getReleaseDate());
        ((TextView)root.findViewById(R.id.tv_vote_average)).setText(String.valueOf(mMovie.getVoteAverage()));

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.info_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }
}
