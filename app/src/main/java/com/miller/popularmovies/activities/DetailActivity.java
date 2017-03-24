package com.miller.popularmovies.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.Video;
import com.miller.popularmovies.providers.MovieFavoritesContract;
import com.miller.popularmovies.utils.VideoUtils;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, DetailFragment.Listener {
    private Movie  mMovie;
    private String mMovieTitle;
    private FloatingActionButton mFavoriteFAB;
    private boolean mIsFavorite = false;

    @Override
    public void onClick(View v) {
        if (v == mFavoriteFAB) {
           toggleFavorite();
        }
    }

    /**
     * Do a query to the MovieFavoriteContentProvider to see if the current movie is already a favorite.
     */
    private void loadFavorite() {
        String selectionClause = MovieFavoritesContract.MovieFavoriteEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(mMovie.getId()) };
        Cursor cursor = getContentResolver().query(MovieFavoritesContract.MovieFavoriteEntry.CONTENT_URI,
                null, selectionClause, selectionArgs, null);
        if (cursor == null) return;
        if (cursor.getCount() > 0) {
            mIsFavorite = true;
            mFavoriteFAB.setImageResource(R.drawable.ic_favorite_filled);
            cursor.close();
        }
    }

    /**
     * Method to add or remove a movie from favorites.
     */
    private void toggleFavorite() {
        if (mIsFavorite) {
            Uri uri = MovieFavoritesContract.MovieFavoriteEntry.getResourceUri(mMovie);
            int deleted = getContentResolver().delete(uri, null, null);
            if (deleted != 0) {
                Snackbar.make(mFavoriteFAB,
                        getString(R.string.snackbar_not_favorited, mMovie.getTitle()),
                        Snackbar.LENGTH_SHORT).show();
                mFavoriteFAB.setImageResource(R.drawable.ic_favorite_hollow);
                mIsFavorite = false;
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(MovieFavoritesContract.MovieFavoriteEntry.COLUMN_MOVIE_ID, mMovie.getId());
            values.put(MovieFavoritesContract.MovieFavoriteEntry.COLUMN_MOVIE_TITLE, mMovie.getTitle());
            Uri insertedUri = getContentResolver().insert(MovieFavoritesContract.MovieFavoriteEntry.CONTENT_URI,
                    values);
            if (insertedUri != null) {
                Snackbar.make(mFavoriteFAB, getString(R.string.snackbar_favorited, mMovie.getTitle()), Snackbar.LENGTH_SHORT).show();
                mFavoriteFAB.setImageResource(R.drawable.ic_favorite_filled);
                mIsFavorite = true;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(MainActivity.MOVIE_INTENT_EXTRA_KEY)) {
            mMovie = getIntent().getParcelableExtra(MainActivity.MOVIE_INTENT_EXTRA_KEY);
        }
        if (mMovie == null) {
            finish();
            return;
        }
        setupLayout();
        loadFavorite();
    }

    private void setupLayout() {
        mMovieTitle = mMovie.getTitle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DetailFragment fragment = DetailFragment.newInstance(mMovie);
        fragmentTransaction.add(R.id.detail_container, fragment).commit();
        setupActionBar();
        mFavoriteFAB = (FloatingActionButton) findViewById(R.id.favorite_movie_fab);
        mFavoriteFAB.setOnClickListener(this);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle(mMovieTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerClicked(Video video) {
        VideoUtils.openYouTubeTrailer(video, this);
    }
}
