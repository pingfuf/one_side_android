package com.oneside.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneside.R;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

/**
 * titleBar对象，公共使用的titleBar
 * <p/>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-05
 * Time: 15:33
 * Author: pingfu
 * FIXME
 */
public class XTitleBar extends RelativeLayout {
    // TitleBar的高度
    public static final int TITLE_BAR_HEIGHT = ViewUtils.rp(50);

    private LinearLayout llLeftContent;
    private LinearLayout llCenterContent;
    private LinearLayout llRightContent;
    private ImageView ivBackPressed;
    private View vNewMessage;
    private TextView tvTitle;
    private TextView tvRight;

    public XTitleBar(Context context) {
        super(context);
        init(context);
    }

    public XTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.v_title_bar, this);
        setBackgroundColor(getResources().getColor(R.color.white));
        llLeftContent = (LinearLayout) findViewById(R.id.ll_left_content);
        llCenterContent = (LinearLayout) findViewById(R.id.ll_center_content);
        ivBackPressed = (ImageView) findViewById(R.id.iv_left);
        vNewMessage = findViewById(R.id.v_new_message);
        vNewMessage.setVisibility(GONE);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llRightContent = (LinearLayout) findViewById(R.id.ll_right_content);
        tvRight = (TextView) findViewById(R.id.tv_right);
    }

    /**
     * 设置标题
     *
     * @param title 标题内容
     */
    public void setTitle(String title) {
        setTitle(title, false);
    }

    /**
     * 设置标题
     *
     * @param title    标题内容
     * @param showBack 是否有返回键
     */
    public void setTitle(String title, boolean showBack) {
        setTitle(title, showBack, (View[]) null);
    }

    /**
     * 设置标题
     *
     * @param title       标题内容
     * @param showBack    是否有返回键
     * @param rightParams 标题右侧内容
     */
    public void setTitle(String title, boolean showBack, View... rightParams) {
        tvTitle.setText(title);
        llLeftContent.setVisibility(showBack ? View.VISIBLE : View.GONE);

        addRightViews(rightParams);
    }

    public LinearLayout getLeftContent() {
        return llLeftContent;
    }

    public LinearLayout getCenterContent() {
        return llCenterContent;
    }

    public TextView getRightTextView() {
        return tvRight;
    }

    private void addRightViews(View... params) {
        if (params == null || params.length == 0) {
            return;
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.leftMargin = ViewUtils.rp(6);
        llRightContent.removeAllViews();
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                llRightContent.addView(params[i], layoutParams);
            }
        }
    }

    /**
     * 是否显示新信息提示
     *
     * @param hasNewMessage 是否显示新信息提示
     */
    public void showOrHideNewMessage(boolean hasNewMessage) {
        vNewMessage.setVisibility(hasNewMessage ? View.VISIBLE : View.GONE);
    }

    public void setViewTitle(View... centerViews) {
        if(LangUtils.isEmpty(centerViews)) {
            return;
        }

        llCenterContent.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -1);
        params.gravity = Gravity.CENTER;
        for(int i = 0; i < centerViews.length; i++) {
            if(centerViews[i] != null) {
                llCenterContent.addView(centerViews[i], params);
            }
        }
    }

    /**
     * 设置标题字体颜色
     *
     * @param color 标题字体颜色
     */
    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * 设置返回键图片
     *
     * @param resId 返回键图片id
     */
    public void setBackPressedImageResource(int resId) {
        ivBackPressed.setImageResource(resId);
    }
}