package com.kuaipao.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.kuaipao.manager.R;

import java.util.Random;

/**
 * Created by pingfu on 16-10-17.
 */
public class XProcessView extends View {
    private int mHeight;
    private int mWidth;
    private Paint mTopPaint;
    private Paint mBottomPaint;
    private float mProcess;
    private Bitmap mBackBitmap;
    private Bitmap mBitmap;
    private ProcessAnim mAnim;

    public XProcessView(Context context) {
        super(context);
        init();
    }

    public XProcessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setProcess(int process) {
        mProcess = (float) process / 100.0f;
        postInvalidate();
    }

    public void stopAnim() {
        clearAnimation();
    }

    public void startAnim() {
        startAnimation(mAnim);
    }

    private void init() {
        mTopPaint = new Paint();
        mTopPaint.setColor(Color.TRANSPARENT);
        mTopPaint.setAntiAlias(true);
        mTopPaint.setStyle(Paint.Style.FILL);

        mBottomPaint = new Paint();
        mBottomPaint.setColor(Color.RED);
        mBottomPaint.setAntiAlias(true);
        mBottomPaint.setStyle(Paint.Style.FILL);

        mBackBitmap = getBitmapById(R.drawable.ic_loading_gray);
        mBitmap = getBitmapById(R.drawable.ic_loading_red);

        mAnim = new ProcessAnim();
        mAnim.setDuration(1500);
        mAnim.setRepeatCount(Integer.MAX_VALUE);
//        mAnim.setRepeatMode(Animation.RELATIVE_TO_SELF);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect srcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        Rect dstRect = new Rect(0, 0, mWidth, mHeight);
        canvas.drawBitmap(mBackBitmap, srcRect, dstRect, new Paint());

        canvas.save();
        //画贝塞尔曲线
        Path mPath = new Path();
        int y;
        int a = 5;
        int top = getStartHeight();
        mPath.moveTo(0, top);//设置Path的起点

        for (int i = 0; i <= mWidth; i++) {
            y = getWaveSize(a, i) + getWaveHeight() + top;
            mPath.quadTo(i, y, i + 1, y);
        }
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.lineTo(0, top);
        mPath.close();

        canvas.clipPath(mPath);
        canvas.drawBitmap(mBitmap, srcRect, dstRect, new Paint());
        canvas.restore();
    }

    private int getStartHeight() {
        int top = (int) (mHeight * (1.0f - mProcess));
        if (top <= 0) {
            top = 0;
        }

        return top;
    }

    private int getWaveHeight() {
        Random random = new Random(System.currentTimeMillis());
        return getWaveSize(random.nextInt(5), random.nextInt(5));
    }

    private int getWaveSize(int i, int j) {
        return (int) (i * Math.sin(j * Math.PI / (100 * mHeight / 600)));
    }

    private class ProcessAnim extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mProcess = interpolatedTime;
            postInvalidate();
        }
    }

    private Bitmap getBitmapById(int resId) {
        return ((BitmapDrawable) ContextCompat.getDrawable(getContext(), resId)).getBitmap();
    }
}

