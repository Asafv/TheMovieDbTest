package com.asafvaron.themoviedbtest.ui.movies

import android.widget.ImageView

import com.asafvaron.themoviedbtest.model.Movie

/**
 * Created by asafvaron on 16/03/2017.
 */

interface MoviesContract {

    /* View */
    interface View {

        fun updateProgress(visibility: Boolean)

        fun updateTitle(title: String?)

        fun onSuccessMoviesLoaded(movies: List<Movie>?)

        fun onFailedLoadMovies(error: String?)

        fun onMovieClicked(imgView: ImageView?, movie: Movie?)

        fun popInfoIfNeeded()
    }

    /* Model/Presenter communicator */
    interface Presenter {

        fun loadMovies(type: String)
    }
}
