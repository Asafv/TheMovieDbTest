package com.asafvaron.themoviedbtest.mvp_grid;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.asafvaron.themoviedbtest.Utils.Consts;
import com.asafvaron.themoviedbtest.Utils.Prefs;
import com.asafvaron.themoviedbtest.database.movies.MoviesContract;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.model.MoviesResponse;
import com.asafvaron.themoviedbtest.rest.ApiClient;
import com.asafvaron.themoviedbtest.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.asafvaron.themoviedbtest.MyApp.getContext;

/**
 * Created by asafvaron on 16/03/2017.
 */

class GridData implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = GridData.class.getSimpleName();

    private static final int LOADER_ID = 1;

    private final LoaderManager mLoaderManager;
    private List<Movie> mMovieList = new ArrayList<>();
    private ApiInterface mApiService;
    private String mCurrentDbType;
    private Call<MoviesResponse> call;
    private Listener mListener;

    void setListener(GridPresenter gridPresenter) {
        mListener = (Listener) gridPresenter;
    }

    interface Listener {
        void onSuccessLoad(List<Movie> movies);

        void onFailedLoad(String error);
    }

    GridData(LoaderManager loaderManager) {
        this.mLoaderManager = loaderManager;
        init();
    }

    // init and load some data
    private void init() {
        mApiService = ApiClient.getClient().create(ApiInterface.class);
        mCurrentDbType = Prefs.getInstance().getString("last_db_type", MoviesContract.MovieTypes.NOW_PLAYING);

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
        mCurrentDbType = (type == null) ? MoviesContract.MovieTypes.NOW_PLAYING : type;

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
                    .insert(MoviesContract.Movies.CONTENT_URI, m.getValues(mCurrentDbType));
//                            ,MoviesContract.Movies.COLUMN_MOVIE_ID + "=" + m.getId(),
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
            m.setId(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_MOVIE_ID)));
            m.setTitle(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_TITLE)));
            m.setOriginalTitle(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_ORIGINAL_TITLE)));
            m.setOverview(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_OVERVIEW)));
            m.setVoteAverage(c.getDouble(c.getColumnIndex(MoviesContract.Movies.COLUMN_VOTE_AVERAGE)));
            m.setVoteCount(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_VOTE_COUNT)));
            m.setPosterPath(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_POSTER)));
            m.setReleaseDate(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_RELEASE_DATE)));
            m.setRunTime(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_RUNTIME)));
            m.setIsInFavs(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_IS_IN_FAVS)));
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
        String selection = MoviesContract.Movies.COLUMN_TYPE + "=?";
        String[] selectionArgs = {mCurrentDbType};

        return new CursorLoader(getContext(), MoviesContract.Movies.CONTENT_URI,
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
            case MoviesContract.MovieTypes.NOW_PLAYING:
                call = mApiService.getNowPlayingMovies(ApiClient.API_KEY);
                break;

            case MoviesContract.MovieTypes.UPCOMING:
                call = mApiService.getUpcomingMovies(ApiClient.API_KEY);
                break;

            case MoviesContract.MovieTypes.TOP_RATED:
                call = mApiService.getTopRatedMovies(ApiClient.API_KEY);
                break;

            case MoviesContract.MovieTypes.POPULAR:
                call = mApiService.getPopularMovies(ApiClient.API_KEY);
                break;
        }

//        Log.d(TAG, "getMovies: url: " + call.request().url());
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                Log.d(TAG, "onResponse: ");
                List<Movie> movies = response.body().getResults();
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
