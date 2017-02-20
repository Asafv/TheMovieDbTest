package com.asafvaron.themoviedbtest.rest;

import com.asafvaron.themoviedbtest.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by asafvaron on 20/02/2017.
 */
public interface ApiInterface {

    @GET("movie/top_rated?api_key=a2ddc78251984ac1b6853aab3885b44c")
    Call<MoviesResponse> getTopRatedMovies();

    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/latest")
    Call<MoviesResponse> getLatestMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(
            @Path("id") int movieId,
            @Query("api_key") String apiKey
    );



}
