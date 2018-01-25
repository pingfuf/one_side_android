package com.oneside.ui.study.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.oneside.ui.study.StudyConfig;
import com.oneside.utils.LogUtils;

/**
 * Created by fupingfu on 2017/6/7.
 */

public class StudyLinearLayout extends LinearLayout {
    public StudyLinearLayout(Context context) {
        super(context);
    }

    public StudyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtils.e(StudyConfig.TAG + "StudyLinearLayout onDraw");
        super.onDraw(canvas);
    }


}
