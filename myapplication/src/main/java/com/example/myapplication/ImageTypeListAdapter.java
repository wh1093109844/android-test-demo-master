package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.ImageTypeListAdapter.ViewHolder;
import com.example.myapplication.bean.Galleryclass;

import java.util.List;

/**
 * Created by hero on 2017/5/21.
 */

public class ImageTypeListAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Galleryclass> typeList;
    private OnItemClickListener mOnItemClick;
    private int currentIndex = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView textView) {
            super(textView);
            this.mTextView = textView;
        }

    }

    public ImageTypeListAdapter(List<Galleryclass> typeList) {
        this.typeList = typeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TextView textView = (TextView) inflater.inflate(R.layout.item_image_type_list, null);
        final ViewHolder viewHolder = new ViewHolder(textView);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (int) textView.getTag();
                if (mOnItemClick != null) {
                    mOnItemClick.onItemClick(textView, currentIndex);
                }
                notifyDataSetChanged();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Resources resources = holder.mTextView.getResources();
        if (typeList != null) {
            Galleryclass galleryclass = typeList.get(position);
            if (position == currentIndex) {
                holder.mTextView.setTextColor(Color.WHITE);
            } else {

                holder.mTextView.setTextColor(resources.getColor(android.R.color.darker_gray));
            }
            holder.mTextView.setTag(position);
            holder.mTextView.setText(galleryclass.description);
        }
    }

    @Override
    public int getItemCount() {
        return typeList == null ? 0 : typeList.size();
    }

    public void update(List<Galleryclass> list) {
        this.typeList = list;
        notifyDataSetChanged();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClick) {
        this.mOnItemClick = onItemClick;
    }

}
