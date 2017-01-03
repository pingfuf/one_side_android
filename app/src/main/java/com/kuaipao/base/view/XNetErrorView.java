package com.kuaipao.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuaipao.manager.R;

/**
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-13
 * Time: 15:01
 * Author: pingfu
 * FIXME
 */
public class XNetErrorView extends LinearLayout {
    private ImageView ivHint;
    private TextView tvMessage;
    private Button btnReLoad;

    public XNetErrorView(Context context) {
        super(context);
        init();
    }

    public XNetErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.v_net_error, this);
        ivHint = (ImageView) findViewById(R.id.iv_hint);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnReLoad = (Button) findViewById(R.id.btn_load_again);
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
}
