package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.qualifier.a;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by hero on 2017/3/2.
 */

public class AUser implements IUser {

    private static final String TAG = AUser.class.getSimpleName();

    private String name;
    private int age;

    @Inject
    @Named("AAO")
    IOpreate opreate;

    @Inject
    @Named("ABAO")
    IOpreate opreate1;

    @Inject
    public AUser(@a String name, @a int age) {
        this.name = name;
        this.age = age;
    }
    @Override
    public void eat() {
        Log.i(TAG, "name:" + name + "   age:" + age + "  opreate:" + opreate);
        opreate.exect();
        opreate1.exect();
    }
}
