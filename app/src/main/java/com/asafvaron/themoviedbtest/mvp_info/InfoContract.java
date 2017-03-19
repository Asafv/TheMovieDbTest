package com.asafvaron.themoviedbtest.mvp_info;

import android.content.Context;

import com.asafvaron.themoviedbtest.model.Movie;

/**
 * Created by asafvaron on 19/03/2017.
 */

interface InfoContract {
    interface View {
        void setMovieRunTime(int runTime);

        void failedToGetMovieRunTime(String err);

    }

    interface Actions {

        void getMovieRunTime();

        void updateDb(Context context, Movie movie);

    }
}
