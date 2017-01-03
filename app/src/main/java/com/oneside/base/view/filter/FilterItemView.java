package com.oneside.base.view.filter;

import android.content.Context;
import android.widget.LinearLayout;

import com.oneside.base.view.filter.model.FilterData;
import com.oneside.utils.ViewUtils;

/**
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-10
 * Time: 11:43
 * Author: pingfu
 * FIXME
 */
public abstract class FilterItemView <T> extends LinearLayout {
    //每一个Item的大小
    protected static final int ITEM_SIZE = ViewUtils.rp(45);
    protected Context mContext;
    protected OnItemChosenListener itemChosenListener;
    protected OnFilterItemClickListener onItemClickListener;

    public static final int SINGLE_COLUMN_TYPE = 0;
    public static final int MULTI_COLUMN_TYPE = 1;

    public FilterItemView(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public void setItemChosenListener(OnItemChosenListener listener) {
        itemChosenListener = listener;
    }

    public void setOnItemClickListener(OnFilterItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * 得到标题
     *
     * @return 标题
     */
    public abstract String getTitle();

    public abstract void update(T object);

    public abstract int getItemHeight();

    public abstract FilterData getItemData(int position);

    public interface OnItemChosenListener {
        void onItemChosen(String tag, FilterData data, int dataType);
    }

    interface OnFilterItemClickListener {
        void onFilterItemClick(String tag, FilterData data, int type);
    }
}
