package com.asafvaron.themoviedbtest.data.source.local;

import com.asafvaron.themoviedbtest.data.source.MoviesDataSource;
import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

import rx.Observable;

import static com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract.MovieTypes.NOW_PLAYING;
import static com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract.MovieTypes.POPULAR;
import static com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract.MovieTypes.TOP_RATED;
import static com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract.MovieTypes.UPCOMING;

/**
 * Created by asafvaron on 03/05/2017.
 */

public class LocalMoviesDataSource implements MoviesDataSource {
    private static LocalMoviesDataSource sInstance;

    public static LocalMoviesDataSource getInstance() {

        if (sInstance == null) {
            sInstance = new LocalMoviesDataSource();
        }
        return sInstance;
    }

    private LocalMoviesDataSource() {
    }

    @Override
    public Observable<List<Movie>> getTopRatedMovies() {
        return null;
    }

    @Override
    public Observable<List<Movie>> getMostPopularMovies() {
        return null;
    }

    @Override
    public Observable<List<Movie>> getUpcomingMovies() {
        return null;
    }

    @Override
    public Observable<List<Movie>> getNowPlayingMovies() {
        return null;
    }

    @Override
    public Observable<List<Movie>> getMoviesByType(String type) {
        switch (type) {
            case NOW_PLAYING:
                return getNowPlayingMovies();

            case UPCOMING:
                return getUpcomingMovies();

            case TOP_RATED:
                return getTopRatedMovies();

            case POPULAR:
                return getMostPopularMovies();

        }
        return Observable.empty();
    }
}
