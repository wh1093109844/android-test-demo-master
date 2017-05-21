package com.example.myapplication;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.example.myapplication.bean.ImageDetail;
import com.example.myapplication.bean.Picture;
import com.example.myapplication.retrofit.RetrofitInterface.GetImageDetial;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hero on 2017/5/22.
 */

public class ImageDetailActivity extends AppCompatActivity {

    private long id;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");
        setTitle(bundle.getString("title"));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewAdapter = new RecyclerViewAdapter(null);
        mRecyclerView.setAdapter(mViewAdapter);
        getDate();
    }

    public void getDate() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.tngou.net").addConverterFactory(GsonConverterFactory.create()).build();
        GetImageDetial imageDetialService = retrofit.create(GetImageDetial.class);
        imageDetialService.getImageDetial(id).enqueue(new Callback<ImageDetail>() {
            @Override
            public void onResponse(Call<ImageDetail> call, Response<ImageDetail> response) {
                mViewAdapter.update(response.body().list);
            }

            @Override
            public void onFailure(Call<ImageDetail> call, Throwable throwable) {

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView mDraweeView;

        public ViewHolder(View itemView) {
            super(itemView);
            mDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.image_detail);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private static final String BASE_URL = "http://www.tngou.net/tnfs/image";


        private List<Picture> mGalleryList;
        private LayoutInflater mLayoutInflater;

        public RecyclerViewAdapter (List<Picture> list) {
            this.mGalleryList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mLayoutInflater == null) {
                mLayoutInflater = (LayoutInflater) parent.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            }
            View itemView = mLayoutInflater.inflate(R.layout.item_image_detail, null);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Picture gallery = mGalleryList.get(position);
            holder.mDraweeView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.default_height);
            holder.mDraweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                                  .setUri(Uri.parse(buildCoverUrl(gallery.src)))
                                                  .setTapToRetryEnabled(true)
                                                  .setOldController(holder.mDraweeView.getController())
                                                  .setControllerListener(new CustomerControllerListener(holder.mDraweeView))
                                                  .build();

            holder.mDraweeView.setController(controller);
        }

        @Override
        public int getItemCount() {
            return mGalleryList == null ? 0 : mGalleryList.size();
        }

        public String buildCoverUrl(String coverPath) {
            String url = BASE_URL + coverPath;
            return url;
        }

        public void update(List<Picture> list) {
            this.mGalleryList = list;
            notifyDataSetChanged();
        }
    }

    class CustomerControllerListener extends BaseControllerListener<ImageInfo> {

        private View mView;
        public CustomerControllerListener(View view) {
            this.mView = view;
        }

        @Override
        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            int width = imageInfo.getWidth();
            int height = imageInfo.getHeight();
            int viewWidth = getWindowManager().getDefaultDisplay().getWidth();
            int viewHeight = viewWidth * height / width;
            LayoutParams layoutParams = mView.getLayoutParams();
            layoutParams.width = viewWidth;
            layoutParams.height = viewHeight;
            ViewGroup viewGroup = (ViewGroup) mView.getParent();
            viewGroup.removeView(mView);
            viewGroup.addView(mView, 0,layoutParams);
        }
    }
}
