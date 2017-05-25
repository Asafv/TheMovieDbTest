package com.asafvaron.themoviedbtest.ui.mvp_snapping;

import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

/**
 * Created by asafvaron on 05/03/2017.
 */
interface SnappingContract {

    interface View {
        void onNowPlayingLoaded(List<Movie> movies);

        void onTopRatedLoaded(List<Movie> movies);

        void onUpcomingLoaded(List<Movie> movies);

        void onPopularLoaded(List<Movie> movies);
    }

    interface Presenter {
        void loadData(boolean forceLoad);
    }
}
