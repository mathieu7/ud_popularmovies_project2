package com.miller.popularmovies.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miller.popularmovies.R;
import com.miller.popularmovies.adapters.MovieAdapter;
import com.miller.popularmovies.http.MovieDBApiCallback;
import com.miller.popularmovies.http.MovieDBAsyncTask;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.MoviePreference;
import com.miller.popularmovies.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieDBApiCallback {

    private RecyclerView mMovieGridRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private static final int NUMBER_OF_SPANS = 2;

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

            }
        });

        setupActionBar();
        MovieDBAsyncTask task = new MovieDBAsyncTask(this);
        task.execute(MoviePreference.MOST_POPULAR);
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
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_REQ_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int SETTINGS_REQ_CODE = 0x40;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Fetch the current network info to deduce if the device has internet connectivity.
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
     * @param result
     */
    @Override
    public void onApiResponse(MovieDBAsyncTask.Result result) {
        if (result.mResponse != null) {
            ArrayList<Movie> movies = (ArrayList<Movie>) result.mResponse.getResults();
            if (mMovieAdapter == null) {
                mMovieAdapter = new MovieAdapter(movies, this);
                mMovieGridRecyclerView.setAdapter(mMovieAdapter);
            } else {
                mMovieAdapter.addItems(movies);
            }
        } else if (result.mException != null) {
            //TODO: display error message to the user.
        }
    }
}
