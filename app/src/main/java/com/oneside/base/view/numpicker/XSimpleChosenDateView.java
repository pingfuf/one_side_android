package com.oneside.base.view.numpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.oneside.R;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pingfu on 16-11-4.
 */
public class XSimpleChosenDateView extends RelativeLayout {
    private XNumberPicker nbYear;
    private XNumberPicker nbMonth;
    private XNumberPicker nbDay;

    private int minYear = 1900;
    private int maxYear = 2099;

    private int mYear;
    private int mMonth;
    private int mDay;

    OnChosenDateListener mChosenDateListener;
    private Calendar mCalendar;

    public XSimpleChosenDateView(Context context) {
        super(context);
        init();
    }

    public XSimpleChosenDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_chosen_date, this);
        mCalendar = Calendar.getInstance();
        nbYear = (XNumberPicker) findViewById(R.id.nb_year);
        nbMonth = (XNumberPicker) findViewById(R.id.nb_month);
        nbDay = (XNumberPicker) findViewById(R.id.nb_day);

        nbYear.setOnValueChangeListener(new XNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int selection, int value) {
                mYear = value;
                setMonthData();
                setDayData();
                chosenDate();
            }
        });
        nbYear.setFormat("%s年");

        nbMonth.setOnValueChangeListener(new XNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int selection, int value) {
                mMonth = value;
                if(mMonth > 12) {
                    mMonth = mMonth % 12;
                }
                setDayData();
                chosenDate();
            }
        });
        nbMonth.setFormat("%s月");

        nbDay.setOnValueChangeListener(new XNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int selection, int value) {
                mDay = value;
                mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.MONTH, mMonth - 1);
                int max = mCalendar.getActualMaximum(Calendar.DATE);
                if(mDay > max) {
                    mDay = mDay % max;
                }

                chosenDate();
            }
        });
        nbDay.setFormat("%s日");

        setData();
    }

    private void chosenDate() {
        if(mChosenDateListener != null) {
            mChosenDateListener.chosenDate(mYear, mMonth, mDay);
        }
    }

    public void setOnChosenDateListener(OnChosenDateListener listener) {
        mChosenDateListener = listener;
    }

    /**
     * 得到当前选中时间
     *
     * @return date
     */
    public Date getValue() {
        LogUtils.e("mYear = %s, mMonth = %s, mDay = %s", mYear, mMonth, mDay);
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth - 1, mDay);
        return calendar.getTime();
    }

    public void setData() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mYear = calendar.get(Calendar.YEAR);
        setYearData();

        mMonth = calendar.get(Calendar.MONTH) + 1;
        setMonthData();

        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        setDayData();

        LogUtils.e("mYear = %s, mMonth = %s, mDay = %s", mYear, mMonth, mDay);
        nbYear.setRepeatAble(false);
        nbMonth.setRepeatAble(true);
        nbDay.setRepeatAble(true);
    }

    public void setSelectedDate(Date selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        setDayData();
        nbYear.setSelection(mYear);
        nbMonth.setSelection(mMonth);
        nbDay.setSelection(mDay);
    }

    private void setYearData() {
        nbYear.setSelectedValue(mYear);
        nbYear.setData(minYear, maxYear);
        nbYear.setSelection(mYear);
    }

    private void setMonthData() {
        nbMonth.setSelectedValue(mMonth);
        nbMonth.setData(1, 12);
        nbMonth.setSelection(mMonth);
    }

    private void setDayData() {
        int min = 1;
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth - 1);
        int max = mCalendar.getActualMaximum(Calendar.DATE);

        nbDay.setSelectedValue(mDay);
        nbDay.setData(min, max);
        nbDay.setSelection(mDay);
    }

    public interface OnChosenDateListener {
        void chosenDate(int year, int month, int day);
    }
}
