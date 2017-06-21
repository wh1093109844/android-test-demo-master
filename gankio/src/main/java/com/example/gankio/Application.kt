package com.example.gankio

import android.app.Application
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig

/**
 * Created by hero on 2017/6/17.
 */
class Application(): Application() {

    override fun onCreate() {
        super.onCreate()
        val diskCacheConfig: DiskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setMaxCacheSize(Const.DISK_CACHE_MAX_SIZE)
                .setBaseDirectoryName(Const.DISK_CACHE_DIR_NAME)
                .build()
        var config: ImagePipelineConfig = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(diskCacheConfig)
                .build()
        Fresco.initialize(this, config)
    }
}