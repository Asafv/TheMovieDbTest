package com.asafvaron.themoviedbtest.ui.movie_details

import android.content.Context

import com.asafvaron.themoviedbtest.model.Movie

/**
 * Created by asafvaron on 19/03/2017.
 */

internal interface MovieDetailContract {
    interface View {
        fun setMovieRunTime(runTime: Int)

        fun failedToGetMovieRunTime(err: String)
    }

    interface Presenter {
        fun getMovieRunTime()

        fun updateDb(context: Context, movie: Movie)
    }
}
