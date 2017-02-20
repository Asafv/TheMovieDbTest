package com.asafvaron.themoviedbtest.fragments;

import android.database.Cursor;
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

import com.asafvaron.themoviedbtest.MoviesApi;
import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.adapters.MoviesGridAdapter;
import com.asafvaron.themoviedbtest.database.MoviesContract;
import com.asafvaron.themoviedbtest.item_decorations.GridSpacingItemDecoration;
import com.asafvaron.themoviedbtest.listeners.ResultCallback;
import com.asafvaron.themoviedbtest.objects.MyMovie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MoviesFragment.class.getSimpleName();

    private static final int LOADER_ID = 1;
    private RecyclerView mRvGridList;
    private MoviesGridAdapter mMoviesGridAdapter;
    private ProgressBar mProgress;

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

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void getSomeData() {
//        MoviesApiEnum.INSTANCE.getSomeMovies();
        MoviesApi.getInstance().getSomeMovies(new ResultCallback<Boolean>() {

            @Override
            public void onResult(Boolean data, String err) {
                if (data) {
                    Log.i(TAG, "onResult: restartLoader");
                    getLoaderManager().restartLoader(LOADER_ID, null, MoviesFragment.this);
                } else
                    Log.e(TAG, "onResult: ERR: " + err);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        mProgress.setVisibility(View.VISIBLE);
        return new CursorLoader(getContext(), MoviesContract.Movies.CONTENT_URI,
                null, null, null, MoviesContract.Movies.COLUMN_MOVIE_ID + " asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if (c.moveToNext()) {
            Log.d(TAG, "onLoadFinished: c != null");
            List<MyMovie> movieList = getMovies(c);
            Log.d(TAG, "movieList.size(): " + movieList.size());

            mMoviesGridAdapter = new MoviesGridAdapter(getContext(), movieList);
            mRvGridList.setAdapter(mMoviesGridAdapter);
        } else {
            Log.d(TAG, "onLoadFinished: c == null");
            getSomeData();
        }
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }

    public List<MyMovie> getMovies(Cursor c) {
        List<MyMovie> movies = new ArrayList<>();

        while (c.moveToNext()) {
            MyMovie m = new MyMovie();
            m.setId(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_MOVIE_ID)));
            m.setTitle(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_TITLE)));
            m.setOriginal_title(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_ORIGINAL_TITLE)));
            m.setOverview(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_OVERVIEW)));
            m.setRuntime(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_RUNTIME)));
            m.setVote_average(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_VOTE_AVERAGE)));
            m.setVote_count(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_VOTE_COUNT)));
            m.setPoster_path(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_POSTER)));
            m.setRelease_date(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_RELEASE_DATE)));
            Log.d(TAG, "getMovies: m= " + m.toString());
            movies.add(m);
        }
        return movies;
    }
}
