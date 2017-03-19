package com.asafvaron.themoviedbtest.mvp_grid;

import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

/**
 * Created by asafvaron on 16/03/2017.
 */

public interface GridContract {

    /* View */
    interface View {

        void updateProgress(boolean visibility);

        void updateTitle(String title);

        void onSuccessMoviesLoaded(List<Movie> movies);

        void closeInfoFragIfOpen();

        void onFailedLoadMovies(String error);

        void onMovieClicked(Movie movie);
    }

    /* Model/Presenter communicator */
    interface Actions {

        void loadMovies(String type);

    }
}
