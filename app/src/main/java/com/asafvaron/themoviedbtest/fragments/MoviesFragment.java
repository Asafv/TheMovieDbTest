package com.asafvaron.themoviedbtest.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.asafvaron.themoviedbtest.MyApp;
import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.activity.MainActivity;
import com.asafvaron.themoviedbtest.adapters.MoviesGridAdapter;
import com.asafvaron.themoviedbtest.database.MoviesContract;
import com.asafvaron.themoviedbtest.database.MoviesDbHelper;
import com.asafvaron.themoviedbtest.views.GridSpacingItemDecoration;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.model.MoviesResponse;
import com.asafvaron.themoviedbtest.rest.ApiClient;
import com.asafvaron.themoviedbtest.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MoviesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        MoviesGridAdapter.MoviesGridAdapterListener {

    private static final String TAG = MoviesFragment.class.getSimpleName();

    private static final int LOADER_ID = 1;
    private RecyclerView mRvGridList;
    private MoviesGridAdapter mMoviesGridAdapter;
    private ProgressBar mProgress;
    private List<Movie> mCurrentMovieList;

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movies_frag_layout, container, false);

        mProgress = (ProgressBar) root.findViewById(R.id.progress);
        mRvGridList = (RecyclerView) root.findViewById(R.id.rv_grid_list);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SQLiteDatabase db = new MoviesDbHelper(getContext()).getReadableDatabase();
        if (db.query(MoviesContract.TopRatedMovies.TABLE_NAME, null, null, null, null, null, null) != null) {
            Log.d(TAG, "onActivityCreated: init loader");
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else
            getTopRatedMovies();

        db.close();
    }

    private void getTopRatedMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getTopRatedMovies(ApiClient.API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> topRated = response.body().getResults();
                Log.d(TAG, "onResponse() returned: " + topRated.size());
                saveTopRated(topRated);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ERR: " + t.getLocalizedMessage());
            }
        });
    }

    private void saveTopRated(List<Movie> topRated) {
        for (Movie m : topRated) {
            MyApp.getContext().getContentResolver()
                    .insert(MoviesContract.TopRatedMovies.CONTENT_URI, m.getValues());
        }
        Log.d(TAG, "saveTopRated: restart loader");
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        mProgress.setVisibility(View.VISIBLE);
        return new CursorLoader(getContext(), MoviesContract.TopRatedMovies.CONTENT_URI,
                null, null, null, MoviesContract.TopRatedMovies.COLUMN_MOVIE_ID + " asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if (c.moveToNext()) {
            Log.d(TAG, "onLoadFinished: c != null");
            mCurrentMovieList = getMovies(c);
            Log.d(TAG, "mCurrentMovieList.size(): " + mCurrentMovieList.size());

            mMoviesGridAdapter = new MoviesGridAdapter(mCurrentMovieList);
            mMoviesGridAdapter.setListener(this);
            mRvGridList.setAdapter(mMoviesGridAdapter);
            mProgress.setVisibility(View.GONE);
        } else {
            Log.w(TAG, "onLoadFinished: c == null");
            getTopRatedMovies();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }

    public List<Movie> getMovies(Cursor c) {
        List<Movie> movies = new ArrayList<>();

        while (c.moveToNext()) {
            Movie m = new Movie();
            m.setId(c.getInt(c.getColumnIndex(MoviesContract.TopRatedMovies.COLUMN_MOVIE_ID)));
            m.setTitle(c.getString(c.getColumnIndex(MoviesContract.TopRatedMovies.COLUMN_TITLE)));
            m.setOriginalTitle(c.getString(c.getColumnIndex(MoviesContract.TopRatedMovies.COLUMN_ORIGINAL_TITLE)));
            m.setOverview(c.getString(c.getColumnIndex(MoviesContract.TopRatedMovies.COLUMN_OVERVIEW)));
            m.setVoteAverage(c.getDouble(c.getColumnIndex(MoviesContract.TopRatedMovies.COLUMN_VOTE_AVERAGE)));
            m.setVoteCount(c.getInt(c.getColumnIndex(MoviesContract.TopRatedMovies.COLUMN_VOTE_COUNT)));
            m.setPosterPath(c.getString(c.getColumnIndex(MoviesContract.TopRatedMovies.COLUMN_POSTER)));
            m.setReleaseDate(c.getString(c.getColumnIndex(MoviesContract.TopRatedMovies.COLUMN_RELEASE_DATE)));
            Log.d(TAG, "getMovies: m= " + m.toString());
            movies.add(m);
        }
        return movies;
    }

    @Override
    public void onMovieClicked(int pos) {
        Movie movie = mCurrentMovieList.get(pos);
        ((MainActivity) getActivity()).showInfoFragment(movie);
    }
}
