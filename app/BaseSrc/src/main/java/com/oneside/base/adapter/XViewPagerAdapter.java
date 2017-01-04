package com.oneside.base.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.oneside.utils.LogUtils;

import java.util.List;

/**
 * Created by pingfu on 16-6-29.
 */
public class XViewPagerAdapter extends PagerAdapter {
    private List<View> mViews;

    public XViewPagerAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = getViewAtPosition(position);
//        if(view != null && view.getParent() != null && view.getParent() == container) {
//            container.removeView(view);
//        }
    }

    public int getSize() {
        return mViews != null ? mViews.size() : 0;
    }

    @Override
    public int getCount() {
        int count = 0;
        if(mViews != null) {
            if(mViews.size() > 1) {
                count = Integer.MAX_VALUE;
            } else {
                count = mViews.size();
            }
        }
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getViewAtPosition(position);
        if(view != null && view.getParent() != null && view.getParent() instanceof ViewGroup) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    private View getViewAtPosition(int position) {
        if(getSize() == 0) {
            return null;
        }

        return mViews.get(position % getSize());
    }
}
