package com.kuaipao.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

public class UserInfoItemView extends RelativeLayout {

    private TextView mLeftTitleView, mRightSubTitleView;
    private View mHorizontalLine, mRightArrowReplace;
    private ImageView mRightImageView, mRightArrow;


    public UserInfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public UserInfoItemView(Context context) {
        super(context);
        initViews(context, null);
    }

    public void setRightImage(int drawableID) {
        if (drawableID != -1) {
            mRightImageView.setVisibility(View.VISIBLE);
            mRightImageView.setImageResource(drawableID);
        } else {
            mRightImageView.setVisibility(View.GONE);
        }
    }

    public void setRightImage(Bitmap bitmap) {
        if (bitmap != null) {
            mRightImageView.setVisibility(View.VISIBLE);
            mRightImageView.setImageBitmap(bitmap);
        } else {
            mRightImageView.setVisibility(View.GONE);
        }
    }

    public ImageView getRightImage() {
        return mRightImageView;
    }

    public void setLeftText(CharSequence text) {
        mLeftTitleView.setText(text);
    }

    public void setRightText(CharSequence rightText) {
        if (rightText != null) {
            mRightSubTitleView.setVisibility(View.VISIBLE);
            mRightSubTitleView.setText(rightText);
        }
    }

    public void setHorizontalLineVisible(boolean visible) {
        if (visible) {
            mHorizontalLine.setVisibility(View.VISIBLE);
        } else {
            mHorizontalLine.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightArrowVisible(boolean visible) {
        if (visible) {
            mRightArrow.setVisibility(View.VISIBLE);
            mRightArrowReplace.setVisibility(View.GONE);
        } else {
            mRightArrow.setVisibility(View.GONE);
            mRightArrowReplace.setVisibility(View.VISIBLE);
        }
    }


    private void initViews(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.ui_user_info_item, this);
        mLeftTitleView = ViewUtils.find(this, R.id.user_info_item_tv_left);
        mRightImageView = ViewUtils.find(this, R.id.user_info_item_iv_right);
        mRightSubTitleView = ViewUtils.find(this, R.id.user_info_item_tv_right);
        mHorizontalLine = ViewUtils.find(this, R.id.user_info_item_view_horizontal_line);
        mRightArrow = ViewUtils.find(this, R.id.user_info_item_iv_right_arrow);
        mRightArrowReplace = ViewUtils.find(this, R.id.user_info_item_v_right_arrow_replace);
        if (attrs != null) {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.UserInfoItemView);
            String leftText = typeArray.getString(R.styleable.UserInfoItemView_user_info_left_text);
            setLeftText(leftText);

            String rightText = typeArray.getString(R.styleable.UserInfoItemView_user_info_right_text);

            setRightText(rightText);
            typeArray.recycle();
        }

    }


}
