package com.miller.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Review;
import com.miller.popularmovies.models.Video;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> mReviews;

    public ReviewAdapter(ArrayList<Review> reviews) {
        mReviews = reviews;
    }
    public ArrayList<Review> getData() {
        return mReviews;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_review_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review review = mReviews.get(position);
        holder.mContentTextView.setText(review.getContent());
        holder.mAuthorTextView.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        return (mReviews == null || mReviews.isEmpty()) ? 0 : mReviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAuthorTextView, mContentTextView;

        ViewHolder(View view) {
            super(view);
            mAuthorTextView = (TextView) view.findViewById(R.id.review_author_text_view);
            mContentTextView = (TextView) view.findViewById(R.id.review_content_text_view);
        }
    }
}
