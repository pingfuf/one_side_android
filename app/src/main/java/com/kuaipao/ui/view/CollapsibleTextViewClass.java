package com.kuaipao.ui.view;

import com.kuaipao.manager.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.view.View.OnClickListener;

public class CollapsibleTextViewClass extends LinearLayout implements OnClickListener {

    /**
     * default text show max lines
     */
    public static final int DEFAULT_MAX_LINE_COUNT = 3;

    private static final int COLLAPSIBLE_STATE_NONE = 0;
    private static final int COLLAPSIBLE_STATE_SHRINK = 1;
    private static final int COLLAPSIBLE_STATE_SPREAD = 2;

    private TextView desc;
//  private TextView descOp;
//  private ImageView decOpImageView;
//  private LinearLayout decLayout;

    private View arrowView;

    //  private String shrinkup;
//  private String spread;
    private int mState;
    private boolean flag;

    public CollapsibleTextViewClass(Context context, AttributeSet attrs) {
        super(context, attrs);
//    shrinkup = context.getString(R.string.desc_shrinkup);
//    spread = context.getString(R.string.desc_spread);
        View view = inflate(context, R.layout.collapsible_textview_merchant, this);

//    view.setPadding(0, -1, 0, 0);
        desc = (TextView) view.findViewById(R.id.desc_tv);
//    descOp = (TextView) view.findViewById(R.id.desc_op_tv);
//    decOpImageView = (ImageView) view.findViewById(R.id.desc_icon);
//    decLayout = (LinearLayout) view.findViewById(R.id.desc_op_layout);
//    // decOpImageView.setOnClickListener(this);
//    // descOp.setOnClickListener(this);
//    decLayout.setOnClickListener(this);
    }

    public CollapsibleTextViewClass(Context context) {
        this(context, null);
    }

    public final void setDesc(CharSequence charSequence, BufferType bufferType, View arrowView) {
        desc.setText(charSequence, bufferType);
        mState = COLLAPSIBLE_STATE_SHRINK;
        flag = false;
        this.arrowView = arrowView;
        requestLayout();
    }

//  public boolean bIsArrowShown() {
//    return mState != COLLAPSIBLE_STATE_NONE ? true : false;
//  }
//
//  public boolean bIsArrowUp() {
//    return mState == COLLAPSIBLE_STATE_SHRINK ? true : false;
//  }

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
                mState = COLLAPSIBLE_STATE_NONE;
//        descOp.setVisibility(View.GONE);
                desc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
//        decLayout.setVisibility(View.GONE);
                if (arrowView != null) {
                    arrowView.setVisibility(View.GONE);
                }
            } else {
                post(new InnerRunnable());

            }
        }
    }

    class InnerRunnable implements Runnable {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void run() {
            if (mState == COLLAPSIBLE_STATE_SPREAD) {
                desc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
                // descOp.setVisibility(View.VISIBLE);
                // decLayout.setVisibility(View.VISIBLE);
//        if (descOp.getVisibility() == View.GONE) {
//          descOp.setVisibility(View.VISIBLE);
//        }
//        descOp.setText(spread);
//        decOpImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_find_more_down));
//        if (arrowView != null) {
//          ObjectAnimator animator =
//              ObjectAnimator.ofFloat(arrowView, "rotation", -90l, 0l).setDuration(300);
//          animator.start();
//        }
                mState = COLLAPSIBLE_STATE_SHRINK;
            } else if (mState == COLLAPSIBLE_STATE_SHRINK) {
                desc.setMaxLines(Integer.MAX_VALUE);
                // decLayout.setVisibility(View.VISIBLE);
//        if (descOp.getVisibility() == View.GONE) {
//          descOp.setVisibility(View.VISIBLE);
//        }
//        descOp.setText(shrinkup);
//        decOpImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_shrinkup));
//        if (arrowView != null) {
//          ObjectAnimator animator =
//              ObjectAnimator.ofFloat(arrowView, "rotation", 0l, -90l).setDuration(300);
//          animator.start();
//        }
                mState = COLLAPSIBLE_STATE_SPREAD;
            }
        }
    }
}
