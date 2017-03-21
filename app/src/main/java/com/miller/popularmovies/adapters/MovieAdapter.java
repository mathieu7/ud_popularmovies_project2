package com.miller.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.utils.ImageUtils;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Movie> mMovies;
    private Context mContext;
    private OnMovieClickedListener mListener;

    public MovieAdapter(ArrayList<Movie> movies, Context context) {
        mMovies = movies;
        mContext = context;
        if (!(mContext instanceof OnMovieClickedListener) && !(mContext instanceof Activity)) {
            throw new IllegalArgumentException("Activity Context must implement OnMovieClickedListener");
        }
        mListener = (OnMovieClickedListener) mContext;
    }

    /**
     * Add items to the end of the current adapter's dataset.
     * @param movies
     */
    public void addItems(ArrayList<Movie> movies) {
        int oldSize = mMovies.size();
        mMovies.addAll(movies);
        notifyItemRangeInserted(oldSize, movies.size());
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }

    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_movie_view_holder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        ImageUtils.setMoviePoster(movie.getPosterPath(), mContext, holder.mImageView);
        final ImageView imageView = holder.mImageView;
        imageView.setContentDescription(movie.getTitle());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieClicked(movie, imageView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mMovies == null || mMovies.isEmpty()) ? 0 : mMovies.size();
    }

    public interface OnMovieClickedListener {
        void onMovieClicked(Movie movie, ImageView imageView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        ViewHolder(ImageView view) {
            super(view);
            mImageView = view;
        }
    }
}
