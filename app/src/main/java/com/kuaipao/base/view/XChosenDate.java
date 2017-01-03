package com.kuaipao.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.InjectUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pingfu on 16-8-15.
 */
public class XChosenDate extends LinearLayout {
    public static final int YEAR_TYPE_BEFOR = 1;
    public static final int MONTH_TYPE_BEFOR = 2;
    public static final int DAY_TYPE_BEFOR = 3;
    public static final int YEAR_TYPE_AFTER = 4;
    public static final int MONTH_TYPE_AFTER = 5;
    public static final int DAY_TYPE_AFTER = 6;

    private int dividerHeight;

    @From(R.id.ll_content)
    private LinearLayout llContent;

    @From(R.id.np_year)
    private NumberPicker npYear;

    @From(R.id.np_month)
    private NumberPicker npMonth;

    private NumberPicker npDay;

    private Date mDate;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int minYear;
    private int maxYear;
    private int minMonth;
    private int maxMonth;
    private int minDay;
    private int maxDay;

    private int maxMonthDay;

    private OnDateChosenHandler mChosenHandler;

    private HashMap<Integer, HashMap<Integer, Integer>> mDataMap;

    public XChosenDate(Context context) {
        super(context);
        init();
    }

    public XChosenDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_choose_date, this);
        InjectUtils.autoInject(this);

        dividerHeight = ViewUtils.rp(1);

        npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                chosenDate(npYear.getValue(), npMonth.getValue(), picker.getValue());
            }
        });

        npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int value = 0;
                if(npDay != null && npDay.getParent() == llContent) {
                    value = npDay.getValue();
                    llContent.removeView(npDay);
                }
                npDay = createNumberPicker();
                llContent.addView(npDay);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                int max = getMonthDay(calendar, mMonth);
                if(oldVal > newVal) {
                    //选择小的日期
                    setDayData(npDay, minDay, max);
                    if(value < minDay) {
                        value = minDay;
                    }
                } else {
                    setDayData(npDay, 1, maxDay - (max - minDay));
                    if(value > maxDay - (max - minDay)) {
                        value = maxDay - (max - minDay);
                    }
                }
                npDay.setValue(value);
                chosenDate(npYear.getValue(), npMonth.getValue(), npDay.getValue());
            }
        });

        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        setNumberPickerDivider(npYear, R.color.choose_date_picker_bkg, dividerHeight);
        setNumberPickerDivider(npMonth, R.color.choose_date_picker_bkg, dividerHeight);
    }

    public void setData(Date date, int value, int valueType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mDate = date;
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        npYear.setMaxValue(mYear);
        npYear.setMinValue(mYear);
        npYear.setDisplayedValues(new String[]{"" + mYear + "年"});
        npDay = createNumberPicker();
        llContent.addView(npDay);
        switch (valueType) {
            case YEAR_TYPE_BEFOR:
                minYear = mYear - value;
                maxYear = mYear;
                minDay = maxDay = minMonth = maxMonth = -1;
                break;
            case YEAR_TYPE_AFTER:
                minYear = mYear;
                maxYear = minYear + value;
                minDay = maxDay = minMonth = maxMonth = -1;
                break;
            case MONTH_TYPE_BEFOR:
                minYear = maxYear = mYear;
                maxMonth = mMonth;
                minMonth = maxMonth - value;
                break;
            case MONTH_TYPE_AFTER:
                break;
            case DAY_TYPE_BEFOR:
                break;
            case DAY_TYPE_AFTER:
                minYear = maxYear = mYear;
                minMonth = mMonth;
                maxMonth = getMaxMonth(calendar, value);
                setMonthData(minMonth + 1, maxMonth + 1);

                minDay = calendar.get(Calendar.DAY_OF_MONTH);
                maxDay = value;
                int max = getMonthDay(calendar, mMonth);
                if(minDay + value > value) {
                    setDayData(npDay, minDay, max);
                } else {
                    setDayData(npDay, minDay, minDay + value);
                }

                break;
        }
        updateData(date, value, valueType);
    }

    public void setDateChosenHandler(OnDateChosenHandler handler) {
        mChosenHandler = handler;
    }

    private String getStringValue(int value) {
        return value < 10 ? "0" + value : "" + value;
    }

    private int getMaxMonth(Calendar calendar, int value) {
        int maxDay = getMonthDay(calendar, mMonth);
        if(mDay + value > maxDay) {
            return minMonth + 1;
        }

        return minMonth;
    }

    private int getMonthDay(Calendar calendar, int mMonth) {
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    private void setMonthData(int minMonth, int maxMonth) {
        npMonth.setMinValue(minMonth);
        npMonth.setMaxValue(maxMonth);
        String[] monthArray = new String[] {
                getStringValue(minMonth) + "月",
                getStringValue(maxMonth) + "月"
        };
        npMonth.setDisplayedValues(monthArray);
        npMonth.setValue(minMonth);
    }

    private void setDayData(NumberPicker picker, int minDay, int maxDay) {
        LogUtils.e("setDayData, minDay = %s, maxDay = %s", minDay, maxDay);
        picker.setMinValue(minDay);
        picker.setMaxValue(maxDay);

        String dayValues[] = new String[maxDay - minDay + 1];
        for(int i = minDay; i <= maxDay; i++) {
            dayValues[i - minDay] = getStringValue(i) + "日";
        }
        picker.setDisplayedValues(dayValues);
        picker.setValue(minDay);
    }

    private void updateData(Date date, int value, int valueType) {
        if(mDataMap == null) {
            mDataMap = new HashMap<>();
        }

        switch (valueType) {
            case YEAR_TYPE_BEFOR:
                break;
            case YEAR_TYPE_AFTER:
                break;
            case MONTH_TYPE_BEFOR:
                break;
            case MONTH_TYPE_AFTER:
                break;
            case DAY_TYPE_BEFOR:
                break;
            case DAY_TYPE_AFTER:

                break;
        }
    }

    private NumberPicker createNumberPicker() {
        final NumberPicker numberPicker = new NumberPicker(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.rp(70), ViewUtils.rp(130));
        numberPicker.setLayoutParams(params);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                chosenDate(npYear.getValue(), npMonth.getValue(), numberPicker.getValue());
            }
        });

        setNumberPickerDivider(numberPicker, R.color.choose_date_picker_bkg, dividerHeight);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        return numberPicker;
    }

    private void chosenDate(int year, int month, int day) {
        if(mChosenHandler != null) {
            mChosenHandler.chosenDate(year, month, day);
        }
    }

    private void setNumberPickerDivider(NumberPicker picker, int colorId, int height) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        int i = 0;
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable drawable = new ColorDrawable(getResources().getColor(colorId));
                    pf.set(picker, drawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                i++;
            } else if(pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, height);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                i++;
            }
            if(i >= 2) {
                break;
            }
        }
    }

    public interface OnDateChosenHandler {
        void chosenDate(int year, int month, int day);
    }
}
