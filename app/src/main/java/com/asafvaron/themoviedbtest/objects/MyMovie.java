package com.asafvaron.themoviedbtest.objects;

/**
 * Created by asafvaron on 19/02/2017.
 */
public class MyMovie {
    private static final String TAG = MyMovie.class.getSimpleName();

    private int id;
    private String title;
    private String original_title;
    private String release_date;
    private String overview;
    private String poster_path;
    private float vote_average;
    private int vote_count;
    private int runtime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return "\n" + getTitle()
                + "\n" + getOverview()
                + "\n" + getRelease_date()
                + "\n" + getPoster_path()
                + "\n" + getRuntime()
                + "\n" + getVote_average()
                + "\n" + getVote_count();
    }
}
