package com.kuaipao.ui.view;

import com.kuaipao.manager.R;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.view.View.OnClickListener;

public class CollapsibleBlueTextView extends LinearLayout implements OnClickListener {

    /**
     * default text show max lines
     */
    private static final int DEFAULT_MAX_LINE_COUNT = 3;

    private static final int COLLAPSIBLE_STATE_NONE = 0;
    private static final int COLLAPSIBLE_STATE_SHRINK_UP = 1;
    private static final int COLLAPSIBLE_STATE_SPREAD_DOWN = 2;

    private TextView desc;
    private TextView descOp;
    private LinearLayout decLayout;

    private View arrowView;

    private String strShrinkup;
    private String strSpreadDown;
    private int mState;
    private boolean flag;

    public CollapsibleBlueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        strShrinkup = context.getString(R.string.desc_shrinkup);
        strSpreadDown = context.getString(R.string.desc_spread);
        View view = inflate(context, R.layout.collapsible_blue_textview, this);

        desc = (TextView) view.findViewById(R.id.desc_tv);
        descOp = (TextView) view.findViewById(R.id.desc_op_tv);
        decLayout = (LinearLayout) view.findViewById(R.id.desc_op_layout);
        decLayout.setOnClickListener(this);
    }

    public CollapsibleBlueTextView(Context context) {
        this(context, null);
    }

    public final void setDesc(CharSequence charSequence, BufferType bufferType, View arrowView) {
        desc.setText(charSequence, bufferType);
        mState = COLLAPSIBLE_STATE_SPREAD_DOWN;
        flag = false;
        this.arrowView = arrowView;
        requestLayout();
    }

    @Override
    public void onClick(View v) {
        flag = false;
        requestLayout();
    }

    public void performOnClick() {
        flag = false;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!flag) {
            flag = true;
            if (desc.getLineCount() <= DEFAULT_MAX_LINE_COUNT) {
                mState = COLLAPSIBLE_STATE_SPREAD_DOWN;
                descOp.setVisibility(View.GONE);
                desc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
                decLayout.setVisibility(View.GONE);
            } else {
                post(new InnerRunnable());

            }
        }
    }

    class InnerRunnable implements Runnable {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void run() {
            if (mState == COLLAPSIBLE_STATE_SPREAD_DOWN) {
                desc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
                // descOp.setVisibility(View.VISIBLE);
                // decLayout.setVisibility(View.VISIBLE);
                if (decLayout.getVisibility() == View.GONE) {
                    decLayout.setVisibility(View.VISIBLE);
                }
                if (descOp.getVisibility() == View.GONE) {
                    descOp.setVisibility(View.VISIBLE);
                }
                descOp.setText(strSpreadDown);
                if (arrowView != null) {
                    ObjectAnimator animator =
                            ObjectAnimator.ofFloat(arrowView, "rotation", -90l, 0l).setDuration(300);
                    animator.start();
                }
                mState = COLLAPSIBLE_STATE_SHRINK_UP;
            } else if (mState == COLLAPSIBLE_STATE_SHRINK_UP) {
                desc.setMaxLines(Integer.MAX_VALUE);
                // decLayout.setVisibility(View.VISIBLE);
                if (descOp.getVisibility() == View.GONE) {
                    descOp.setVisibility(View.VISIBLE);
                }
                if (decLayout.getVisibility() == View.GONE) {
                    decLayout.setVisibility(View.VISIBLE);
                }
                descOp.setText(strShrinkup);
                mState = COLLAPSIBLE_STATE_SPREAD_DOWN;
            }
        }
    }
}
