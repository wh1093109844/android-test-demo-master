package com.example.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.myapplication.ImageTypeListAdapter.OnItemClickListener;
import com.example.myapplication.bean.Galleryclass;
import com.example.myapplication.retrofit.RetrofitInterface.GetImageTypeList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ViewPager mViewPager;
    private ImageTypeListAdapter mImageTypeListAdapter;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.image_type_list);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false));
        mImageTypeListAdapter = new ImageTypeListAdapter(null);
        mRecyclerView.setAdapter(mImageTypeListAdapter);
        mImageTypeListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mViewPager.setCurrentItem(position);
            }
        });

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), null);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mImageTypeListAdapter.setCurrentIndex(position);
                mRecyclerView.scrollToPosition(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.tngou.net").addConverterFactory(MyConverterFactory.create()).build();

        GetImageTypeList getImageTypeList = retrofit.create(GetImageTypeList.class);
        Call<List<Galleryclass>> repo = getImageTypeList.getImageTypeList();
        repo.enqueue(new Callback<List<Galleryclass>>() {
            @Override
            public void onResponse(Call<List<Galleryclass>> call, Response<List<Galleryclass>> response) {
                List<Galleryclass> list = response.body();
                if (list != null) {
                    for (Galleryclass type : list) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("id:").append(type.id).append("\tname:").append(type.name).append("\ttitle:").append(type.title).append("\tkeywords:").append(type.keywords).append("\tdesc:").append(type.description).append("\tseq:").append(type.seq);
                        Log.i(TAG, builder.toString());

                    }
                }
                mImageTypeListAdapter.update(list);
                mViewPagerAdapter.update(list);
            }

            @Override
            public void onFailure(Call<List<Galleryclass>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Galleryclass> mGalleryclasses;
        public ViewPagerAdapter(FragmentManager fm, List<Galleryclass> list) {
            super(fm);
            this.mGalleryclasses = list;
        }

        @Override
        public Fragment getItem(int position) {
            ImageListFragment fragment = new ImageListFragment();
            Galleryclass galleryclass = mGalleryclasses.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt(ImageListFragment.IMAGE_TYPE_ID, galleryclass.id);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mGalleryclasses == null ? 0 : mGalleryclasses.size();
        }

        public void update(List<Galleryclass> galleryclasses) {
            this.mGalleryclasses = galleryclasses;
            notifyDataSetChanged();
        }
    }
}
