package com.example.myapplication.component;

import com.example.myapplication.MainActivity;
import com.example.myapplication.module.OpreateModule;

import dagger.Component;

/**
 * Created by hero on 2017/3/2.
 */
@Component(modules = {OpreateModule.class})
public interface UserComponent {
    void inject(MainActivity activity);
}
