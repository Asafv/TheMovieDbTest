package com.asafvaron.themoviedbtest;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.asafvaron.themoviedbtest.activity.MainActivity;
import com.asafvaron.themoviedbtest.model.Movie;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    private Movie movie = new Movie("https://image.tmdb.org/t/p/w500" + "/wnVHDbGz7RvDAHFAsVVT88FxhrP.jpg",
            false, "sheker sheker sheker sheker sheker sheker",
            "2017-02-08", null, 341174, "", "", "Fifty Shades Darker", "", 6.123, 123, false, 6.9);

    private MainActivity mMainActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void init() {
        mMainActivity = mActivityRule.getActivity();
    }

    @Test
    public void gridMovies_DisplayedUi() throws Exception {
        // check if grid fragment was loaded with default
        onView(withText(R.string.action_now_playing)).check(matches(isDisplayed()));
    }

    @Test
    public void infoMovie_InfoDisplayed() {

        // click on one of the items and make sure info fragment is displayed
//        onView(withId(R.id.rv_grid_list)).perform(click());

        mMainActivity.showInfoFragment(movie);

        // check action bar title has changed
        onView(withText(R.string.movie_info_ab_title)).check(matches(isDisplayed()));

        // check movie data is displayed
        onView(withId(R.id.tv_release_date)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_poster)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_overview)).check(matches(isDisplayed()));

    }
}
