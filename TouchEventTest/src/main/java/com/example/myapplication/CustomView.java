package com.example.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

/**
 * Created by hero on 2017/3/18.
 */

public class CustomView extends View {

    private static final String TAG = CustomView.class.getSimpleName();

    private float x;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, TAG + " ----> dispatchTouchEvent:" + event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, TAG + " ----> onTouchEvent:" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:

                LayoutParams params = (LayoutParams) getLayoutParams();
                if (params == null) {
                    params = new LayoutParams(getWidth(), getHeight());
                    setLayoutParams(params);
                }
                float temp = event.getRawX() - x;
                x = event.getRawX();
                temp += params.leftMargin;
                if (temp <= 0) {
                    temp = 0;
                }
                int max = ((View)getParent()).getWidth() - getWidth();
                if (temp >= max) {
                    temp = max;
                }
                params.leftMargin = (int) temp;
                setLayoutParams(params);
                break;
        }
        return true;
    }
}
