package com.kuaipao.ui.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

import java.util.Random;

import com.kuaipao.manager.R;


public class RandomTransitionLazyView extends FrameLayout {


    private final Handler mHandler;
    private int[] mResourceIds;
    private LazyImageView mImageView;
    private int mActiveImageIndex = -1;

    private final Random random = new Random();
    private int mSwapMs = 10000;
    private int mFadeInOutMs = 400;

    private float maxScaleFactor = 1.5F;
    private float minScaleFactor = 1.2F;
    private float lastToScale;
    private float lastToTranslationX;
    private float lastToTranslationY;


    private Runnable mSwapImageRunnable = new Runnable() {
        @Override
        public void run() {
            animation();
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs/* - mFadeInOutMs*/);
        }
    };

    public RandomTransitionLazyView(Context context) {
        this(context, null);
    }

    public RandomTransitionLazyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomTransitionLazyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
    }

  /*public void setResourceIds(int... resourceIds) {
    mResourceIds = resourceIds;
    fillImageViews();
  }*/

    public void setImageKey(String key) {
        mImageView.setImageKey(key);

    }

    public void setImageResource(int resId) {
//    mImageView.setImageResource(resId);
        mImageView.setDefaultImageResId(resId);
        mImageView.setImageKey("");
    }

    private void animation() {
        if (mActiveImageIndex == -1) {
            mActiveImageIndex = 0;
            animate(mImageView, true);
            return;
        }
        mActiveImageIndex = 0;

        animate(mImageView, false);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void start(View view, long duration, float fromScale, float toScale,
                       float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
        view.setScaleX(fromScale);
        view.setScaleY(fromScale);
        view.setTranslationX(fromTranslationX);
        view.setTranslationY(fromTranslationY);
        ViewPropertyAnimator propertyAnimator =
                view.animate().translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale)
                        .scaleY(toScale).setDuration(duration);
        propertyAnimator.start();
    }

    private float pickScale() {
        return this.minScaleFactor + this.random.nextFloat()
                * (this.maxScaleFactor - this.minScaleFactor);
    }

    private float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
    }

    public void animate(View view, boolean firstTime) {
        float fromScale = pickScale();
        if (!firstTime) {
            fromScale = lastToScale;
        }
        float toScale = pickScale();
        lastToScale = toScale;
        float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
        if (!firstTime) {
            fromTranslationX = lastToTranslationX;
        }
        float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
        if (!firstTime) {
            fromTranslationY = lastToTranslationY;
        }
        float toTranslationX = pickTranslation(view.getWidth(), toScale);
        lastToTranslationX = toTranslationX;
        float toTranslationY = pickTranslation(view.getHeight(), toScale);
        lastToTranslationY = toTranslationY;
        if (Build.VERSION.SDK_INT >= 14) {
            start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY,
                    toTranslationX, toTranslationY);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startKenBurnsAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mSwapImageRunnable);
    }

    private void startKenBurnsAnimation() {
        mHandler.post(mSwapImageRunnable);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = inflate(getContext(), R.layout.lazy_view_kenburns, this);

        mImageView = (LazyImageView) view.findViewById(R.id.image0);
    }


}
