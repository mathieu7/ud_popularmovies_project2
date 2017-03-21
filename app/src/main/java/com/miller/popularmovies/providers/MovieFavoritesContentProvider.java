package com.miller.popularmovies.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import static com.miller.popularmovies.providers.MovieFavoritesContract.MovieFavoriteEntry.TABLE_NAME;

public class MovieFavoritesContentProvider extends ContentProvider {
    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieFavoritesContract.AUTHORITY,
                MovieFavoritesContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(MovieFavoritesContract.AUTHORITY,
                MovieFavoritesContract.PATH_FAVORITES + "/#", FAVORITES);
        return uriMatcher;
    }

    private MovieFavoritesDbHelper mMovieFavoritesDbHelper;

    @Override
    public boolean onCreate() {
        mMovieFavoritesDbHelper = new MovieFavoritesDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMovieFavoritesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(
                            MovieFavoritesContract.MovieFavoriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mMovieFavoritesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case FAVORITES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(TABLE_NAME, null,
                        "movie_id=?",
                        new String[]{id},
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieFavoritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int favoritesDeleted = 0;

        switch (match) {
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                favoritesDeleted = db.delete(TABLE_NAME, "movie_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (favoritesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favoritesDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mMovieFavoritesDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FAVORITES:
                rowsUpdated = db.update(TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
