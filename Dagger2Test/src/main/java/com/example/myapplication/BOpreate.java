package com.example.myapplication;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by hero on 2017/3/4.
 */

public class BOpreate implements IOpreate {

    private static final String TAG = BOpreate.class.getSimpleName();

    private String name;
    private int age;

    @Inject
    public BOpreate(String name, int age) {
        this.name = name;
        this.age = age;
    }
    @Override
    public void exect() {
        Log.i(TAG, "exect: name:" + name + "  age:" + age);
    }
}
