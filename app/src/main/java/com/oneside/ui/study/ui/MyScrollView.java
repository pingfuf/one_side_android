package com.oneside.ui.study.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.oneside.utils.LogUtils;

/**
 * Created by fupingfu on 2017/12/14.
 */

public class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        LogUtils.e("study scrollView onTouchEvent %s", ev.getAction());
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        LogUtils.e("study scrollView onInterceptTouchEvent");
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.e("study scrollView dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
}
