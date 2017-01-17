package com.oneside.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.oneside.utils.LogUtils;
import com.oneside.R;

/**
 * Created by pingfu on 16-8-17.
 */
public class XClipView extends View {
    /** 剪切类型：0为横向剪切，1为竖向剪切 */
    private int mClipType;
    private int mClipDegree;

    private int firstPartColor;
    private int mHeight = -1;
    private int mWidth = -1;

    public XClipView(Context context) {
        super(context);
    }

    public XClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public XClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.XClipView);
            mClipType = a.getDimensionPixelSize(R.styleable.XClipView_clip_type, 0);
            mClipDegree = a.getColor(R.styleable.XClipView_clip_degree, 90);
        } finally {
            if(a != null) {
                a.recycle();
            }
        }
    }

    public void setData(int width, int height) {
        mWidth = width;
        mHeight = height;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtils.e("XCLipView width = %s, height= %s", mWidth, mHeight);

        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setColor(Color.WHITE);
        RectF rectF1 = new RectF(0, 0, 60, mHeight);
        Path path1 = new Path();
        canvas.drawRoundRect(rectF1, 20, 20, paint1);
        path1.moveTo(30, 0);
        path1.lineTo((int)(mWidth * (3.0/ 4.0)), 0);
        path1.lineTo((int)(mWidth * (1.0/ 2.0)), mHeight);
        path1.lineTo(30, mHeight);
        path1.close();
        canvas.drawPath(path1, paint1);

        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        Shader mShader = new LinearGradient((int)(mWidth * (2.0/ 3.0)), 0, mWidth, mHeight,
                new int[] { Color.RED, Color.GREEN }, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        paint2.setShader(mShader);
        Path path2 = new Path();
        path2.moveTo(mWidth - 30, 0);
        path2.lineTo((int)(mWidth * (3.0/ 4.0)), 0);
        path2.lineTo((int)(mWidth * (1.0/ 2.0)), mHeight - 0);
        path2.lineTo(mWidth - 30, mHeight);
        path2.close();
        canvas.drawPath(path2, paint2);
        RectF rectF2 = new RectF(mWidth - 60, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectF2, 20, 20, paint2);
    }
}
