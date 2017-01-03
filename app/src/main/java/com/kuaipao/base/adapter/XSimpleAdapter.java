package com.kuaipao.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kuaipao.manager.R;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 小熊快跑Adapter基类，实现ITEM类型统一的Item
 *
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-06
 * Time: 13:35
 * Author: pingfu
 * FIXME
 */
public abstract class XSimpleAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    protected Context mContext;

    /**
     * 使用内部数据mData，防止外部操作更改data，从而导致UI显示异常，比如，当用户操作的同时，访问网络改变数据，会导致显示异常
     *
     * @param context context
     */
    public XSimpleAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    /**
     * 传入外部data的构造函数，如果外部数据不会随着用户的操作而改变，建议使用这个构造函数
     *
     * @param context 当前context
     * @param data    外部数据
     */
    public XSimpleAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    public void setData(List<T> data) {
        mData.clear();
        if(data != null) {
            mData.addAll(data);
        }
        ViewUtils.runInHandlerThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        int count = 0;
        if(!LangUtils.isEmpty(mData)) {
            count = mData.size();
        }

        return count;
    }

    @Override
    public T getItem(int position) {
        T item = null;
        if(position < getCount()) {
            item = mData.get(position);
        }

        return item;
    }

    /**
     * 得到当前的List数据
     *
     * @return mDatas
     */
    public List<T> getData() {
        return mData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    protected View createItemView(int layoutId) {
        View view = null;
        if(layoutId > 0 && mContext != null) {
            view = View.inflate(mContext, layoutId, null);
        }

        return view;
    }

    protected <T> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }
}
