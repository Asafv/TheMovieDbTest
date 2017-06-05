package com.asafvaron.themoviedbtest.data.io;

import com.asafvaron.themoviedbtest.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by asafvaron on 20/02/2017.
 */
public class MoviesResponse {

    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public List<Movie> results;

    @SerializedName("total_results")
    public int totalResults;

    @SerializedName("total_pages")
    public int totalPages;
}
