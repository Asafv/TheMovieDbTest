package com.asafvaron.themoviedbtest.model;

import android.content.ContentValues;

import com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafvaron on 19/02/2017.
 */
public class Movie implements Serializable{

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();

    @SerializedName("id")
    private Integer id;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("title")
    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("video")
    private Boolean video;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("runtime")
    private int runTime;

    // 0 (false) , 1 (true)
    private int isInFavs;

    public Movie(String posterPath, boolean adult, String overview, String releaseDate,
                 List<Integer> genreIds, Integer id, String originalTitle, String originalLanguage,
                 String title, String backdropPath, Double popularity, Integer voteCount,
                 Boolean video, Double voteAverage) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.runTime = 0;
    }

    public Movie() {
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getIsInFavs() {
        return isInFavs;
    }

    public void setIsInFavs(int isInFavs) {
        this.isInFavs = isInFavs;
    }

    public ContentValues getValues(String dbType) {
        ContentValues values = new ContentValues();
        values.put(MoviesDbContract.Movies.COLUMN_MOVIE_ID, getId());
        values.put(MoviesDbContract.Movies.COLUMN_TITLE, getTitle());
        values.put(MoviesDbContract.Movies.COLUMN_OVERVIEW, getOverview());
        values.put(MoviesDbContract.Movies.COLUMN_RELEASE_DATE, getReleaseDate());
        values.put(MoviesDbContract.Movies.COLUMN_POSTER, getPosterPath());
        values.put(MoviesDbContract.Movies.COLUMN_VOTE_AVERAGE, getVoteAverage());
        values.put(MoviesDbContract.Movies.COLUMN_VOTE_COUNT, getVoteCount());
        values.put(MoviesDbContract.Movies.COLUMN_RUNTIME, getRunTime());
        values.put(MoviesDbContract.Movies.COLUMN_IS_IN_FAVS, getIsInFavs());
        values.put(MoviesDbContract.Movies.COLUMN_TYPE, dbType);
        return values;
    }
}
