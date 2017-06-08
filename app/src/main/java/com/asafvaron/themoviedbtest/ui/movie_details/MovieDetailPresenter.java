package com.asafvaron.themoviedbtest.ui.movie_details;

import android.content.Context;
import android.util.Log;

import com.asafvaron.themoviedbtest.model.Movie;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by asafvaron on 19/03/2017.
 */

class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private static final String TAG = "MovieDetailPresenter";

    private final MovieDetailContract.View mView;
    private final MovieDetailModel mData;

    MovieDetailPresenter(MovieDetailContract.View view, MovieDetailModel movieDetailModel) {
        mView = checkNotNull(view, "View cannot be null!");
        mData = checkNotNull(movieDetailModel, "Model cannot be null!!");
        // XXX why needed??
        mView.setPresenter(this);
    }

    @Override
    public void getMovieRunTime() {
        mData.getRunTime(new MovieDetailModel.InfoModelListener.RunTimeCallback() {

            @Override
            public void onSuccess(int runTime) {
                mView.setMovieRunTime(runTime);
            }

            @Override
            public void onFailed(String err) {
                mView.failedToGetMovieRunTime(err);
            }
        });
    }

    @Override
    public void updateDb(Context context, Movie movie) {
        mData.updateMovieToDb(context, movie, new MovieDetailModel.InfoModelListener.UpdateDbCallback() {

            @Override
            public void onUpdated() {
                Log.i(TAG, "onUpdated: ");
            }

            @Override
            public void onFailed(String err) {
                Log.e(TAG, "onFailed: " + err);
            }
        });
    }

}
