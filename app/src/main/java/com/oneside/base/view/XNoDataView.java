package com.oneside.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.R;

/**
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-13
 * Time: 15:01
 * Author: pingfu
 * FIXME
 */
public class XNoDataView extends LinearLayout {
    private LinearLayout llContent;
    private ImageView ivHint;
    private TextView tvMessage;
    private Button btnReLoad;
    public View vCourseBottom;

    public XNoDataView(Context context) {
        super(context);
        init();
    }

    public XNoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.v_no_data, this);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        ivHint = (ImageView) findViewById(R.id.iv_hint);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnReLoad = (Button) findViewById(R.id.btn_load_again);
        vCourseBottom = findViewById(R.id.v_course_item);
    }

    public void setReloadClickListener(OnClickListener listener) {
        if(btnReLoad != null) {
            btnReLoad.setOnClickListener(listener);
        }
    }

    public ImageView getNoDataImageView() {
        return ivHint;
    }

    public TextView getMessageView() {
        return tvMessage;
    }

    public Button getReLoadButton() {
        return btnReLoad;
    }

    public void setBackgroundColor(int color) {
        llContent.setBackgroundColor(getContext().getResources().getColor(color));
    }
}
