package com.asafvaron.themoviedbtest.data.source;

import com.asafvaron.themoviedbtest.data.source.local.LocalMoviesDataSource;
import com.asafvaron.themoviedbtest.data.source.remote.RemoteMoviesDataSource;
import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

import rx.Observable;

/**
 * Created by asafvaron on 03/05/2017.
 */

public class MoviesRepository implements MoviesDataSource {

    private static final String TAG = "MoviesRepository";

    private static MoviesRepository sInstance;
    private final LocalMoviesDataSource localDataSource;
    private final RemoteMoviesDataSource remoteDataSource;

    public static MoviesRepository getsInstance() {
        if (sInstance == null) {
            sInstance = new MoviesRepository();
        }
        return sInstance;
    }

    private MoviesRepository() {
        localDataSource = LocalMoviesDataSource.getInstance();
        remoteDataSource = RemoteMoviesDataSource.getInstance();
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
        return Observable.empty();
    }
}
