package com.asafvaron.themoviedbtest.movie_snapping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.asafvaron.themoviedbtest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asafvaron on 01/03/2017.
 */
public class MovieSnapActivity extends AppCompatActivity {
    private static final String TAG = MovieSnapActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_snap_activity);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (null == savedInstanceState) {
            loadSnappingFragment();
        }
    }

    private void loadSnappingFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frags_container, SnappingFragment.newInstance(),
                        SnappingFragment.class.getSimpleName())
                .commit();
    }
}
