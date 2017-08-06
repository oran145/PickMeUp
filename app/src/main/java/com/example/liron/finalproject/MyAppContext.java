package com.example.liron.finalproject;

import android.app.Application;
import android.content.Context;

/**
 * Created by liron on 25-Jul-17.
 */

public class MyAppContext extends Application{

    private static Context context;
    public void onCreate() {
        super.onCreate();
        MyAppContext.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return MyAppContext.context;
    }
}
