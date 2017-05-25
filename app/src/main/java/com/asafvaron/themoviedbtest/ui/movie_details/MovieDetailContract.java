package com.asafvaron.themoviedbtest.ui.movie_details;

import android.content.Context;

import com.asafvaron.themoviedbtest.model.Movie;

/**
 * Created by asafvaron on 19/03/2017.
 */

interface MovieDetailContract {
    interface View {
        void setMovieRunTime(int runTime);

        void failedToGetMovieRunTime(String err);
    }

    interface Presenter {
        void getMovieRunTime();

        void updateDb(Context context, Movie movie);
    }
}
