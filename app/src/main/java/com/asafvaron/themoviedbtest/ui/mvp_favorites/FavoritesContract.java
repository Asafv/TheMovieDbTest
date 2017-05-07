package com.asafvaron.themoviedbtest.ui.mvp_favorites;

import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

/**
 * Created by asafvaron on 05/03/2017.
 */
interface FavoritesContract {
    interface View {
        void onLoadComplete(List<Movie> favsList);

        void updateProgress(boolean isUpdating);
    }

    interface UserActions {

        void loadFavorites();
    }
}
