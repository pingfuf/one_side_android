package com.kuaipao.ui.view;

import android.view.View;
import android.widget.GridView;

/**
 * Created by MVEN on 16/6/28.
 * <p/>
 * email: magiwen@126.com.
 */


public class ScrollGridView extends GridView {
    public ScrollGridView(android.content.Context context,
                          android.util.AttributeSet attrs) {
        super(context, attrs);
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
