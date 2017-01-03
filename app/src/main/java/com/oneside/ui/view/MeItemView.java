package com.oneside.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

public class MeItemView extends RelativeLayout {

    private TextView mLeftTitleView, mRightSubTitleView;
    private View mUnreadView, mHorizontalLine;
    private ImageView mLeftImageView;

    public MeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public MeItemView(Context context) {
        super(context);
        initViews(context, null);
    }

    public void setLeftImage(int drawableID) {
        mLeftImageView.setImageResource(drawableID);
    }

    public void setLeftText(CharSequence text) {
        mLeftTitleView.setText(text);
    }

    public void setRightText(CharSequence rightText) {
        if (LangUtils.isNotEmpty(rightText)) {
            mRightSubTitleView.setVisibility(View.VISIBLE);
            mRightSubTitleView.setText(rightText);
        }
    }

    public void setRightTextColor(int color) {
        mRightSubTitleView.setTextColor(color);
    }

    public void setUnread(boolean unread) {
        if (!unread) {
            mUnreadView.setVisibility(View.GONE);
        } else {
            mUnreadView.setVisibility(View.VISIBLE);
        }
    }

    public void setHorizontalLineVisible(boolean visible) {
        if (visible) {
            mHorizontalLine.setVisibility(View.VISIBLE);
        } else {
            mHorizontalLine.setVisibility(View.GONE);
        }
    }

    private void initViews(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.ui_me_item, this);
        mLeftTitleView = ViewUtils.find(this, R.id.me_item_iv_left_text);
        mLeftImageView = ViewUtils.find(this, R.id.me_item_iv_left);
        mRightSubTitleView = ViewUtils.find(this, R.id.me_item_tv_right_text);
        mUnreadView = ViewUtils.find(this, R.id.me_item_view_my_msg_circle);
        mHorizontalLine = ViewUtils.find(this, R.id.me_item_view_horizontal_line);
        if (attrs != null) {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.MeItemView);
            String leftText = typeArray.getString(R.styleable.MeItemView_me_left_text);
            setLeftText(leftText);

            String rightText = typeArray.getString(R.styleable.MeItemView_me_right_text);

            setRightText(rightText);
            typeArray.recycle();
        }

    }


}
