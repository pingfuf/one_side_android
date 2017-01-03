package com.kuaipao.base.view.numpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kuaipao.manager.R;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;


/**
 * Created by pingfu on 16-8-23.
 */
public class XNumberPicker extends RelativeLayout {
    private static final int SHOW_ITEM_COUNT = 5;
    private static final int VIEW_ITEM_SIZE = ViewUtils.rp(32);
    private static final int MAX_VALUE = 10000;

    private ListView mListView;
    private View vDivider;
    private NumberPickerAdapter mAdapter;

    private int mCurrentSelectItem;
    private int minValue;
    private int maxValue;
    private boolean isRepeat;

    private OnValueChangeListener mValueChangeListener;

    public String tag = "";

    public XNumberPicker(Context context) {
        super(context);
        init();
    }

    public XNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_number_picker, this);
        mListView = (ListView) findViewById(R.id.lv_items);
        mAdapter = new NumberPickerAdapter(getContext(), VIEW_ITEM_SIZE);
        mListView.setAdapter(mAdapter);
        mListView.setDivider(null);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    mCurrentSelectItem = mListView.getFirstVisiblePosition();
                    int dx = mListView.getChildAt(0).getTop();
                    int selection = mCurrentSelectItem;
                    if(mAdapter.getSize() > 0) {
                        selection = mCurrentSelectItem % mAdapter.getSize();
                    }

                    if (Math.abs(dx) > (2 * VIEW_ITEM_SIZE / 3)) {
                        dx = dx + VIEW_ITEM_SIZE;
                        selection = selection + 1;
                    }
                    adjustItems(dx);
                    mCurrentSelectItem = selection;
                    mAdapter.setSelectedValue(getValue(), true);

                    if (mValueChangeListener != null) {
                        mValueChangeListener.onValueChanged(mCurrentSelectItem, getValue());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    /**
     *
     * @param minValue
     * @param maxValue
     */
    public void setData(int minValue, int maxValue) {
        if(minValue < maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            mAdapter.setData(minValue, maxValue);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setFormat(String format) {
        mAdapter.setFormat(format);
    }

    public void setSelectedValue(int value) {
        mAdapter.setSelectedValue(value, false);
    }

    /**
     * 设置选中位置
     *
     * @param value 选中位置的值
     */
    void setSelection(int value) {
        int currentValue = getValue();
        mCurrentSelectItem = mCurrentSelectItem + value - currentValue;
        if(isRepeat) {
            mCurrentSelectItem = mCurrentSelectItem + MAX_VALUE * mAdapter.getSize() ;
            LogUtils.e("setSelection currentValue = %s, mSelection = %s", value, mCurrentSelectItem);
        }

        mListView.setSelection(mCurrentSelectItem);
    }

    public int getValue() {
        return mAdapter.getItem(mCurrentSelectItem) + SHOW_ITEM_COUNT / 2;
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        mValueChangeListener = listener;
    }

    /**
     * 设置字体颜色
     *
     * @param textColor 字体颜色
     */
    public void setTextColor(int textColor) {
        mAdapter.setDefaultTextColor(textColor);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置字体大小
     *
     * @param size 字体大小
     */
    public void setTextSize(int size) {
        mAdapter.setTextSize(size);
        mAdapter.notifyDataSetChanged();
    }

    private void adjustItems(final int dx) {
        post(new Runnable() {
            @Override
            public void run() {
                mListView.smoothScrollBy(dx, 300);
            }
        });
    }

    /**
     * 设置是否循环
     * @param isRepeated 是否循环
     */
    public void setRepeatAble(boolean isRepeated) {
//        if(mAdapter.getSize() <= MAX_VALUE / 2) {
//            return;
//        }
        this.isRepeat = isRepeated;
        mAdapter.setRepeatAble(isRepeated);
        mAdapter.notifyDataSetChanged();
        int selection = mListView.getSelectedItemPosition();
        if (isRepeated) {
            selection = Integer.MAX_VALUE / 2 + selection;
            mListView.setSelection(selection);
        }
    }

    public interface OnValueChangeListener {
        void onValueChanged(int selection, int value);
    }
}
