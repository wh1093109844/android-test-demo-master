package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnScrollChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.example.myapplication.bean.Gallery;
import com.example.myapplication.bean.ImageList;
import com.example.myapplication.retrofit.RetrofitInterface;
import com.example.myapplication.retrofit.RetrofitInterface.GetImageListByType;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hero on 2017/5/21.
 */

public class ImageListFragment extends Fragment {
    private static final String Tag = ImageListFragment.class.getSimpleName();
    private static final int DEFATULT_ROWS = 10;
    public static final String IMAGE_TYPE_ID = "image_type_id";
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ImageList mImageList;
    private int imageTypeId;
    private int currentPage = 1;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_list, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            imageTypeId = bundle.getInt(IMAGE_TYPE_ID);
        }
        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getImageList(currentPage);
    }

    private void initView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mRecyclerViewAdapter = new RecyclerViewAdapter(null);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.Adapter adapter, int position) {
                RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) adapter;
                Intent intent = new Intent(view.getContext(), ImageDetailActivity.class);
                Gallery gallery = recyclerViewAdapter.getItem(position);
                intent.putExtra("id", gallery.id);
                intent.putExtra("title", gallery.title);
                startActivity(intent);
            }
        });
        mRecyclerView.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });
    }

    private void getImageList(final int page) {
        isLoading = true;
        Retrofit retrofit = new Builder().baseUrl("http://www.tngou.net").addConverterFactory(GsonConverterFactory.create()).build();
        GetImageListByType imageListService = retrofit.create(RetrofitInterface.GetImageListByType.class);
        Call<ImageList> call = imageListService.getImageList(imageTypeId, page, DEFATULT_ROWS);
        call.enqueue(new Callback<ImageList>() {
            @Override
            public void onResponse(Call<ImageList> call, Response<ImageList> response) {
                mImageList = response.body();
                mRecyclerViewAdapter.nextPage(mImageList.tngou);
                isLoading = false;
                currentPage = page;
            }

            @Override
            public void onFailure(Call<ImageList> call, Throwable throwable) {
                isLoading = false;
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView imageListDescTextView;
        public SimpleDraweeView imageListConverImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageListDescTextView = (TextView) itemView.findViewById(R.id.image_desc);
            imageListConverImageView = (SimpleDraweeView) itemView.findViewById(R.id.cover_image);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private static final String BASE_URL = "http://www.tngou.net/tnfs/image";

        private static final int OFFSET = 5;

        private List<Gallery> mGalleryList;
        private LayoutInflater mInflater;
        private OnItemClickListener mOnItemClickListener;

        public RecyclerViewAdapter (List<Gallery> list) {
            this.mGalleryList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mInflater == null) {
                mInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            final View itemView = mInflater.inflate(R.layout.item_image_list, null);
            final ViewHolder viewHolder = new ViewHolder(itemView);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(itemView, RecyclerViewAdapter.this, (int)viewHolder.imageListConverImageView.getTag());
                    }
                }
            });
            return viewHolder;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Gallery gallery = mGalleryList.get(position);
            holder.imageListConverImageView.setTag(position);
            holder.imageListDescTextView.setText(gallery.title);
            holder.imageListConverImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.default_height);
            holder.imageListConverImageView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                                  .setUri(Uri.parse(buildCoverUrl(gallery.img)))
                                                  .setTapToRetryEnabled(true)
                                                  .setOldController(holder.imageListConverImageView.getController())
                                                  .setControllerListener(new CustomerControllerListener(holder.imageListConverImageView, this))
                                                  .build();

            holder.imageListConverImageView.setController(controller);
            if (position + OFFSET >= getItemCount() && !isLoading) {
                getImageList(currentPage + 1);
            }
        }

        public Gallery getItem(int position) {
            if (mGalleryList != null && position < mGalleryList.size()) {
                return mGalleryList.get(position);
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return mGalleryList == null ? 0 : mGalleryList.size();
        }

        public String buildCoverUrl(String coverPath) {
            String url = BASE_URL + coverPath;
            Log.i(Tag, "cover url:" + url);
            return url;
        }

        public void nextPage(List<Gallery> list) {
            if (mGalleryList == null) {
                mGalleryList = new ArrayList<>();
            }
            mGalleryList.addAll(list);
            notifyDataSetChanged();
        }
    }

    class CustomerControllerListener extends BaseControllerListener<ImageInfo> {

        private View mView;
        private RecyclerViewAdapter mRecyclerViewAdapter;
        public CustomerControllerListener(View view, RecyclerViewAdapter adapter) {
            this.mView = view;
            this.mRecyclerViewAdapter = adapter;
        }

        @Override
        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            int width = imageInfo.getWidth();
            int height = imageInfo.getHeight();
            int viewWidth = mView.getWidth();
            int viewHeight = viewWidth * height / width;
            LayoutParams layoutParams = mView.getLayoutParams();
            layoutParams.height = viewHeight;
            ViewGroup viewGroup = (ViewGroup) mView.getParent();
            viewGroup.removeView(mView);
            viewGroup.addView(mView, 0,layoutParams);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.Adapter adapter, int position);
    }
}
