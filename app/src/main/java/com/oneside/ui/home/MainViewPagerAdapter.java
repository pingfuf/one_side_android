package com.oneside.ui.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oneside.utils.LangUtils;

import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.ImageViewPagerAdapter;

/**
 * Created by fupingfu on 2017/1/4.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<Fragment> fragments) {
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if(LangUtils.isEmpty(mFragments) || position >= mFragments.size()) {
            return null;
        }

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
