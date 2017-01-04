package com.oneside.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oneside.utils.LangUtils;

import java.util.ArrayList;

/**
 * Created by pingfu on 16-10-26.
 */
public class XFragmentPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> _fragments;

    public XFragmentPageAdapter(ArrayList<Fragment> fragments, FragmentManager fm) {
        super(fm);
        _fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return _fragments == null ? null : _fragments.get(position);
    }

    @Override
    public int getCount() {
        return LangUtils.isEmpty(_fragments) ? 0 : _fragments.size();
    }
}
