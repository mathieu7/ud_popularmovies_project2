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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.adapters.MovieAdapter;
import com.miller.popularmovies.http.MovieDBApiCallback;
import com.miller.popularmovies.http.MovieDBAsyncTask;
import com.miller.popularmovies.models.ApiResponse;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.MoviePreference;
import com.miller.popularmovies.utils.ApiUtils;
import com.miller.popularmovies.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieDBApiCallback, MovieAdapter.OnMovieClickedListener {
    public static final String MOVIE_INTENT_EXTRA_KEY = "movie";
    private static final String POSTER_TRANSITION_KEY = "moviePosterTransition";
    private static final int NUMBER_OF_SPANS = 2;
    private RecyclerView mMovieGridRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private ApiResponse mPreviousApiResponse;
    private MoviePreference mMoviePreference = MoviePreference.MOST_POPULAR;
    private MovieDBAsyncTask mTask;
    private View mLoadingDialog;
    private ProgressBar mProgressBar;
    private ArrayList<Movie> mMovieResults;

    @Override
    public void onMovieClicked(Movie movie, ImageView imageView) {
        openMovieDetails(movie, imageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        if (savedInstanceState == null) load();
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

    private void loadMore() {
        if (mPreviousApiResponse == null) return;
        final int pageToLoad = mPreviousApiResponse.getPage() + 1;
        if (pageToLoad > mPreviousApiResponse.getTotalPages()) return;
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }
        mTask = new MovieDBAsyncTask(this);
        mTask.execute(ApiUtils.buildUriString(mMoviePreference, this, pageToLoad));
    }

    private void load() {
        if (mMovieResults != null) {
            mMovieResults.clear();
            mMovieResults = null;
        }
        if (mMovieAdapter != null) {
            mMovieAdapter = null;
        }
        displayLoadingDialog(true);
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
        savedInstanceState.putParcelable("previousApiResponse", mPreviousApiResponse);
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mMoviePreference = (MoviePreference) savedInstanceState.getSerializable("currentPreference");
            mMovieResults = savedInstanceState.getParcelableArrayList("currentResults");
            mPreviousApiResponse = savedInstanceState.getParcelable("previousApiResponse");

            mMovieAdapter = new MovieAdapter(mMovieResults, this);
            mMovieGridRecyclerView.setAdapter(mMovieAdapter);
            mMovieAdapter.setOnMovieClickedListener(this);

            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("recyclerViewState"));
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
            hideLoadingDialog();
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
            mPreviousApiResponse = result.mResponse;
        } else if (result.mException != null) {
            displayErrorState(result.mException.toString());
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
