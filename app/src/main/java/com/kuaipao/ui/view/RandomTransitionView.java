package com.kuaipao.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

import com.kuaipao.manager.R;


public class RandomTransitionView extends FrameLayout {


    private final Handler mHandler;
    private int[] mResourceIds;
    private ImageView mImageView;
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
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs/* - mFadeInOutMs */);
        }
    };

    public RandomTransitionView(Context context) {
        this(context, null);
    }

    public RandomTransitionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomTransitionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
    }

    public void setResourceIds(int... resourceIds) {
        mResourceIds = resourceIds;
        fillImageViews();
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

  /*
   * @TargetApi(Build.VERSION_CODES.HONEYCOMB) private void swapImage() { if (mActiveImageIndex ==
   * -1) { mActiveImageIndex = 0; animate(mImageViews[mActiveImageIndex], true); return; }
   * 
   * int inactiveIndex = mActiveImageIndex; mActiveImageIndex = (1 + mActiveImageIndex) %
   * mImageViews.length;
   * 
   * final ImageView activeImageView = mImageViews[mActiveImageIndex];
   * activeImageView.setAlpha(0.0f); ImageView inactiveImageView = mImageViews[inactiveIndex];
   * 
   * animate(activeImageView, false);
   * 
   * AnimatorSet animatorSet = new AnimatorSet(); animatorSet.setDuration(mFadeInOutMs);
   * animatorSet.playTogether(ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
   * ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f)); animatorSet.start(); }
   */


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
        View view = inflate(getContext(), R.layout.view_kenburns, this);

        // mImageViews = new LazyImageView;
        mImageView = (ImageView) view.findViewById(R.id.image0);
        // mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
    }

    private void fillImageViews() {
    /*
     * for (int i = 0; i < mImageViews.length; i++) {
     * mImageViews[i].setImageResource(mResourceIds[i]); }
     */
        mImageView.setImageResource(mResourceIds[0]);
        // mImageViews.setDefaultImageResId(mResourceIds[0]);
    }
}
