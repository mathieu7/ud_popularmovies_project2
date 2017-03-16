package com.miller.popularmovies.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;

public class DetailActivity extends AppCompatActivity {
    private Movie mMovie;
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
    }

}
