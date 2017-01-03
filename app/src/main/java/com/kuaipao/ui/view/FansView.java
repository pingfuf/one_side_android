package com.kuaipao.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.kuaipao.manager.CardManager;
import com.kuaipao.manager.R;

/**
 * Created by Magi on 2015/12/2.
 */
public class FansView extends RelativeLayout {
    private Context mContext;

    public FansView(Context context) {
        this(context, null);
    }

    public FansView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FansView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initViews();
    }

    private void initViews() {
        if (null == mContext)
            mContext = CardManager.getApplicationContext();
        View.inflate(mContext, R.layout.ui_fans_view, this);

    }
}
