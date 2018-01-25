package com.oneside.ui.study.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.oneside.utils.LogUtils;

/**
 * Created by fupingfu on 2017/12/14.
 */
public class MyButton extends android.support.v7.widget.AppCompatButton {
    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtils.e("study button onTouchEvent %s", "onDraw");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtils.e("study button onTouchEvent %s", "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LogUtils.e("study button onTouchEvent %s", "onLayout");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.e("study button onTouchEvent %s", event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.e("study button dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
}
