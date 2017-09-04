package com.oneside.base.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.utils.ViewUtils;
import com.oneside.R;

/**
 * 小熊快跑的默认加载页面
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-06
 * Time: 19:01
 * Author: pingfu
 * FIXME
 */
public class XLoadingView extends LinearLayout {
    private int size;
    private XProcessView vProcess;
    private TextView tvHint;

    public XLoadingView(Context context) {
        super(context);
        init();
    }


    public XLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        initLoadingView();
        initHintView();
        addView(vProcess);
        addView(tvHint);
    }

    private void initLoadingView() {
        vProcess = new XProcessView(getContext());
        size = ViewUtils.rp(80);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(0, 0, 0, 0);
        vProcess.setLayoutParams(params);
        vProcess.startAnim();
    }

    private void initHintView() {
        tvHint = new TextView(getContext());
        tvHint.setText(getContext().getString(R.string.hint_loading));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, ViewUtils.rp(15), 0, 0);
        tvHint.setLayoutParams(params);
        tvHint.setTextColor(getContext().getResources().getColor(R.color.text_color_deep_gray));
        tvHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
    }
}
