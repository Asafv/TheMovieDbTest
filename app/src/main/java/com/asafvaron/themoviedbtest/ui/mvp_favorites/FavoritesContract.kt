package com.asafvaron.themoviedbtest.ui.mvp_favorites

import com.asafvaron.themoviedbtest.model.Movie

/**
 * Created by asafvaron on 05/03/2017.
 */
internal interface FavoritesContract {
    interface View {
        fun onLoadComplete(favsList: List<Movie>? = null)

        fun updateProgress(isUpdating: Boolean)
    }

    interface Presenter {

        fun loadFavorites()
    }
}
