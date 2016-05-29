package com.konka.downloaddemo;

import android.app.Application;

import com.konka.downloadcenter.FileDownloader;

/**
 * Created by Ernest on 2016-5-9.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(this);
    }
}
