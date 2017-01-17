package com.oneside.base.view.numpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.oneside.R;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pingfu on 16-8-23.
 */
public class XChosenDateView extends RelativeLayout {
    private XNumberPicker nbYear;
    private XNumberPicker nbMonth;
    private XNumberPicker nbDay;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int minYear;
    private int minMonth;
    private int minDay;
    private int maxYear;
    private int maxMonth;
    private int maxDay;

    OnChosenDateListener mChosenDateListener;
    private Calendar mCalendar;

    public XChosenDateView(Context context) {
        super(context);
        init();
    }

    public XChosenDateView(Context context, AttributeSet attrs) {
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
                setMonthData(mYear);
                setDayData(mYear, mMonth);
                chosenDate();
            }
        });
        nbYear.setFormat("%s年");

        nbMonth.setOnValueChangeListener(new XNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int selection, int value) {
                mMonth = value;
                setDayData(mYear, mMonth);
                chosenDate();
            }
        });
        nbMonth.setFormat("%s月");

        nbDay.setOnValueChangeListener(new XNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int selection, int value) {
                mDay = value;
                chosenDate();
            }
        });
        nbDay.setFormat("%s日");
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth - 1, mDay);
        return calendar.getTime();
    }

    public void setData(Date minDate, Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(minDate);
        minYear = calendar.get(Calendar.YEAR);
        minMonth = calendar.get(Calendar.MONTH) + 1;
        calendar.set(Calendar.YEAR, minYear);
        calendar.set(Calendar.MONTH, minMonth);
        minDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(maxDate);
        maxYear = calendar.get(Calendar.YEAR);
        maxMonth = calendar.get(Calendar.MONTH) + 1;
        maxDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(new Date());
        mYear = calendar.get(Calendar.YEAR);
        setYearData();

        mMonth = calendar.get(Calendar.MONTH) + 1;
        setMonthData(mYear);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        setDayData(mYear, mMonth);

        nbYear.setRepeatAble(false);
        nbMonth.setRepeatAble(true);
        nbDay.setRepeatAble(true);
    }

    private void setYearData() {
        nbYear.setSelectedValue(mYear);
        nbYear.setData(minYear, maxYear);
        nbYear.setSelection(mYear);
    }

    private void setMonthData(int year) {
        int min = 1;
        int max = 12;
        if (year == minYear) {
            min = minMonth;
        } else if (year == maxYear) {
            max = maxMonth;
        }

        if(mMonth < min) {
            mMonth = min;
        }

        if(mMonth > max) {
            mMonth = max;
        }

        nbMonth.setSelectedValue(mMonth);
        nbMonth.setData(min, max);
        nbMonth.setSelection(mMonth);
    }

    private void setDayData(int year, int month) {
        int min = 1;
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth);
        int max = getMonthDay(mCalendar, mMonth);

        if (year == minYear && month == minMonth) {
            max = maxDay;
        } else if (year == maxYear && month == maxMonth) {
            min = minDay;
        }

        if(mDay < min) {
            mDay = min;
        }

        if( mDay > max) {
            mDay = max;
        }

        nbDay.setSelectedValue(mDay);
        nbDay.setData(min, max);
        nbDay.setSelection(mDay);
    }

    private int getMonthDay(Calendar calendar, int mMonth) {
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth - 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public interface OnChosenDateListener {
        void chosenDate(int year, int month, int day);
    }
}
