package com.asafvaron.themoviedbtest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.activity.MainActivity;
import com.asafvaron.themoviedbtest.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asafvaron on 20/02/2017.
 */
// TODO: 21/02/2017 add menu - add to favs
public class MovieInfoFragment extends Fragment {
    private static final String TAG = MovieInfoFragment.class.getSimpleName();
    private Movie mMovie;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.tv_overview)
    TextView mOverview;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;

    @BindView(R.id.tv_vote_average)
    TextView mVoteRate;

    @BindView(R.id.iv_poster)
    ImageView mPoster;

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

        if (getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable("movie");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movie_info_layout, container, false);
        ButterKnife.bind(this, root);

        Glide.with(getActivity())
                .load(mMovie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mPoster);

        mTitle.setText(mMovie.getTitle());
        mOverview.setText(mMovie.getOverview());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoteRate.setText(String.valueOf(mMovie.getVoteAverage()));

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.info_menu, menu);
        // XXX Yossi please let me know if that's the way
        ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setTitle(getActivity().getString(R.string.movie_info_ab_title));
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "android.R.id.home");
                getActivity().onBackPressed();
                return true;
            case R.id.action_favorite:
                Log.d(TAG, "action_favorite");
                Toast.makeText(getContext(), "Add to favorites", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
