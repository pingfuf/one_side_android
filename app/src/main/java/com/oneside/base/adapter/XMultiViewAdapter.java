package com.oneside.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pingfu on 16-10-18.
 */
public abstract class XMultiViewAdapter<T> extends XSimpleAdapter {
    public XMultiViewAdapter(Context context) {
        super(context);
    }

    public XMultiViewAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null) {
            view = createView(getItemViewType(position));
        }
        bindView(view, getItemViewType(position), position);

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /**
     * 给View绑定数据
     *
     * @param view     指定的View
     * @param viewType view的类型
     * @param position 数据的位置
     */
    protected abstract void bindView(View view, int viewType, int position);

    /**
     * 根据不同的item类型生成不同的View
     *
     * @param viewType
     * @return
     */
    protected abstract View createView(int viewType);
}
