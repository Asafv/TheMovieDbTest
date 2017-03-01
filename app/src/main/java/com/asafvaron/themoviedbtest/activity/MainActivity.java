package com.asafvaron.themoviedbtest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.fragments.MovieInfoFragment;
import com.asafvaron.themoviedbtest.fragments.MoviesGridFragment;
import com.asafvaron.themoviedbtest.model.Movie;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        if (null == savedInstanceState) {
            showMovieGridFragment();
        }
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void showMovieGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.frags_container, MoviesGridFragment.newInstance(), MoviesGridFragment.class.getSimpleName())
                .commit();
    }

    public void showInfoFragment(Movie movie) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.frags_container,
                        MovieInfoFragment.newInstance(movie), MovieInfoFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }
}
