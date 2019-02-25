package com.example.utils;

import android.app.Application;

public class BaseApplication extends Application {

    public static BaseApplication application;

    public static BaseApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
