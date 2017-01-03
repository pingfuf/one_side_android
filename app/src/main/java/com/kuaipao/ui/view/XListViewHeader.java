package com.kuaipao.ui.view;

import com.kuaipao.base.view.XProcessView;
import com.kuaipao.manager.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XListViewHeader extends LinearLayout {
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private XProcessView vProcess;
    private AnimationDrawable mFrameAnimation;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    // private Animation mRotateAnim;

    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    public XListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        /* 初始情况，设置下拉刷新view高度为0 */
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) View.inflate(context, R.layout.xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
//        mProgressBar = (ImageView) findViewById(R.id.xlistview_header_progressbar);
//        mFrameAnimation = (AnimationDrawable) mProgressBar.getBackground();
        vProcess = (XProcessView) findViewById(R.id.v_process2);

        mRotateUpAnim =
                new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim =
                new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        // mRotateAnim = new RotateAnimation(0.0f, 360.0f,
        // Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // mRotateAnim.setDuration(1000); // duration in ms
        // mRotateAnim.setRepeatCount(-1); // -1 = infinite repeated
        // mRotateAnim.setFillAfter(true); // keep rotation after animation
        // mRotateAnim.setInterpolator(new LinearInterpolator());
    }

    public void setState(int state) {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING) { /* 显示进度 */
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
//            mProgressBar.setVisibility(View.VISIBLE);
//            mFrameAnimation.start();
            vProcess.setVisibility(VISIBLE);
            vProcess.startAnim();
        } else { // 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
//            mProgressBar.setVisibility(View.INVISIBLE);
//            mFrameAnimation.stop();
            vProcess.setVisibility(INVISIBLE);
            vProcess.stopAnim();
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText("");
                mHintTextView.setVisibility(View.GONE);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText("");
                    mHintTextView.setVisibility(View.GONE);
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setVisibility(View.VISIBLE);
                mHintTextView.setText(R.string.xlistview_header_hint_loading);
                break;
            default:
        }

        mState = state;
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        return mContainer.getLayoutParams().height;
    }
}
