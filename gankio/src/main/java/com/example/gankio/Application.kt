package com.example.gankio

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by hero on 2017/6/17.
 */
class Application(): Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}