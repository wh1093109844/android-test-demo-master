package com.example.myapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myapplication.bean.Galleryclass;
import com.example.myapplication.retrofit.RetrofitInterface.GetImageTypeList;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private SimpleDraweeView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();

    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        cover = (SimpleDraweeView) findViewById(R.id.conver);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), null);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected:" + position);
                mTabLayout.getTabAt(position).select();
                cover.setImageURI("http://www.tngou.net/tnfs/image/ext/161016/3f2ab53c286b8a5c3949616bafc805ca.jpg");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                Log.i(TAG, "onTabSelected:" + tab.getPosition());
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {
                Log.i(TAG, "onTabUnselected:" + tab.getPosition());
            }

            @Override
            public void onTabReselected(Tab tab) {
                Log.i(TAG, "onTabReselected:" + tab.getPosition());
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
                    for (int i = 0; i < list.size(); i++) {
                        Galleryclass type = list.get(i);
                        StringBuilder builder = new StringBuilder();
                        builder.append("id:").append(type.id).append("\tname:").append(type.name).append("\ttitle:").append(type.title).append("\tkeywords:").append(type.keywords).append("\tdesc:").append(type.description).append("\tseq:").append(type.seq);
                        Log.i(TAG, builder.toString());
                        Tab tab = mTabLayout.newTab();
                        tab.setText(type.title);
                        tab.setTag(type);
                        mTabLayout.addTab(tab);
                    }
                }
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
