package com.miller.popularmovies.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.utils.ImageUtils;

public class DetailFragment extends Fragment {
    private static final String MOVIE_PARAM = "movie";
    private Movie mMovie;

    private OnFragmentInteractionListener mListener;

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
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
