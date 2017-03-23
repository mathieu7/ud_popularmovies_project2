package com.miller.popularmovies.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.Video;
import com.miller.popularmovies.utils.VideoUtils;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener,
 DetailFragment.Listener {
    private Toolbar mToolbar;
    private String mMovieTitle;
    private FloatingActionButton mFavoriteFAB;

    @Override
    public void onClick(View v) {
        if (v == mFavoriteFAB) {
            //TODO: implement
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(MainActivity.MOVIE_INTENT_EXTRA_KEY)) {
            Movie movie = getIntent().getParcelableExtra(MainActivity.MOVIE_INTENT_EXTRA_KEY);
            mMovieTitle = movie.getTitle();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            DetailFragment fragment = DetailFragment.newInstance(movie);
            fragmentTransaction.add(R.id.detail_container, fragment).commit();
        } else {
            finish();
        }
        setupActionBar();
        mFavoriteFAB = (FloatingActionButton) findViewById(R.id.favorite_movie_fab);
        mFavoriteFAB.setOnClickListener(this);
    }

    private void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitle(mMovieTitle);
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

    @Override
    public void onTrailerClicked(Video video) {
        VideoUtils.openYouTubeTrailer(video, this);
    }
}
