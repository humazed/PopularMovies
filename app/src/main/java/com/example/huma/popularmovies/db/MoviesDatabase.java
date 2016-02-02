package com.example.huma.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by huma on 2/1/2016.
 */
public class MoviesDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "movies";
    private static final int DB_VERSION = 1;
    String MOVIES = "movies";

    public MoviesDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MovieTable.TABLE_NAME + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MovieTable.MOVIE_ID + " TEXT NOT NULL,"
                + MovieTable.MOVIE_TITLE + " TEXT NOT NULL,"
                + MovieTable.MOVIE_OVERVIEW + " TEXT,"
                + MovieTable.MOVIE_POPULARITY + " REAL,"
                + MovieTable.MOVIE_VOTE_AVERAGE + " REAL,"
                + MovieTable.MOVIE_VOTE_COUNT + " INTEGER,"
                + MovieTable.MOVIE_BACKDROP_PATH + " TEXT,"
                + MovieTable.MOVIE_POSTER_PATH + " TEXT,"
                + MovieTable.MOVIE_RELEASE_DATE + " TEXT,"
                + "UNIQUE (" + MovieTable.MOVIE_ID + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
