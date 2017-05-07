package com.asafvaron.themoviedbtest.ui.mvp_info;

import android.content.Context;
import android.util.Log;

import com.asafvaron.themoviedbtest.model.Movie;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by asafvaron on 19/03/2017.
 */

class InfoPresenter implements InfoContract.Actions {

    private static final String TAG = "InfoPresenter";

    private final InfoContract.View mView;
    private final InfoModel mData;

    InfoPresenter(InfoContract.View view, InfoModel infoModel) {
        mView = checkNotNull(view, "View cannot be null!");
        mData = checkNotNull(infoModel, "Model cannot be null!!");
    }

    @Override
    public void getMovieRunTime() {
        mData.getRunTime(new InfoModel.InfoModelListener.RunTimeCallback() {

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
        mData.updateMovieToDb(context, movie, new InfoModel.InfoModelListener.UpdateDbCallback() {

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
