package com.miller.popularmovies.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MovieFavoritesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "moviefavorites.db";
    private static final int DB_VERSION = 1;

    MovieFavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + MovieFavoritesContract.MovieFavoriteEntry.TABLE_NAME + " (" +
                        MovieFavoritesContract.MovieFavoriteEntry._ID                + " INTEGER PRIMARY KEY, " +
                        MovieFavoritesContract.MovieFavoriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                        MovieFavoritesContract.MovieFavoriteEntry.COLUMN_MOVIE_ID    + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieFavoritesContract.MovieFavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
