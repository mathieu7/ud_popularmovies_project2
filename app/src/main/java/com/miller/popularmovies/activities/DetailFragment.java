package com.miller.popularmovies.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.adapters.TrailerAdapter;
import com.miller.popularmovies.loader.MovieDBApiLoader;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.MovieVideos;
import com.miller.popularmovies.models.Video;
import com.miller.popularmovies.utils.ImageUtils;

import java.util.ArrayList;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<MovieVideos> {
    private static final String MOVIE_PARAM = "movie";

    /**
     * The Movie this fragment is displaying details for.
     */
    private Movie mMovie;

    /**
     * Whether or not the movie has trailers to display.
     */
    private boolean mHasTrailers;

    /**
     * Our observer
     */
    private Listener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiate the DetailFragment with a given Movie.
     * @param movie
     * @return
     */
    public static DetailFragment newInstance(@NonNull Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_PARAM, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(MOVIE_PARAM);
        } else {
            throw new IllegalStateException("Cannot instantiate DetailFragment without arguments.");
        }
    }

    private ImageView mPosterView;
    private RatingBar mRating;
    private TextView mTitleView, mReleaseView, mSummaryView;
    private RecyclerView mTrailerRecyclerView;

    private TrailerAdapter mTrailerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_detail, container, false);
        mPosterView = (ImageView) view.findViewById(R.id.movie_poster_image_view);
        mTitleView = (TextView) view.findViewById(R.id.movie_header_text_view);
        mReleaseView = (TextView) view.findViewById(R.id.movie_release_date_text_view);
        mRating = (RatingBar) view.findViewById(R.id.movie_rating_bar);
        mSummaryView = (TextView) view.findViewById(R.id.movie_summary_textview);
        mTitleView.setText(mMovie.getTitle());
        mReleaseView.setText(mMovie.getReleaseDate());
        mSummaryView.setText(mMovie.getOverview());
        mRating.setRating(mMovie.getVoteAverage().floatValue() / 2);

        ImageUtils.setMoviePoster(mMovie.getPosterPath(), getContext(), mPosterView);

        mHasTrailers = mMovie.getVideo();
        return view;
    }

    /**
     * Initialize loader for downloading movie trailers, if possible.
     */
    private void displayTrailers() {
        if (!mHasTrailers) return;
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DetailFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface Listener {
        void onTrailerClicked(Video video);
    }

    private static final int LOADER_ID = 1;

    @Override
    public Loader<MovieVideos> onCreateLoader(int id, Bundle args) {
        return null;
        //return new MovieDBApiLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<MovieVideos> loader, MovieVideos data) {
        ArrayList<Video> videos = (ArrayList<Video>) data.getResults();
        mTrailerAdapter.setData(videos);
    }

    @Override
    public void onLoaderReset(Loader<MovieVideos> loader) {
        mTrailerAdapter.setData(null);
    }
}
