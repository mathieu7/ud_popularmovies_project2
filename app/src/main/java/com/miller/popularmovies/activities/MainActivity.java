package com.miller.popularmovies.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.adapters.MovieAdapter;
import com.miller.popularmovies.loader.MovieDBApiLoader;
import com.miller.popularmovies.models.MovieList;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.MoviePreference;
import com.miller.popularmovies.utils.EndlessRecyclerViewScrollListener;
import com.miller.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.miller.popularmovies.api.MovieDBApiClient.*;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Result<MovieList>>, MovieAdapter.OnMovieClickedListener {
    public static final String MOVIE_INTENT_EXTRA_KEY = "movie";
    private static final String POSTER_TRANSITION_KEY = "moviePosterTransition";
    private static final int NUMBER_OF_SPANS = 2;
    private RecyclerView mMovieGridRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private MovieList mPreviousMovieList;
    private MoviePreference mMoviePreference = MoviePreference.MOST_POPULAR;
    private View mLoadingDialog;
    private ProgressBar mProgressBar;
    private boolean isNetworkConnected;
    private boolean mIsContentLoaded;

    @Override
    public Loader<Result<MovieList>> onCreateLoader(int id, Bundle args) {
        PagedRequest<MovieList> request = (mMoviePreference == MoviePreference.TOP_RATED
                ? new TopRatedRequest(this) : new PopularRequest(this));
        return new MovieDBApiLoader<>(request, this);
    }

    @Override
    public void onLoadFinished(Loader<Result<MovieList>> loader, Result<MovieList> data) {
        if (data.mResponse != null) {
            ArrayList<Movie> movies = (ArrayList<Movie>) data.mResponse.getResults();
            mIsContentLoaded = true;
            hideLoadingDialog();
            if (mMovieAdapter == null) {
                mMovieAdapter = new MovieAdapter(movies, this);
                mMovieGridRecyclerView.setAdapter(mMovieAdapter);
            } else {
                mMovieAdapter.addItems(movies);
            }
        } else if (data.mException != null) {
            displayErrorState(data.mException.getMessage());
        }
    }

    @Override
    public void onLoaderReset(Loader<Result<MovieList>> loader) {
        mMovieAdapter.clear();
        displayLoadingDialog(true);
    }

    /**
     * Broadcast Receiver to track network connectivity changes.
     */
    private BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(CONNECTIVITY_ACTION)) {
                boolean isConnected = NetworkUtils.isConnected(MainActivity.this);
                // Trigger the initial load again if the device regains connectivity.
                if (isNetworkConnected != isConnected && isConnected) {
                    isNetworkConnected = true;
                    if (!mIsContentLoaded) {
                        load();
                    }
                }
            }
        }
    };

    @Override
    public void onMovieClicked(Movie movie, ImageView imageView) {
        openMovieDetails(movie, imageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        if (savedInstanceState == null) {
            load();
        }
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(mConnectivityReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    private void initLayout() {
        mMovieGridRecyclerView = (RecyclerView) findViewById(R.id.movie_grid_recyclerview);
        mLoadingDialog = findViewById(R.id.loading_dialog_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_dialog_progress_bar);
        mLayoutManager = new GridLayoutManager(this, NUMBER_OF_SPANS);
        mMovieGridRecyclerView.setLayoutManager(mLayoutManager);
        mMovieGridRecyclerView.setHasFixedSize(true);
        mMovieGridRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMore();
            }
        });
        setupActionBar();
    }

    /**
     * Cancel the running download task if the app goes to the background.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mConnectivityReceiver != null) {
            try {
                unregisterReceiver(mConnectivityReceiver);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Load more content from the Movie DB API. This method is triggered as the user scrolls to the bottom
     * of the recyclerview.
     */
    private void loadMore() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    private void load() {
        if (mMovieAdapter != null) {
            mMovieAdapter.clear();
            mMovieAdapter = null;
        }
        displayLoadingDialog(true);
        if (!NetworkUtils.isConnected(this)) {
            displayNoNetworkState();
        } else {
            if (getSupportLoaderManager().getLoader(0) != null) {
                getSupportLoaderManager().restartLoader(0, null, this);
            } else {
                getSupportLoaderManager().initLoader(0, null, this);
            }
        }
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
        if (mMovieAdapter != null && mMovieAdapter.getMovies() != null) {
            savedInstanceState.putParcelableArrayList("currentMovies", mMovieAdapter.getMovies());
        }
        savedInstanceState.putBoolean("contentLoaded", mIsContentLoaded);
        savedInstanceState.putSerializable("currentPreference", mMoviePreference);
        savedInstanceState.putParcelable("recyclerViewState", mLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mMoviePreference = (MoviePreference) savedInstanceState.getSerializable("currentPreference");
            mIsContentLoaded = savedInstanceState.getBoolean("contentLoaded");
            ArrayList<Movie> savedMovieList = savedInstanceState.getParcelableArrayList("currentMovies");

            mMovieAdapter = new MovieAdapter(savedMovieList, this);
            mMovieGridRecyclerView.setAdapter(mMovieAdapter);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("recyclerViewState"));
        }
    }


    private void displayErrorState(final String error) {
        TextView errorView = (TextView) findViewById(R.id.error_text_view);
        errorView.setText(error);
        displayLoadingDialog(false);
    }

    private void displayLoadingDialog(final boolean showProgress) {
        mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        if (mLoadingDialog.getVisibility() != View.VISIBLE) {
            mLoadingDialog.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingDialog() {
        mLoadingDialog.setVisibility(View.GONE);
    }

    /**
     * Prompt user with SnackBar to enable network connectivity.
     */
    private void displayNoNetworkState() {
        Snackbar snackbar = Snackbar.make(mLoadingDialog,
                getString(R.string.snackbar_no_network), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.snackbar_action_enable), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        snackbar.show();
    }

    /**
     * Open the DetailActivity with the passed Movie object.
     *
     * @param movie
     */
    private void openMovieDetails(final Movie movie, final ImageView posterView) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_INTENT_EXTRA_KEY, movie);
        ViewCompat.setTransitionName(posterView, POSTER_TRANSITION_KEY);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                posterView, POSTER_TRANSITION_KEY);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}
