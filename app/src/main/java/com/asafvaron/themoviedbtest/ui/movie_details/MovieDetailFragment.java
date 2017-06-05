package com.asafvaron.themoviedbtest.ui.movie_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
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
import com.asafvaron.themoviedbtest.data.api.MoviesApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asafvaron on 20/02/2017.
 */
public class MovieDetailFragment extends Fragment
        implements MovieDetailContract.View {

    private static final String TAG = "MovieDetailFragment";

    private Movie mMovie;
    private MovieDetailContract.Presenter mPresenter;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.tv_overview)
    TextView mOverview;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;

    @BindView(R.id.tv_vote_average)
    TextView mVoteRate;

    @BindView(R.id.tv_runtime)
    TextView mRunTime;

    @BindView(R.id.iv_poster)
    ImageView mPoster;

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
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
            mPresenter = new MovieDetailPresenter(this, new MovieDetailModel(mMovie));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movie_info_layout, container, false);
        ButterKnife.bind(this, root);

        ViewCompat.setTransitionName(mPoster, "2");

        Log.e("XXX", "onCreateView: movie id: " + mMovie.getId());
        Glide.with(getActivity())
                .load(MoviesApi.IMAGE_URL + mMovie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mPoster);

        mTitle.setText(mMovie.getTitle());
        mOverview.setText(mMovie.getOverview());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoteRate.setText(String.valueOf(mMovie.getVoteAverage()));

        int runTime = mMovie.getRunTime();
        if (runTime != 0) {
            Log.d(TAG, "runtime set from Db");
            setRunTime(runTime);
        } else {
            Log.w(TAG, "fetching runtime from server ");
            mRunTime.setText(getString(R.string.fetch_duration_tmp));
            mPresenter.getMovieRunTime();
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.info_menu, menu);

        // set the Favorites menu icon
        MenuItem favItem = menu.findItem(R.id.action_add_remove_favorite);
        Log.d(TAG, "mMovie.getIsInFavs(): " + mMovie.getIsInFavs());
        favItem.setIcon((mMovie.getIsInFavs() == 1)
                ? R.drawable.ic_favorite
                : R.drawable.ic_favorite_border_black_24dp);

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
            case R.id.action_add_remove_favorite:
                Log.d(TAG, "action_favorite");
                addOrRemoveToFavs(item);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addOrRemoveToFavs(MenuItem item) {
        if (mMovie.getIsInFavs() == 1) {
            mMovie.setIsInFavs(0);
            item.setIcon(R.drawable.ic_favorite_border_black_24dp);
        } else {
            mMovie.setIsInFavs(1);
            item.setIcon(R.drawable.ic_favorite);
        }
        // update the DB after changes
        mPresenter.updateDb(getContext(), mMovie);
    }

    private void setRunTime(int runTime) {
        mRunTime.setText(String.format("%d min", runTime));
    }

    @Override
    public void setMovieRunTime(int runTime) {
        setRunTime(runTime);
        mMovie.setRunTime(runTime);
        mPresenter.updateDb(getContext(), mMovie);
    }

    @Override
    public void failedToGetMovieRunTime(String err) {
        mRunTime.setText("N/A");
        Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
    }

}
