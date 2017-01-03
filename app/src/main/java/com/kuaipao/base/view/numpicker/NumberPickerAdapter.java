package com.kuaipao.base.view.numpicker;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuaipao.manager.R;
import com.kuaipao.utils.LogUtils;

/**
 * Created by pingfu on 16-10-13.
 */
class NumberPickerAdapter extends BaseAdapter {
    private Context mContext;
    private int mDefaultTextColor;
    private int mSelectTextColor;
    private int textSize = View.getDefaultSize(14, TypedValue.COMPLEX_UNIT_SP);
    private int viewHeight;
    private int mSize;
    private boolean isRepeated = false;
    private int minValue;
    private int maxValue;
    private int mSelectedValue;
    private String format;

    public NumberPickerAdapter(Context context, int height) {
        mContext = context;
        viewHeight = height;
        mDefaultTextColor = context.getResources().getColor(R.color.text_color_gray_66);
        mSelectTextColor = context.getResources().getColor(R.color.title_red_color);
        mSize = 0;
    }

    public void setData(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        mSize = maxValue - minValue + 1;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setRepeatAble(boolean repeatAble) {
        isRepeated = repeatAble;
    }

    public void setDefaultTextColor(int color) {
        mDefaultTextColor = color;
    }

    public void setTextSize(int size) {
        textSize = size;
    }

    public int getSize() {
        return mSize;
    }

    public void setSelectedValue(int selection, boolean requiredFreshing) {
        mSelectedValue = selection;
        if(requiredFreshing) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        int count = mSize + 4;
        if(isRepeated) {
            count = Integer.MAX_VALUE;
        }

        return count;
    }

    @Override
    public Integer getItem(int position) {
        if(!isRepeated) {
            return minValue + position - 2;
        }
        if(mSize <= 0) {
            return minValue;
        }

        return position % mSize + minValue;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = new TextView(mContext);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewHeight);
            view.setLayoutParams(params);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(textSize);
            view.setClickable(false);
            view.setFocusable(false);
        } else {
            view = (TextView) convertView;
        }

        view.setText(getItemStr(getItem(position)));
        view.setTextColor(getTextColor(getItem(position)));

        return view;
    }

    private String getItemStr(int i) {
        if(i < minValue || i > maxValue) {
            return "";
        }

        if(format != null) {
            return String.format(format, i);
        }

        return Integer.toString(i);
    }

    private int getTextColor(int value) {
        int color = mDefaultTextColor;
        if(value == mSelectedValue) {
//            color = mSelectTextColor;
        }

        return color;
    }
}
