package com.oneside.ui.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.oneside.utils.LogUtils;

/**
 * Created by magi on 15/12/28.
 */
public class FitDataViewPager extends ViewPager {


    private float touchX = 0f, downX = 0f, touchY = 0f, downY = 0f;

    //    private float startX;
//    private float lastX;
    private int mTouchSlop;

    public FitDataViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public FitDataViewPager(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        touchX = ev.getX();
        touchY = ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = touchX;
            downY = touchY;
        }
        int currentItem = getCurrentItem();
        PagerAdapter adapter = getAdapter();
        int pageCount = adapter == null ? 0 : adapter.getCount();
        LogUtils.d("2525 downX=%s; touchX=%s; mTouchSlop=%s", downX, touchX, mTouchSlop);
        boolean isScrollVertical = false;
        if ((Math.abs(touchX - downX) > 0 && Math.abs(touchX - downX) < mTouchSlop) ||
                (Math.abs(touchY - downY) >= mTouchSlop && Math.abs((touchY - downY) / (touchX - downX)) > 1.5))
            isScrollVertical = true;

        if (isScrollVertical || (currentItem == 0 && downX <= touchX)
                || (currentItem == pageCount - 1 && downX >= touchX)) {
            getParent().requestDisallowInterceptTouchEvent(false);
        } else
            getParent().requestDisallowInterceptTouchEvent(true);

        return super.dispatchTouchEvent(ev);
    }

    // only intercept slide events
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        boolean isSlide = true;
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                lastX = ev.getX();
//                return super.onInterceptTouchEvent(ev);
//            case MotionEvent.ACTION_MOVE:
//                return true;
//            case MotionEvent.ACTION_UP:
//                getParent().requestDisallowInterceptTouchEvent(false);
//                return super.onInterceptTouchEvent(ev);
//        }
//        LogUtils.e("wwwwwweeeeee");
//        return super.onInterceptTouchEvent(ev);
//        return true;
//    }

//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//
//        int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                startX = ev.getX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (getCurrentItem() == 0) {
//                    if (ev.getX() - startX > mTouchSlop) {
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                    } else {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                } else if (getCurrentItem() == getAdapter().getCount() - 1) {
//                    if (startX - ev.getX() > mTouchSlop) {
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                    } else {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//        }
//
//        return super.onTouchEvent(ev);
//    }
}
