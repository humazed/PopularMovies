package com.example.huma.popularmovies;

import android.app.Application;

import com.idescout.sql.SqlScoutServer;

/**
 * User: YourPc
 * Date: 4/1/2017
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SqlScoutServer.create(this, getPackageName());
    }
}
