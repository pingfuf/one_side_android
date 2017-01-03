package com.kuaipao.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.kuaipao.utils.ViewUtils;

/**
 * Created by MVEN on 16/8/16.
 * <p/>
 * email: magiwen@126.com.
 */


public class LinearGradientLineForNumberPicker extends View {
    public LinearGradientLineForNumberPicker(Context context) {
        super(context);
    }

    public LinearGradientLineForNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearGradientLineForNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, ViewUtils.rp(2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
