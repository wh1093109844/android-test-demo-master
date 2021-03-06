package com.example.myapplication.component;

import com.example.myapplication.AUser;
import com.example.myapplication.BUser;
import com.example.myapplication.module.OpreateModule;

import dagger.Component;

/**
 * Created by hero on 2017/3/4.
 */

@Component(modules = OpreateModule.class)
public interface OpreateComponent {
    void inject(AUser user);
    void inject(BUser user);
}
