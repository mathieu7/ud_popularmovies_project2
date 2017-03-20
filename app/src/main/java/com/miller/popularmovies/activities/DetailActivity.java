package com.miller.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.utils.ImageUtils;

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
        mPosterView = (ImageView) findViewById(R.id.movie_poster_image_view);
        mTitleView = (TextView) findViewById(R.id.movie_header_text_view);
        mReleaseView = (TextView) findViewById(R.id.movie_release_date_text_view);
        mRating = (RatingBar) findViewById(R.id.movie_rating_bar);
        mSummaryView = (TextView) findViewById(R.id.movie_summary_textview);

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
