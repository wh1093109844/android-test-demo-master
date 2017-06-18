package com.example.gankio.model

/**
 * Created by za-wanghe on 2017/6/16.
 */

data class Welfare(val _id: String, val createdAt: String, val desc: String, val publishedAt: String, val source: String, val type: String, val url: String, val used: Boolean, val who: String)

data class WelfareResult(val error: Boolean, val results: List<Welfare>)

data class ImageSize(val width: Int, val height: Int)

