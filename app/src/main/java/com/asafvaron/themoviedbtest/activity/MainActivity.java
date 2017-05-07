package com.asafvaron.themoviedbtest.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.widget.ImageView;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.Utils.Consts;
import com.asafvaron.themoviedbtest.Utils.Prefs;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.ui.movie_details.MovieDetailFragment;
import com.asafvaron.themoviedbtest.ui.movie_snapping.SnappingFragment;
import com.asafvaron.themoviedbtest.ui.movies.MoviesFragment;

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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        MoviesFragment moviesFragment = MoviesFragment.newInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Setup exit transition on first fragment
            moviesFragment.setExitTransition(null);
            moviesFragment.setReenterTransition(null);
        } else {
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.replace(R.id.frags_container, moviesFragment, MoviesFragment.class.getSimpleName())
                .commit();
    }

    private void loadSnappingFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frags_container, SnappingFragment.newInstance(),
                        SnappingFragment.class.getSimpleName())
                .commit();
    }

    public void showInfoFragment(ImageView imgView, Movie movie) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movie);

        // Note that we need the API version check here because the actual transition classes (e.g. Fade)
        // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
        // ARE available in the support library (though they don't do anything on API < 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // setup enter transition on info fragment
            movieDetailFragment.setSharedElementEnterTransition(new ChangeImageTransform());
            movieDetailFragment.setSharedElementReturnTransition(new ChangeImageTransform());
            movieDetailFragment.setEnterTransition(new ChangeImageTransform());

            // setup return transition
//            movieDetailFragment.setReturnTransition(new ChangeBounds());

            ft.addSharedElement(imgView, "movieClickTransition");
        } else {
            // set default animation for lower api
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        ft.replace(R.id.frags_container, movieDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class DetailsTransition extends TransitionSet {
        DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }
}
