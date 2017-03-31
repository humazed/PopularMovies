package com.example.huma.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.huma.popularmovies.db.MoveContract.MovieEntry;
import com.example.huma.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;


public class MoviesDBProviderUtils {
    Context mContext;
    private SQLiteDatabase dp;

    public MoviesDBProviderUtils(Context context) {
        mContext = context;
        dp = new MoviesDBHelper(context).getWritableDatabase();
    }

    public void addMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieEntry.COLUMN_MOVIE_POPULARITY, movie.getPopularity());
        values.put(MovieEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());
        values.put(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());

        mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, values);

//        dp.insert(MovieEntry.COLUMN_TABLE_NAME, null, values);
    }

    public void deleteMovie(Movie movie) {
        mContext.getContentResolver().delete(MovieEntry.CONTENT_URI,
                MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(movie.getId())});

//        dp.delete(MovieEntry.COLUMN_TABLE_NAME, MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(movie.getId())});
    }

    public List<Movie> getMovies() {
        String[] columns = {MovieEntry.COLUMN_MOVIE_ID,
                MovieEntry.COLUMN_MOVIE_TITLE,
                MovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieEntry.COLUMN_MOVIE_POPULARITY,
                MovieEntry.COLUMN_MOVIE_VOTE_COUNT,
                MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                MovieEntry.COLUMN_MOVIE_BACKDROP_PATH};


//        Cursor cursor = dp.query(MovieEntry.COLUMN_TABLE_NAME, columns,
//                null, null, null, null, null);

        Cursor cursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, columns,
                null, null, null);

        List<Movie> movies = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_OVERVIEW)));
                movie.setPopularity(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POPULARITY))));
                movie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_VOTE_COUNT))));
                movie.setVoteAverage(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE))));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER_PATH)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_BACKDROP_PATH)));
                movies.add(movie);
            }
            cursor.close();
        }

        return movies;
    }

    public boolean isFav(Movie movie) {
        final String selectQuery = "SELECT * FROM " + MovieEntry.TABLE_NAME + " WHERE " + MovieEntry.COLUMN_MOVIE_ID + " = ?";
        Cursor cursor = dp.rawQuery(selectQuery, new String[]{String.valueOf(movie.getId())});
        boolean isFav = cursor.moveToNext();
        cursor.close();
        return isFav;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dp.close();
    }
}
