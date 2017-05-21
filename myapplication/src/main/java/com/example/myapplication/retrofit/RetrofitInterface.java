package com.example.myapplication.retrofit;

import com.example.myapplication.bean.Galleryclass;
import com.example.myapplication.bean.ImageDetail;
import com.example.myapplication.bean.ImageList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hero on 2017/5/21.
 */

public class RetrofitInterface {
    public interface GetImageTypeList {
        @GET("/tnfs/api/classify")
        Call<List<Galleryclass>> getImageTypeList();
    }

    public interface GetImageListByType {
        @POST("/tnfs/api/list")
        Call<ImageList> getImageList(@Query("id") int id, @Query("page") int page, @Query("rows") int rows);
    }

    public interface GetImageDetial {
        @POST("/tnfs/api/show")
        Call<ImageDetail> getImageDetial(@Query("id") long id);
    }
}
