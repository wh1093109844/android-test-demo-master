package com.example.gankio.retrofit
import com.example.gankio.Const
import com.example.gankio.model.WelfareResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by za-wanghe on 2017/6/16.
 */

interface RequestWelfareService {
    @GET(Const.WELFARE_URL)
    fun getWelfareList(@Path("pageSize") pageSize: Int, @Path("page") page: Int): Call<WelfareResult>
}
