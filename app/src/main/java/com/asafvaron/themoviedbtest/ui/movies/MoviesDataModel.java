package com.asafvaron.themoviedbtest.ui.movies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.asafvaron.themoviedbtest.Utils.Consts;
import com.asafvaron.themoviedbtest.Utils.Prefs;
import com.asafvaron.themoviedbtest.data.api.MoviesApi;
import com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.data.io.MoviesResponse;
import com.asafvaron.themoviedbtest.data.api.MoviesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.asafvaron.themoviedbtest.MyApp.getContext;

/**
 * Created by asafvaron on 16/03/2017.
 */

// TODO: 07/05/2017 convert to Clean pattern
class MoviesDataModel implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MoviesDataModel";

    private static final int LOADER_ID = 1;

    private final LoaderManager mLoaderManager;
    private List<Movie> mMovieList = new ArrayList<>();
    private MoviesService mApiService;
    private String mCurrentDbType;
    private Call<MoviesResponse> call;
    private Listener mListener;

    interface Listener {
        void onSuccessLoad(List<Movie> movies);

        void onFailedLoad(String error);
    }

    MoviesDataModel(LoaderManager loaderManager) {
        this.mLoaderManager = loaderManager;
        init();
    }

    void setListener(MoviesPresenter moviesPresenter) {
        mListener = moviesPresenter;
    }

    // init and load some data
    private void init() {
        mApiService = MoviesApi.getInstance().getMoviesService();
        mCurrentDbType = Prefs.getInstance().getString("last_db_type", MoviesDbContract.MovieTypes.NOW_PLAYING);

        // init loader
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    /**
     * use this method to request the movies from The Movie DB api server
     * and save to data base on success
     */
    void getMovies(String type) {
        // if already has data return it to ui
        if (type == null && mMovieList.size() > 0) {
            Log.i(TAG, "getMovies: return from db");
            mListener.onSuccessLoad(mMovieList);
        }

        // set the new DB type
        mCurrentDbType = (type == null) ? MoviesDbContract.MovieTypes.NOW_PLAYING : type;

        mLoaderManager.restartLoader(LOADER_ID, null, this);
    }

    /**
     * saves the last success query result to DB
     *
     * @param movies result returned from server api
     */
    private void saveToDb(List<Movie> movies) {
        for (Movie m : movies) {
            Uri uri = getContext().getContentResolver()
                    .insert(MoviesDbContract.Movies.CONTENT_URI, m.getValues(mCurrentDbType));
//                            ,MoviesDbContract.Movies.COLUMN_MOVIE_ID + "=" + m.getId(),
//                            null*/);
            Log.w(TAG, "saveToDb: uri:" + uri);
        }
        Prefs.getInstance().putString(Consts.LAST_DB_TYPE, mCurrentDbType);
        Log.d(TAG, "saveToDb: done");
    }

    private List<Movie> createMoviesListFromCursor(Cursor c) {
        List<Movie> movies = new ArrayList<>();

        while (c.moveToNext()) {
            Movie m = new Movie();
            m.setId(c.getInt(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_MOVIE_ID)));
            m.setTitle(c.getString(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_TITLE)));
            m.setOriginalTitle(c.getString(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_ORIGINAL_TITLE)));
            m.setOverview(c.getString(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_OVERVIEW)));
            m.setVoteAverage(c.getDouble(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_VOTE_AVERAGE)));
            m.setVoteCount(c.getInt(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_VOTE_COUNT)));
            m.setPosterPath(c.getString(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_POSTER)));
            m.setReleaseDate(c.getString(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_RELEASE_DATE)));
            m.setRunTime(c.getInt(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_RUNTIME)));
            m.setIsInFavs(c.getInt(c.getColumnIndex(MoviesDbContract.Movies.COLUMN_IS_IN_FAVS)));
//            Log.d(TAG, "getMovies: m= " + m.toString());
            movies.add(m);
        }
        return movies;
    }

    /* Loader callbacks */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        // query the database using the movie type
        String selection = MoviesDbContract.Movies.COLUMN_TYPE + "=?";
        String[] selectionArgs = {mCurrentDbType};

        return new CursorLoader(getContext(), MoviesDbContract.Movies.CONTENT_URI,
                null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Log.d(TAG, "onLoadFinished: ");
        // create the list
        if (c.moveToNext()) {
            mMovieList = createMoviesListFromCursor(c);
            Log.d(TAG, "mMovieList.size(): " + mMovieList.size());
            mListener.onSuccessLoad(mMovieList);
        } else {
            Log.e(TAG, "no movies found in DB, fetching from api");
            getMoviesFromApi();
        }
    }

    private void getMoviesFromApi() {
        switch (mCurrentDbType) {
            case MoviesDbContract.MovieTypes.NOW_PLAYING:
                call = mApiService.getNowPlayingMovies();
                break;

            case MoviesDbContract.MovieTypes.UPCOMING:
                call = mApiService.getUpcomingMovies();
                break;

            case MoviesDbContract.MovieTypes.TOP_RATED:
                call = mApiService.getTopRatedMovies();
                break;

            case MoviesDbContract.MovieTypes.POPULAR:
                call = mApiService.getPopularMovies();
                break;
        }

//        Log.d(TAG, "getMovies: url: " + call.request().url());
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                Log.d(TAG, "onResponse: ");
                List<Movie> movies = response.body().results;
//                mListener.onSuccessLoad(movies);
                saveToDb(movies);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ERR: " + t.getLocalizedMessage());
                mListener.onFailedLoad(t.getLocalizedMessage());
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }

}
