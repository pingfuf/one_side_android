package com.kuaipao.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

public class MeAddressView extends RelativeLayout {
    private ImageView mDelIcon, mAddressIcon, mArrawIcon;
    private TextView mTitleTv, mMainAddressTv, mDetailsAddressTv;
    private View mHorizontalLine;

    public MeAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    public MeAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public MeAddressView(Context context) {
        super(context);
        initViews(context, null);
    }

    private void initViews(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.ui_me_address, this);
        mDelIcon = ViewUtils.find(this, R.id.me_address_del);
        mAddressIcon = ViewUtils.find(this, R.id.me_address_img);
        mArrawIcon = ViewUtils.find(this, R.id.me_address_right_arrow);
        mTitleTv = ViewUtils.find(this, R.id.me_address_title);
        mMainAddressTv = ViewUtils.find(this, R.id.me_address_main);
        mDetailsAddressTv = ViewUtils.find(this, R.id.me_address_details);
        mHorizontalLine = ViewUtils.find(this, R.id.me_address_horizontal_line);
        if (attrs != null) {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.MeAddressView);
            String titleText = typeArray.getString(R.styleable.MeAddressView_title_text);

            String mainText = typeArray.getString(R.styleable.MeAddressView_main_address_text);

            String detailText = typeArray.getString(R.styleable.MeAddressView_detail_address_text);

            boolean delIsVisible =
                    typeArray.getBoolean(R.styleable.MeAddressView_del_icon_visible, false);

            boolean arrawIsVisible =
                    typeArray.getBoolean(R.styleable.MeAddressView_arraw_icon_visible, false);

            Drawable titleIcon = typeArray.getDrawable(R.styleable.MeAddressView_title_src);
            mDelIcon.setVisibility(delIsVisible ? View.VISIBLE : View.GONE);
            mArrawIcon.setVisibility(arrawIsVisible ? View.VISIBLE : View.GONE);
            mAddressIcon.setImageDrawable(titleIcon);
            mTitleTv.setText(titleText);
            mMainAddressTv.setText(mainText);
            mDetailsAddressTv.setText(detailText);

            typeArray.recycle();
        }
    }

    public void setLeftImage(int imageId) {
        mAddressIcon.setImageResource(imageId);
    }

    public void setLeftImage(Drawable imageDrawable) {
        mAddressIcon.setImageDrawable(imageDrawable);
    }

    public void setLeftTitleString(int titleId) {
        mTitleTv.setText(this.getResources().getString(titleId));
    }

    public void setLeftTitleString(String titleStr) {
        if (LangUtils.isNotEmpty(titleStr))
            mTitleTv.setText(titleStr);
    }

    public void setMainAddress(int mainAddressId) {
        mMainAddressTv.setText(getResources().getString(mainAddressId));
    }

    public void setMainAddress(String mainAddressStr) {
        if (LangUtils.isNotEmpty(mainAddressStr))
            mMainAddressTv.setText(mainAddressStr);
    }

    public String getMainAddress() {
        return mMainAddressTv.getText().toString();
    }

    public void setDetailsAddress(int detailAddressId) {
        mDetailsAddressTv.setText(getResources().getString(detailAddressId));
    }

    public String getDetailsAddress() {
        return mDetailsAddressTv.getText().toString();
    }

    public void setDetailsAddress(String detailAddressStr) {
        if (LangUtils.isNotEmpty(detailAddressStr))
            mDetailsAddressTv.setText(detailAddressStr);
    }

    public void setDelIconVisible(int visible) {
        mDelIcon.setVisibility(visible);
    }

    public void setArrawIconVisible(int visible) {
        mArrawIcon.setVisibility(visible);
    }

    public void setHorizontalLineVisible(int visible) {
        mHorizontalLine.setVisibility(visible);
    }
}
