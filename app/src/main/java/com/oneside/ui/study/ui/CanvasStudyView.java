package com.oneside.ui.study.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;

/**
 * Created by fupingfu on 2017/6/22.
 */

public class CanvasStudyView extends View {
    private float mProcess;
    private Paint mPaint;
    private ProcessAnim mAnim;
    private float mDuration;

    public CanvasStudyView(Context context) {
        super(context);
        init();
    }

    public CanvasStudyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setTextSize(ViewUtils.rp(3));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        mPaint.setStrokeWidth(ViewUtils.rp(2));
        mAnim = new ProcessAnim();
        mDuration = 1;
    }

    public void setProcess(float process) {
        mProcess = process;
        mDuration = 1;
        postInvalidate();
    }

    public void startAnimation() {
        mAnim.setDuration(1500);
        mDuration = 1500;
        startAnimation(mAnim);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        LogUtils.e("canvas" + ", width = " + getWidth() );
        float radius = mProcess * 180 * 2;
        float angle = 180 * 1.5f;
        canvas.drawArc(rectF, angle, radius, false, mPaint);
    }

    private class ProcessAnim extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mProcess = interpolatedTime;
            LogUtils.e("process = " + mProcess);
            postInvalidate();
        }
    }
}
