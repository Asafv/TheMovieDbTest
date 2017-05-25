package com.asafvaron.themoviedbtest.ui.movies;

import android.widget.ImageView;

import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

/**
 * Created by asafvaron on 16/03/2017.
 */

public interface MoviesContract {

    /* View */
    interface View {

        void updateProgress(boolean visibility);

        void updateTitle(String title);

        void onSuccessMoviesLoaded(List<Movie> movies);

        void onFailedLoadMovies(String error);

        void onMovieClicked(ImageView imgView, Movie movie);

        void popInfoIfNeeded();
    }

    /* Model/Presenter communicator */
    interface Presenter {

        void loadMovies(String type);

    }
}
