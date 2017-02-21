package com.asafvaron.themoviedbtest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.fragments.MovieInfoFragment;
import com.asafvaron.themoviedbtest.fragments.MoviesFragment;
import com.asafvaron.themoviedbtest.model.Movie;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.frags_container,
                MoviesFragment.newInstance(), MoviesFragment.class.getSimpleName()).commit();
    }

    public void showInfoFragment(Movie movie) {
        MovieInfoFragment movieInfoFragment = MovieInfoFragment.newInstance(movie);
        getSupportFragmentManager().beginTransaction().replace(R.id.frags_container,
                movieInfoFragment, MovieInfoFragment.class.getSimpleName()).commit();

    }
}