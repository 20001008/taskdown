package com.task;

import android.app.Application;
import android.content.Context;

public class  Myappcation extends Application {
private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
    }
    public static Context getContext()
    {
        return context;
    }
}
