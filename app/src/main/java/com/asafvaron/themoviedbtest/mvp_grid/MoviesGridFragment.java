package com.asafvaron.themoviedbtest.mvp_grid;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.Utils.Consts;
import com.asafvaron.themoviedbtest.Utils.Prefs;
import com.asafvaron.themoviedbtest.activity.MainActivity;
import com.asafvaron.themoviedbtest.adapters.MoviesGridAdapter;
import com.asafvaron.themoviedbtest.database.movies.MoviesContract;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.mvp_info.MovieInfoFragment;
import com.asafvaron.themoviedbtest.views.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MoviesGridFragment extends Fragment
        implements GridContract.View {

    private static final String TAG = MoviesGridFragment.class.getSimpleName();

    private MoviesGridAdapter mMoviesGridAdapter;

    private GridContract.Actions mActions;

    @BindView(R.id.progress)
    ProgressBar mProgress;

    @BindView(R.id.rv_grid_list)
    RecyclerView mRvGridList;

    public static MoviesGridFragment newInstance() {
        return new MoviesGridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setHasOptionsMenu(true);

        mActions = new GridPresenter(new GridData(getLoaderManager()), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View root = inflater.inflate(R.layout.movies_grid_frag_layout, container, false);
        ButterKnife.bind(this, root);

        // set the color
        mProgress.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        mMoviesGridAdapter = new MoviesGridAdapter(new ArrayList<Movie>(0), this);
        mRvGridList.setAdapter(mMoviesGridAdapter);
        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
        mRvGridList.setHasFixedSize(true);
        mRvGridList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvGridList.setItemAnimator(new DefaultItemAnimator());
        // set itemDecoration for grid spacing
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(getContext(), R.dimen.grid_item_offset);
        mRvGridList.addItemDecoration(decoration);
    }

    @Override
    public void onResume() {
        super.onResume();
        // get the movies from api
        mActions.loadMovies(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        updateTitle(Prefs.getInstance().getString(Consts.LAST_DB_TYPE, getString(R.string.action_now_playing)));

        inflater.inflate(R.menu.movie_grid_menu, menu);
        ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setDisplayShowHomeEnabled(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_now_playing:
                mActions.loadMovies(MoviesContract.MovieTypes.NOW_PLAYING);
                return true;

            case R.id.action_upcoming:
                mActions.loadMovies(MoviesContract.MovieTypes.UPCOMING);
                return true;

            case R.id.action_top_rated:
                mActions.loadMovies(MoviesContract.MovieTypes.TOP_RATED);
                return true;

            case R.id.action_popular:
                mActions.loadMovies(MoviesContract.MovieTypes.POPULAR);
                return true;

            case R.id.action_favorites:
                loadFavoritesFragment();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFavoritesFragment() {
    }

    @Override
    public void updateProgress(boolean visibility) {
        mProgress.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateTitle(String title) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void onSuccessMoviesLoaded(List<Movie> movies) {
        Log.i(TAG, "onSuccessMoviesLoaded: ");
        mMoviesGridAdapter.setData(movies);
    }

    @Override
    public void onFailedLoadMovies(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMovieClicked(ImageView imgView, Movie movie) {
        Log.d(TAG, "onMovieClicked: " + movie.getTitle());
        ((MainActivity) getActivity()).showInfoFragment(imgView, movie);
    }

    @Override
    public void popInfoIfNeeded() {
        if (getActivity().getSupportFragmentManager()
                .findFragmentByTag(MovieInfoFragment.class.getSimpleName()) != null) {
            getActivity().onBackPressed();
        }
    }
}
