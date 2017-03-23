package com.miller.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Video;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private ArrayList<Video> mTrailers;
    private OnVideoClickedListener mListener;

    public TrailerAdapter(ArrayList<Video> videos, OnVideoClickedListener listener) {
        mTrailers = videos;
        mListener = listener;
    }

    /**
     * Add items to the end of the current adapter's dataset.
     * @param videos
     */
    public void setData(ArrayList<Video> videos) {
        mTrailers = videos;
        notifyDataSetChanged();
    }

    public ArrayList<Video> getData() {
        return mTrailers;
    }

    public void clear() {
        mTrailers.clear();
        notifyDataSetChanged();
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_trailer_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Video video = mTrailers.get(position);
        holder.mTextView.setText(video.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onVideoClicked(video);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mTrailers == null || mTrailers.isEmpty()) ? 0 : mTrailers.size();
    }

    public interface OnVideoClickedListener {
        void onVideoClicked(Video video);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.trailer_text_view);
        }
    }
}
