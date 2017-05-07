package com.asafvaron.themoviedbtest.ui.movies;

import android.text.TextUtils;
import android.util.Log;

import com.asafvaron.themoviedbtest.Utils.Consts;
import com.asafvaron.themoviedbtest.Utils.Prefs;
import com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract;
import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by asafvaron on 16/03/2017.
 */

class MoviesPresenter implements MoviesContract.Actions, MoviesDataModel.Listener {

    private static final String TAG = "MoviesPresenter";

    private final MoviesContract.View mView;
    private final MoviesDataModel mData;

    MoviesPresenter(MoviesDataModel moviesDataModel, MoviesContract.View view) {
        mData = checkNotNull(moviesDataModel, "MoviesDataModel (Model) must not be null!");
        mData.setListener(this);
        mView = checkNotNull(view, "MoviesDbContract.View (View) must not be null!");
    }

    @Override
    public void loadMovies(String type) {
        Log.e(TAG, "loadMovies: type: " + type);
        mView.updateProgress(true);

        mView.updateTitle(TextUtils.isEmpty(type)
                ? Prefs.getInstance().getString(Consts.LAST_DB_TYPE, MoviesDbContract.MovieTypes.NOW_PLAYING)
                : type);

        mView.popInfoIfNeeded();
        mData.getMovies(type);
    }

    @Override
    public void onSuccessLoad(List<Movie> movies) {
        Log.i(TAG, "onSuccessLoad: ");
        mView.updateProgress(false);
        mView.onSuccessMoviesLoaded(movies);
    }

    @Override
    public void onFailedLoad(String error) {
        mView.updateProgress(false);
        mView.onFailedLoadMovies(error);
    }
}
