package com.asafvaron.themoviedbtest.data.api;

import com.asafvaron.themoviedbtest.data.io.MoviesResponse;
import com.asafvaron.themoviedbtest.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by asafvaron on 20/02/2017.
 */
public interface MoviesService {

    String ENDPOINT = "https://api.themoviedb.org/3/";

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies();

    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlayingMovies();

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies();

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies();

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int movieId);
}
