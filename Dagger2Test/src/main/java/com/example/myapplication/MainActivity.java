package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myapplication.component.DaggerUserComponent;
import com.example.myapplication.component.UserComponent;
import com.example.myapplication.module.OpreateModule;
import com.example.myapplication.module.UserModule;

import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    @Named("AUSER")
    IUser user;

    @Inject
    @Named("BUSER")
    IUser bUser;

    UserComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        component = DaggerUserComponent.builder().userModule(new UserModule()).opreateModule(new OpreateModule()).build();
        component.inject(this);
        Log.i(TAG, "logcat");

        user.eat();
        bUser.eat();
    }
}
