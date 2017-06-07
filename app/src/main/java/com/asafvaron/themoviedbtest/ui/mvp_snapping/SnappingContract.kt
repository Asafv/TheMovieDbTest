package com.asafvaron.themoviedbtest.ui.mvp_snapping

import com.asafvaron.themoviedbtest.model.Movie

/**
 * Created by asafvaron on 05/03/2017.
 */
internal interface SnappingContract {

    interface View {
        fun onNowPlayingLoaded(movies: List<Movie>?)

        fun onTopRatedLoaded(movies: List<Movie>?)

        fun onUpcomingLoaded(movies: List<Movie>?)

        fun onPopularLoaded(movies: List<Movie>?)
    }

    interface Presenter {
        fun loadData(forceLoad: Boolean)
    }
}
