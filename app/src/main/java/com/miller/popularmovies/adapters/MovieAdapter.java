package com.miller.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.utils.ImageUtils;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(ArrayList<Movie> movies, Context context) {
        mMovies = movies;
        mContext = context;
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

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_movie_view_holder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        ImageUtils.setMoviePoster(movie.getPosterPath(), mContext, holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return (mMovies == null || mMovies.isEmpty()) ? 0 : mMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(ImageView view) {
            super(view);
            mImageView = view;
        }
    }
}
