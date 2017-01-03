package com.kuaipao.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kuaipao.manager.R;
import com.kuaipao.utils.LogUtils;

import java.util.Random;

/**
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-09-21
 * Time: 13:18
 * Author: pingfu
 * FIXME
 */
public class XProcessTempView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private int mHeight;
    private int mWidth;
    private int mProcess;
    private Bitmap mBackBitmap;
    private Bitmap mBitmap;
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsRunning;
    private boolean isFinished;
    private Rect srcRect;
    private Rect dstRect;
    private int backgroundColor;
    private Thread mThread;

    public XProcessTempView(Context context) {
        super(context);
        init();
    }

    public XProcessTempView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        isFinished = false;
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(this);

        mBackBitmap = getBitmapById(R.drawable.ic_loading_gray);
        mBitmap = getBitmapById(R.drawable.ic_loading_red);
        backgroundColor = getResources().getColor(R.color.layout_background);
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsRunning = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.e("surfaceDestroyed ###########");
        mIsRunning = false;
    }

    @Override
    public void run() {
        while (!isFinished) {
            if(!mIsRunning) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!mHolder.getSurface().isValid()) {
                continue;
            }
            mCanvas = mHolder.lockCanvas();
            drawView(mCanvas);
            try {
                Thread.sleep(30);
                mProcess = (mProcess + 1) % 100;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mWidth == 0 || mHeight == 0) {
            mWidth = w;
            mHeight = h;

            srcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            dstRect = new Rect(0, 0, mWidth, mHeight);
        }
    }

    /**
     * 设置加载进度：0到100内的整数数
     *
     * @param process 加载进度
     */
    public void setProcess(int process) {
        mProcess = process;
    }

    public void startProcess() {
        mIsRunning = true;
        mProcess = 0;
    }

    public void stopProcess() {
        mIsRunning = false;
    }

    protected void drawView(Canvas canvas) {
        if (canvas == null) {
            return;
        }

        Paint backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setColor(backgroundColor);
        canvas.drawRect(dstRect, backPaint);
        canvas.drawBitmap(mBackBitmap, srcRect, dstRect, backPaint);

        canvas.save();
        //画贝塞尔曲线
        Path mPath = new Path();
        int y;
        int a = 5;
        int top = getStartHeight();

        //设置Path的起点
        mPath.moveTo(0, top);

        //贝叶斯曲线
        for (int i = 0; i <= mWidth; i++) {
            y = getWaveSize(a, i) + getWaveHeight() + top;
            mPath.quadTo(i, y, i + 1, y);
        }
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.lineTo(0, top);
        mPath.close();

        canvas.clipPath(mPath);
        Paint topPaint = new Paint();
        topPaint.setAntiAlias(true);
        canvas.drawBitmap(mBitmap, srcRect, dstRect, topPaint);
        canvas.restore();
    }

    private int getStartHeight() {
        int top = (int) (mHeight * (1 - (float) mProcess / 100.0f));
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

    private Bitmap getBitmapById(int resId) {
        return ((BitmapDrawable) ContextCompat.getDrawable(getContext(), resId)).getBitmap();
    }
}
