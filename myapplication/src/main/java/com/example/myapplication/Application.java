package com.example.myapplication;


import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by hero on 2017/5/21.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
