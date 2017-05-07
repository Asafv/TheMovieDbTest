package com.asafvaron.themoviedbtest;

import com.asafvaron.themoviedbtest.data.api.MoviesApi;
import com.asafvaron.themoviedbtest.data.api.MoviesService;
import com.asafvaron.themoviedbtest.ui.movies.MoviesFragment;
import com.asafvaron.themoviedbtest.ui.movie_details.MovieDetailFragment;
import com.asafvaron.themoviedbtest.model.Movie;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public ExampleUnitTest() {
        super();
    }


    @Mock
    private static List<Movie> MOVIE_LIST = Lists.newArrayList(
            new Movie("https://image.tmdb.org/t/p/w500" + "/wnVHDbGz7RvDAHFAsVVT88FxhrP.jpg",
                    false, "sheker sheker sheker sheker sheker sheker",
                    "2017-02-08", null, 341174, "", "", "Fifty Shades Darker", "", 6.123, 123, false, 6.9),
            new Movie("https://image.tmdb.org/t/p/w500" + "/bbxtz5V0vvnTDA2qWbiiRC77Ok9.jpg",
                    false, "sheker sheker sheker sheker sheker sheker",
                    "2017-02-08", null, 14564, "", "", "Rings", "", 6.123, 123, false, 6.9)
    );

    @Mock
    private static List<Movie> EMPTY_MOVIE_LIST = new ArrayList<>(0);

    private MoviesService mMoviesService;

    @Mock
    private MovieDetailFragment mMovieDetailFragment;

    @Mock
    private MoviesFragment mMoviesFragment;

    @Before
    public void setupViews() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mMoviesFragment = new MoviesFragment();
        mMoviesService = MoviesApi.getInstance().getMoviesService();
    }

    @Test
    public void MovieGrid_loadMovies() {
//        verify(mMoviesService).getNowPlayingMovies(MoviesApi.API_KEY);
//        verify(mMoviesService).getTopRatedMovies(MoviesApi.API_KEY);
//        verify(mMoviesService).getPopularMovies(MoviesApi.API_KEY);
//        verify(mMoviesService).getUpcomingMovies(MoviesApi.API_KEY);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}