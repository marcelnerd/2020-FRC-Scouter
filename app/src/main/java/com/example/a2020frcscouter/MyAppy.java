package com.example.a2020frcscouter;

import android.app.Application;
import android.content.Context;

public class MyAppy extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyAppy.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyAppy.context;
    }
}
