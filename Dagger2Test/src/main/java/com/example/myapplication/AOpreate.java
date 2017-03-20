package com.example.myapplication;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by hero on 2017/3/4.
 */

public class AOpreate implements IOpreate {

    private static final String TAG = AOpreate.class.getSimpleName();

    @Inject
    public AOpreate(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private String name;
    private int age;

    @Override
    public void exect() {
        Log.i(TAG, "name:" + name + "  age:" + age);
    }
}
