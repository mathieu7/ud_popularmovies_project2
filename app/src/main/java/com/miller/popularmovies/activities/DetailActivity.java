package com.miller.popularmovies.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.utils.ImageUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private Movie mMovie;
    private Toolbar mToolbar;
    private ImageView mPosterView;
    private RatingBar mRating;
    private TextView mTitleView, mReleaseView, mSummaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(MainActivity.MOVIE_INTENT_EXTRA_KEY)) {
            mMovie = getIntent().getParcelableExtra(MainActivity.MOVIE_INTENT_EXTRA_KEY);
        } else {
            finish();
        }
        setupActionBar();
        setupMovieDetails();
    }

    private void setupMovieDetails() {
        mPosterView = (ImageView) findViewById(R.id.image_view_movie_poster);
        mTitleView = (TextView) findViewById(R.id.text_view_movie_header);
        mReleaseView = (TextView) findViewById(R.id.text_view_movie_release);
        mRating = (RatingBar) findViewById(R.id.rating_bar_movie);
        mSummaryView = (TextView) findViewById(R.id.text_view_movie_summary);

        ImageUtils.setMoviePoster(mMovie.getPosterPath(), this, mPosterView);
        mTitleView.setText(mMovie.getTitle());
        mReleaseView.setText(mMovie.getReleaseDate());
        mSummaryView.setText(mMovie.getOverview());
        mRating.setRating(mMovie.getVoteAverage().floatValue() / 2);

        mToolbar.setTitle(mMovie.getTitle());
    }

    private void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
