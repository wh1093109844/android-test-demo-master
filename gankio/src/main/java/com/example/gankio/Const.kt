package com.example.gankio

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by za-wanghe on 2017/6/16.
 */
object Const {
    const val BASE_URL = "http://gank.io"
    private const val API_URL = "/api/data/%1${'$'}s/{pageSize}/{page}"

    const val WELFARE_URL = "/api/data/福利/{pageSize}/{page}"
    const val ALL_URL = "/api/data/all/{pageSize}/{page}"
    const val ANDROID_URL = "/api/data/Android/{pageSize}/{page}"
    const val IOS_URL = "/api/data/iOS/{pageSize}/{page}"
    const val VIDEO_URL = "/api/data/休息视频/{pageSize}/{page}"
    const val EXTRA_URL = "/api/data/扩展资源/{pageSize}/{page}"
    const val FRONT_END_URL = "/api/data/前端/{pageSize}/{page}"
    const val RECOMMEND_URL = "/api/data/瞎推荐/{pageSize}/{page}"
    const val APP_URL = "/api/data/App/{pageSize}/{page}"

    const val PAGE_SIZE = 20

    const val SPAN_COUNT = 2

    const val PRE_LOAD_SIZE = 5

    const val DISK_CACHE_MAX_SIZE: Long = 50 * 1024 * 1024
    const val DISK_CACHE_DIR_NAME = "gankio"

    val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}