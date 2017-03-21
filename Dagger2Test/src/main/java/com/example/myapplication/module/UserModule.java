package com.example.myapplication.module;

import com.example.myapplication.AUser;
import com.example.myapplication.BUser;
import com.example.myapplication.IUser;
import com.example.myapplication.qualifier.a;
import com.example.myapplication.qualifier.b;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hero on 2017/3/2.
 */
@Module
public class UserModule {

    @Provides
    @a
    public String getAName() {
        return "zhangsan";
    }

    @Provides
    @a
    public int getAAge() {
        return 150;
    }

    @Provides
    @b
    public int getBAge() {
        return 20;
    }

    @Provides
    @b
    public String getBName() {
        return "lisi";
    }

    @Provides
    @Named("AUSER")
    public IUser getAUser(AUser user) {
        return user;
    }

    @Provides
    @Named("BUSER")
    public IUser getBUser(BUser user) {
        return user;
    }
}
