package org.bean;

import android.app.Application;

/**
 * Created by liuyulong on 15-2-9.
 */
public class MainApp extends Application {

    private static MainApp sApp;

    @Override
    public void onCreate() {
        sApp = this;
        super.onCreate();
    }

    public static MainApp getApp() {
        return sApp;
    }
}
