package com.example.huma.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.huma.popularmovies.db.MoveContract.MovieEntry;

/**
 * Created by huma on 2/1/2016.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "movies";
    private static final int DB_VERSION = 1;
    String MOVIES = "movies";

    public MoviesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MovieEntry.TABLE_NAME + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL,"
                + MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"
                + MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT,"
                + MovieEntry.COLUMN_MOVIE_POPULARITY + " REAL,"
                + MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL,"
                + MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " INTEGER,"
                + MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT,"
                + MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT,"
                + MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT,"
                + "UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
