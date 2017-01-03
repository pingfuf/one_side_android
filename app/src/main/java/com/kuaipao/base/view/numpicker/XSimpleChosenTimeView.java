package com.kuaipao.base.view.numpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kuaipao.manager.R;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pingfu on 16-11-4.
 */
public class XSimpleChosenTimeView extends RelativeLayout {
    private XNumberTimePicker nbDate;
    private XNumberPicker nbHour;
    private XNumberPicker nbMinute;

    private int mDate;
    private int mHour;
    private int mMinute;

    private int minMinute;
    private int minHour;

    OnChosenDateListener mChosenDateListener;
    private Calendar mCalendar;

    public XSimpleChosenTimeView(Context context) {
        super(context);
        init();
    }

    public XSimpleChosenTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_chosen_time, this);
        nbDate = (XNumberTimePicker) findViewById(R.id.nb_year);
        nbHour = (XNumberPicker) findViewById(R.id.nb_month);
        nbMinute = (XNumberPicker) findViewById(R.id.nb_day);

        nbDate.setOnValueChangeListener(new XNumberTimePicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int selection, int value) {
                mDate = value - 2;
                if (mDate < 0) {
                    mDate = 0;
                }
                LogUtils.e("dateValue = %s", mDate);
                setHourData(mDate);
                setMinuteData(mDate, mHour);
                chosenDate();
            }
        });

        nbHour.tag = "hour";
        nbMinute.tag = "minute";

        nbHour.setOnValueChangeListener(new XNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int selection, int value) {
                mHour = value;
                LogUtils.e("hour = %s", mHour);
                if (mHour >= 24) {
                    mHour = mHour % 24;
                    if(mDate == 0) {
                        mHour = mHour + minHour;
                    }
                }

                setMinuteData(mDate, mHour);
                chosenDate();
            }
        });
        nbHour.setFormat("%s时");

        nbMinute.setOnValueChangeListener(new XNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(int selection, int value) {
                mMinute = value;
                LogUtils.e("minute = %s", mMinute);
                if (mMinute >= 60) {
                    mMinute = mMinute % 60;
                    if(mDate == 0 && mHour == minHour) {
                        mMinute = mMinute + minMinute;
                    }
                }

                chosenDate();
            }
        });
        nbMinute.setFormat("%s分");

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        minHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        minMinute = mCalendar.get(Calendar.MINUTE);

        setData();
    }

    private void chosenDate() {
        LogUtils.e("chosenDate day = %s, hour = %s, minute = %s", mDate, mHour, mMinute);
        if (mChosenDateListener != null) {
            mChosenDateListener.chosenDate(mDate, mHour, mMinute);
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
        LogUtils.e("mDate = %s, mHour = %s, mMinute = %s", mDate, mHour, mMinute);
        Calendar calendar = Calendar.getInstance();
        Date date = LangUtils.dateByAddingTimeDay(new Date(), mDate);
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, day, mHour, mMinute);

        return calendar.getTime();
    }

    public int getDateNumber() {
        return mDate;
    }

    public void setData() {
        setYearData();
        setHourData(0);
        setMinuteData(0, minHour);

        nbDate.setRepeatAble(false);
        nbHour.setRepeatAble(true);
        nbMinute.setRepeatAble(true);
    }

    public void setSelectedDate(int day, int hour, int minute) {
        mDate = day;

        mHour = hour;
        if (mDate == 0 && hour < minHour) {
            mHour = minHour;
        }

        mMinute = minute;
        if (mDate == 0 && mHour == minHour && minute < minMinute) {
            mMinute = minMinute;
        }

        nbDate.setSelection(day);
        setHourData(mDate);
        nbHour.setSelection(hour);
        setMinuteData(mDate, mHour);
        nbMinute.setSelection(minute);
    }

    private void setYearData() {
        nbDate.setSelectedValue(mDate);
        nbDate.setSelection(mDate);
        nbDate.setData(60);
    }

    private void setHourData(int mDate) {
        nbHour.setSelectedValue(mHour);

        nbHour.setData(0, 23);

        if (mDate == 0) {
            nbHour.setData(minHour, 23);

            if(mHour < minHour) {
                mHour = minHour;
            }
        } else {
            nbHour.setData(0, 23);
        }

        nbHour.setSelection(mHour);
    }

    private void setMinuteData(int date, int hour) {
        nbMinute.setSelectedValue(mMinute);

        nbMinute.setData(0, 59);

        if (date == 0 && hour == minHour) {
            nbMinute.setData(minMinute, 59);

            if(mMinute < minMinute) {
                mMinute = minMinute;
            }

        } else {
            nbMinute.setData(0, 59);
        }

        nbMinute.setSelection(mMinute);
    }

    public interface OnChosenDateListener {
        void chosenDate(int year, int month, int day);
    }
}
