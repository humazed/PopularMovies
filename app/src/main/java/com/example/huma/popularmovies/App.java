package com.example.huma.popularmovies;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
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
        Stetho.initializeWithDefaults(this);
        Utils.init(this);
    }
}
