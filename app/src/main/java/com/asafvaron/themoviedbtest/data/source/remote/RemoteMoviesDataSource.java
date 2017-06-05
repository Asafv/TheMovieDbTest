package com.asafvaron.themoviedbtest.data.source.remote;

import com.asafvaron.themoviedbtest.data.source.MoviesDataSource;
import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

import rx.Observable;

/**
 * Created by asafvaron on 03/05/2017.
 */

public class RemoteMoviesDataSource implements MoviesDataSource {
    private static RemoteMoviesDataSource sInstance;


    public static RemoteMoviesDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new RemoteMoviesDataSource();
        }
        return sInstance;

    }

    private RemoteMoviesDataSource() {

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
        return null;
    }
}
