package com.example.huma.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.huma.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;


public class MoviesDBProvider {
    Context mContext;
    private SQLiteDatabase dp;

    public MoviesDBProvider(Context context) {
        mContext = context;
        dp = new MoviesDatabase(context).getWritableDatabase();
    }

    public boolean addMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieTable.MOVIE_ID, movie.getId());
        values.put(MovieTable.MOVIE_TITLE, movie.getTitle());
        values.put(MovieTable.MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieTable.MOVIE_POPULARITY, movie.getPopularity());
        values.put(MovieTable.MOVIE_VOTE_COUNT, movie.getVoteCount());
        values.put(MovieTable.MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieTable.MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieTable.MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(MovieTable.MOVIE_BACKDROP_PATH, movie.getBackdropPath());

        return (1 != dp.insert(MovieTable.TABLE_NAME, null, values));
    }

    public void deleteMovie(Movie movie) {
        dp.delete(MovieTable.TABLE_NAME, MovieTable.MOVIE_ID + "=?", new String[]{String.valueOf(movie.getId())});
    }

    public List<Movie> getMovies() {
        String[] columns = {MovieTable.MOVIE_ID,
                MovieTable.MOVIE_TITLE,
                MovieTable.MOVIE_OVERVIEW,
                MovieTable.MOVIE_POPULARITY,
                MovieTable.MOVIE_VOTE_COUNT,
                MovieTable.MOVIE_VOTE_AVERAGE,
                MovieTable.MOVIE_RELEASE_DATE,
                MovieTable.MOVIE_POSTER_PATH,
                MovieTable.MOVIE_BACKDROP_PATH};


        Cursor cursor = dp.query(MovieTable.TABLE_NAME, columns,
                null, null, null, null, null);

        List<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_ID))));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_TITLE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_OVERVIEW)));
            movie.setPopularity(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_POPULARITY))));
            movie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_VOTE_COUNT))));
            movie.setVoteAverage(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_VOTE_AVERAGE))));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_RELEASE_DATE)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_POSTER_PATH)));
            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieTable.MOVIE_BACKDROP_PATH)));
            movies.add(movie);
        }
        cursor.close();
        return movies;
    }

    public boolean isFav(Movie movie) {
        final String selectQuery = "SELECT * FROM " + MovieTable.TABLE_NAME + " WHERE " + MovieTable.MOVIE_ID + " = ?";
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
