package com.miller.popularmovies.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miller.popularmovies.R;
import com.miller.popularmovies.adapters.FavoritesAdapter;
import com.miller.popularmovies.providers.MovieFavoritesContract;

public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private FavoritesAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.favorites_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        Uri uri = MovieFavoritesContract.MovieFavoriteEntry.CONTENT_URI;
        return new CursorLoader(getContext(), uri, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        cursor.moveToFirst();
        mAdapter = new FavoritesAdapter(getContext());
        mAdapter.swapCursor(cursor);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {}
}
