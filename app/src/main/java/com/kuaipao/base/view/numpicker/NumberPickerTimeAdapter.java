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
import com.kuaipao.utils.LangUtils;

import java.util.Date;

/**
 * Created by pingfu on 16-10-13.
 */
class NumberPickerTimeAdapter extends BaseAdapter {
    private Context mContext;
    private int mDefaultTextColor;
    private int mSelectTextColor;
    private int textSize = View.getDefaultSize(14, TypedValue.COMPLEX_UNIT_SP);
    private int viewHeight;
    private int mSize;
    private boolean isRepeated = false;
    private int maxValue;
    private int mSelectedValue;
    private String format;
    private Date minDate;

    public NumberPickerTimeAdapter(Context context, int height) {
        mContext = context;
        viewHeight = height;
        mDefaultTextColor = context.getResources().getColor(R.color.text_color_gray_66);
        mSelectTextColor = context.getResources().getColor(R.color.title_red_color);
        mSize = 0;
        minDate = new Date();
    }

    public void setData(int maxValue) {
        this.maxValue = maxValue;
        mSize = maxValue + 4;
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
        return position;
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

        view.setText(getItemStr(position - 2));
        view.setTextColor(getTextColor(getItem(position)));

        return view;
    }

    private String getItemStr(int i) {
        if(i < 0) {
            return "";
        } else if( i> maxValue) {
            return "";
        } else {
            Date date = LangUtils.dateByAddingTimeDay(minDate, i);
            String str = LangUtils.formatDate(date, "yyyy年MM月dd日");

            return str;
        }
    }

    private int getTextColor(int value) {
        int color = mDefaultTextColor;
        if(value == mSelectedValue) {
//            color = mSelectTextColor;

        }

        return color;
    }
}
