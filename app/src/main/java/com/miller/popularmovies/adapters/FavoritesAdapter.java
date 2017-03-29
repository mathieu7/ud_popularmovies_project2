package com.miller.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.providers.MovieFavoritesContract;


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;

    public FavoritesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_favorite_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int movieIdIndex = mCursor.getColumnIndex(MovieFavoritesContract.MovieFavoriteEntry.COLUMN_MOVIE_ID);
        int titleIndex = mCursor.getColumnIndex(MovieFavoritesContract.MovieFavoriteEntry.COLUMN_MOVIE_TITLE);
        mCursor.moveToPosition(position);
        String movieId = mCursor.getString(movieIdIndex);
        String movieTitle = mCursor.getString(titleIndex);
        holder.itemView.setTag(movieId);
        holder.movieTitleView.setText(movieTitle);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitleView;

        ViewHolder(View itemView) {
            super(itemView);
            movieTitleView = (TextView) itemView.findViewById(R.id.favorite_movie_title_view);
        }
    }
}