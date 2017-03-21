package com.example.myapplication.module;

import com.example.myapplication.AOpreate;
import com.example.myapplication.BOpreate;
import com.example.myapplication.IOpreate;
import com.example.myapplication.qualifier.a;
import com.example.myapplication.qualifier.b;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hero on 2017/3/4.
 */
@Module(includes = UserModule.class)
public class OpreateModule {

    @Provides
    @Named("AAO")
    public IOpreate getOpreate1(@a String name, @a int age) {
        return new AOpreate(name, age);
    }

    @Provides
    @Named("ABAO")
    public IOpreate getOpreate3(@a String name, @a int age) {
        return new BOpreate(name, age);
    }

    @Provides
    @Named("BAO")
    public IOpreate getOpreate2(@b String name, @b int age) {
        return new AOpreate(name, age);
    }
}
