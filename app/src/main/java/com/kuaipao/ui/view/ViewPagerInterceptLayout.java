package com.kuaipao.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.kuaipao.utils.LogUtils;
import com.kuaipao.manager.R;

/**
 * Created by MVEN on 16/5/31.
 */
public class ViewPagerInterceptLayout extends RelativeLayout {
    private ViewPager viewPager;

    public ViewPagerInterceptLayout(Context context) {
        super(context);
        init();
    }

    public ViewPagerInterceptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPagerInterceptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    //是否分发到下面的子View
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
//                viewPager = (ViewPager) findViewById(R.id.merchant_home_card_viewpager);
//                ev.setLocation(viewPager.getTop(),viewPager.getLeft());


//                final MotionEvent event = MotionEvent.obtainNoHistory(ev);
//                event.setLocation(0, 0);
//                event.offsetLocation(viewPager.getLeft(), viewPager.getTop());
//                viewPager.dispatchTouchEvent(event);
                break;
        }

        return super.dispatchTouchEvent(ev);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (viewPager != null && viewPager instanceof ViewPager) {
                    LogUtils.e("wwwwwwwww  action move");
                    viewPager.dispatchTouchEvent(event);

                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
