package com.asafvaron.themoviedbtest.mvp_grid;

import android.text.TextUtils;
import android.util.Log;

import com.asafvaron.themoviedbtest.Utils.Consts;
import com.asafvaron.themoviedbtest.Utils.Prefs;
import com.asafvaron.themoviedbtest.database.MoviesContract;
import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by asafvaron on 16/03/2017.
 */

class GridPresenter implements GridContract.Actions {

    private static final String TAG = "GridPresenter";

    private final GridContract.View mView;
    private final GridData mData;

    GridPresenter(GridData gridData, GridContract.View view) {
        mData = checkNotNull(gridData, "GridData (Model) must not be null!");
        mView = checkNotNull(view, "GridContract.View (View) must not be null!");
    }

    @Override
    public void loadMovies(String type) {
        mView.updateProgress(true);

        mView.updateTitle(TextUtils.isEmpty(type)
                ? Prefs.getInstance().getString(Consts.LAST_DB_TYPE, MoviesContract.MovieTypes.NOW_PLAYING)
                : type);

        mData.getMovies(type, new GridData.Listener() {

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
        });
    }
}
