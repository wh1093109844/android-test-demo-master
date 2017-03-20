package com.example.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.RelativeLayout;

/**
 * Created by hero on 2017/3/18.
 */

public class CustomeViewGroup extends RelativeLayout {


    private String TAG = CustomeViewGroup.class.getSimpleName();

    private VelocityTracker mVelocityTracker;

    private int mStatus = 0;

    private static int STATUS_INTERCEPT = 1;
    private static int STATUS_UNINTERCEPT = 2;
    private static int STATUS_RESET = 0;

    private float y;

    public CustomeViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomeViewGroup(Context context, @Nullable AttributeSet attrs, int theme) {
        super(context, attrs, theme);
    }

    private void  initVolocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, TAG + " ------> dispatchTouchEvent:" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, TAG + " ------> onInterceptTouchEvent:" + ev.getAction());
        initVolocityTracker();
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStatus = STATUS_RESET;
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(ev);
                y = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.computeCurrentVelocity(1000);
                float x = mVelocityTracker.getXVelocity();  //左右速度
                float y = mVelocityTracker.getYVelocity();  //上下速度
                //如果上下的速度大于左右的速度，则说明是上下滑动，需要拦截该事件
                if (mStatus == STATUS_RESET && Math.abs(y) > Math.abs(x)) {
                    mStatus = STATUS_INTERCEPT;
                    return true;    //拦截事件
                } else if (x != 0 && y != 0){
                    mStatus = STATUS_UNINTERCEPT;
                }
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, TAG + " ------> onTouchEvent:" + event.getAction() + "   RAWY:" + event.getRawY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float temp = y - event.getRawY();
                y = event.getRawY();
                scrollBy(0, (int)temp);
                break;
            case MotionEvent.ACTION_UP:
                mStatus = STATUS_RESET;
                break;
        }
        return true;
    }
}
