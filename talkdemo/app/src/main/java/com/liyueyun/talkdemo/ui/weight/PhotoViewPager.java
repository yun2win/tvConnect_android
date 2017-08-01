package com.liyueyun.talkdemo.ui.weight;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hejie on 2017/5/10.
 */

public class PhotoViewPager extends ViewPager {

    private boolean isScroll = true;
    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isScroll() {
        return isScroll;
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       if(!isScroll){
           return false;
       }else {
           try {
               return super.onInterceptTouchEvent(ev);
           } catch (Exception e) {
               e.printStackTrace();
           }
           return false;
       }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!isScroll){
            return false;
        }else {
            return super.onTouchEvent(ev);
        }
    }
}