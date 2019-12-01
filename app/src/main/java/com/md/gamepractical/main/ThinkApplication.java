package com.md.gamepractical.main;

import android.app.Application;
import android.content.res.Configuration;

import com.md.gamepractical.Utils;

public class ThinkApplication extends Application {

    public static Utils utils;

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new Utils(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
