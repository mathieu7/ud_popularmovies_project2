package com.miller.popularmovies.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.adapters.MovieAdapter;
import com.miller.popularmovies.http.MovieDBApiCallback;
import com.miller.popularmovies.http.MovieDBAsyncTask;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.MoviePreference;
import com.miller.popularmovies.utils.ApiUtils;
import com.miller.popularmovies.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieDBApiCallback, MovieAdapter.OnMovieClickedListener {
    private static final String POSTER_TRANSITION_KEY = "moviePosterTransition";
    @Override
    public void onMovieClicked(Movie movie, ImageView imageView) {
        openMovieDetails(movie, imageView);
    }

    public static final String MOVIE_INTENT_EXTRA_KEY = "movie";
    private RecyclerView mMovieGridRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private MoviePreference mMoviePreference = MoviePreference.MOST_POPULAR;
    private static final int NUMBER_OF_SPANS = 2;
    private MovieDBAsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieGridRecyclerView = (RecyclerView) findViewById(R.id.movie_grid_recyclerview);
        mLayoutManager = new GridLayoutManager(this, NUMBER_OF_SPANS);
        mMovieGridRecyclerView.setLayoutManager(mLayoutManager);

        mMovieGridRecyclerView.setHasFixedSize(true);
        mMovieGridRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                load(page);
            }
        });

        setupActionBar();
        load();
    }

    private void load(final int page) {
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }
        mTask = new MovieDBAsyncTask(this);

    }

    private void load() {
        mTask = new MovieDBAsyncTask(this);
        mTask.execute(ApiUtils.buildUriString(mMoviePreference, this));
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Open the SettingsActivity when the Settings menu item is selected.
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_display_top_rated:
                mMoviePreference = MoviePreference.TOP_RATED;
                load();
                break;
            case R.id.action_display_popular:
                mMoviePreference = MoviePreference.MOST_POPULAR;
                load();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("currentResults", mMovieResults);
        savedInstanceState.putSerializable("currentPreference", mMoviePreference);
        savedInstanceState.putParcelable("recyclerViewState", mLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("recyclerViewState"));
            mMoviePreference = (MoviePreference) savedInstanceState.getSerializable("currentPreference");
            mMovieResults = savedInstanceState.getParcelableArrayList("currentResults");
        }
    }

    /**
     * Fetch the current network info to deduce if the device has internet connectivity.
     *
     * @return
     */
    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * Callback triggered from a call to the Movie DB Api.
     * Display the movies or update the current adapter.
     *
     * @param result
     */
    @Override
    public void onApiResponse(MovieDBAsyncTask.Result result) {
        if (result.mResponse != null) {
            ArrayList<Movie> results = (ArrayList<Movie>) result.mResponse.getResults();
            if (mMovieAdapter == null) {
                mMovieAdapter = new MovieAdapter(results, this);
                mMovieAdapter.setOnMovieClickedListener(this);
                mMovieGridRecyclerView.setAdapter(mMovieAdapter);
                mMovieResults = results;
            } else {
                mMovieAdapter.addItems(results);
                mMovieResults.addAll(results);
            }
        } else if (result.mException != null) {
            displayErrorState(result.mException.toString());
        }
    }

    private void displayErrorState(final String error) {

    }

    private ArrayList<Movie> mMovieResults;

    /**
     * Open the DetailActivity with the passed Movie object.
     *
     * @param movie
     */
    private void openMovieDetails(final Movie movie, final ImageView posterView) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_INTENT_EXTRA_KEY, movie);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                posterView, POSTER_TRANSITION_KEY);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}
