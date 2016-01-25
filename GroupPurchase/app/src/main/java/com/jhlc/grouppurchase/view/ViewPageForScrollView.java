package com.jhlc.grouppurchase.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wang on 2015/4/3.
 */
public class ViewPageForScrollView extends ViewPager {
    private int mLastMotionY;
    private int mLastMotionX;
    private int deltaY,deltaX;

    public ViewPageForScrollView(Context context) {
        super(context);
    }

    public ViewPageForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int)ev.getX();
        int y = (int)ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                deltaY = y - mLastMotionY;
                deltaX = x - mLastMotionX;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int x = (int)ev.getX();
//        int y = (int)ev.getY();
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                Log.d("PullToRefreshView","Tab1ViewPageForScrollView:ACTION_DOWN");
//                mLastMotionX = x;
//                mLastMotionY = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                deltaY = y - mLastMotionY;
//                deltaX = x - mLastMotionX;
//                Log.d("PullToRefreshView","Tab1ViewPageForScrollView:deltaY,deltaX:" + deltaY + "," + deltaX);
//                if(Math.abs(deltaY) > Math.abs(deltaX)){
//                    return false;
//                }
//                else {
//                    return true;
//                }
//            case MotionEvent.ACTION_UP:
//                Log.d("PullToRefreshView","Tab1ViewPageForScrollView:ACTION_UP");
//                break;
//        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            if(Math.abs(deltaY) < Math.abs(deltaX)){
                getParent().requestDisallowInterceptTouchEvent(true);
            }

        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        // 下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            // 采用最大的view的高度
            if (h > height) {
                height = h;
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
