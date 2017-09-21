package com.zyh.network;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ruoyun on 2017/3/31.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
