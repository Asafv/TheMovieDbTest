package com.asafvaron.themoviedbtest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.Utils.Consts;
import com.asafvaron.themoviedbtest.Utils.Prefs;
import com.asafvaron.themoviedbtest.fragments.MovieInfoFragment;
import com.asafvaron.themoviedbtest.mvp_grid.MoviesGridFragment;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.movie_snapping.SnappingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (null == savedInstanceState) {
            showLastSelectedViewFragment(Prefs.getInstance().getInt(Consts.LAST_USED_VIEW, 0));
        }
    }

    private void showLastSelectedViewFragment(int lastView) {
        switch (lastView) {
            case Consts.GRID_FRAG:
                loadMovieGridFragment();
                break;

            case Consts.SNAP_FRAG:
                loadSnappingFragment();
                break;
        }
    }

    public void loadMovieGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.frags_container, MoviesGridFragment.newInstance(), MoviesGridFragment.class.getSimpleName())
                .commit();
    }

    private void loadSnappingFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frags_container, SnappingFragment.newInstance(),
                        SnappingFragment.class.getSimpleName())
                .commit();
    }

    public void showInfoFragment(Movie movie) {
        //XXX Yossi how to stop fragments jump-to-last-position when using replace instead of add
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.frags_container,
                        MovieInfoFragment.newInstance(movie), MovieInfoFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }
}
