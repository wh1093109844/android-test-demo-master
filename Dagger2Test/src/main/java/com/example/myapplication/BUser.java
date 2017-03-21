package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.component.OpreateComponent;
import com.example.myapplication.qualifier.b;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by hero on 2017/3/3.
 */

public class BUser implements IUser {

    private static final String TAG = BUser.class.getSimpleName();

    private String name;
    @Inject
    @Named("BAO")
    IOpreate opreate;

    OpreateComponent component;

    @Inject
    public BUser(@b String name) {
        this.name = name;
    }

    @Override
    public void eat() {
        Log.i(TAG, "this is BUser " + name + "  opreate:" + opreate);
        opreate.exect();
    }
}
