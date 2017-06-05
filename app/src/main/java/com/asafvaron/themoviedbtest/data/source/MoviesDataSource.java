package com.asafvaron.themoviedbtest.data.source;

import com.asafvaron.themoviedbtest.model.Movie;

import java.util.List;

import rx.Observable;

/**
 * Created by asafvaron on 03/05/2017.
 */

public interface MoviesDataSource {

    Observable<List<Movie>> getTopRatedMovies();

    Observable<List<Movie>> getMostPopularMovies();

    Observable<List<Movie>> getUpcomingMovies();

    Observable<List<Movie>> getNowPlayingMovies();

    Observable<List<Movie>> getMoviesByType(String type);
}
