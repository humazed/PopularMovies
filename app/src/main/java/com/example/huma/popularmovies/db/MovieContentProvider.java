package com.example.huma.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.example.huma.popularmovies.db.MoveContract.MovieEntry;

public class MovieContentProvider extends ContentProvider {
    private SQLiteDatabase db;


    public MovieContentProvider() {
    }

    @Override
    public boolean onCreate() {
        db = new MoviesDBHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri = Uri.EMPTY;

        long id = db.insert(MovieEntry.TABLE_NAME, null, values);
        if (id > 0)
            returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor = db.query(
                MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // No need to be implemented.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // No need to be implemented.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
