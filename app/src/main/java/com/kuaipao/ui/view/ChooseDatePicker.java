package com.kuaipao.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnValueChangeListener;

import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZhanTao on 3/15/16.
 */
public class ChooseDatePicker extends LinearLayout {
    public int maxYear;
    public int minYear;
    public int nowMonth;
    public int nowDay = new Date().getDay();

    private Context mContext;
    public NumberPicker mSolarYearSpinner;
    private NumberPicker mSolarMonthSpinner;
    private NumberPicker mSolarDaySpinner;

    private Calendar calendar;
    String single = "0", multiple = "";

    private int mYear, mHour, mMinute, mMonth, mDay, daysOFMonth, daysOfLunarMonth;
    private OnTimeChangedListener mOnTimeChangedListener;

    public ChooseDatePicker(Context context) {
        super(context);
        init(context);
    }

    public ChooseDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChooseDatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        inflate(context, R.layout.choose_date_picker, this);
        init();
//        setDate(new Date());
    }

    public void setDate(Date date) {
        setDate(date, 2);
    }

    public void setDate(Date date, int year) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        maxYear = calendar.get(Calendar.YEAR);
        minYear = maxYear - year;

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        daysOFMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        mSolarYearSpinner.setMaxValue(maxYear);
        mSolarYearSpinner.setMinValue(minYear);
        mSolarYearSpinner.setValue(mYear);
        mSolarMonthSpinner.setMaxValue(12);
        mSolarMonthSpinner.setMinValue(1);
        mSolarMonthSpinner.setValue(mMonth + 1);
        mSolarMonthSpinner.setFormatter(new PickerFormatter(single, multiple));
        mSolarDaySpinner.setMaxValue(daysOFMonth);
        mSolarDaySpinner.setFormatter(new PickerFormatter(single, multiple));

        mSolarDaySpinner.setMinValue(1);
        mSolarDaySpinner.setValue(mDay);

        checkRangeOfDate(calendar);
        mSolarYearSpinner.setOnValueChangedListener(mOnYearChangedListener);
        mSolarMonthSpinner.setOnValueChangedListener(mOnMonthChangedListener);

        mSolarDaySpinner.setOnValueChangedListener(mOnDayChangedListener);
    }

    public void setSelectDate(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        mSolarYearSpinner.setMaxValue(maxYear);
        mSolarYearSpinner.setMinValue(minYear);
        mSolarYearSpinner.setValue(mYear);
        mSolarMonthSpinner.setMaxValue(12);
        mSolarMonthSpinner.setMinValue(1);
        mSolarMonthSpinner.setValue(mMonth + 1);
        mSolarDaySpinner.setValue(mDay);
    }

    public void updateTime(Calendar calendar) {
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        daysOFMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        mSolarYearSpinner.setMaxValue(maxYear);
        mSolarYearSpinner.setMinValue(minYear);
        mSolarYearSpinner.setValue(mYear);
        checkRangeOfDate(calendar);

        mSolarMonthSpinner.setValue(mMonth + 1);

        mSolarDaySpinner.setValue(mDay);
    }

    private void checkRangeOfDate(Calendar calendar) {
        if (calendar.get(Calendar.YEAR) == maxYear) {
            mSolarMonthSpinner.setMaxValue(nowMonth + 1);
            mSolarMonthSpinner.setMinValue(1);
        } else if (calendar.get(Calendar.YEAR) == minYear) {
            mSolarMonthSpinner.setMinValue(nowMonth + 1);
            mSolarMonthSpinner.setMaxValue(12);
        } else {
            mSolarMonthSpinner.setMinValue(1);
            mSolarMonthSpinner.setMaxValue(12);
        }

        if (calendar.get(Calendar.YEAR) == maxYear && calendar.get(Calendar.MONTH) == nowMonth) {
            mSolarDaySpinner.setMaxValue(nowDay);
            mSolarDaySpinner.setMinValue(1);
        } else if (calendar.get(Calendar.YEAR) == minYear && calendar.get(Calendar.MONTH) == nowMonth) {
            mSolarDaySpinner.setMinValue(nowDay);
            mSolarDaySpinner.setMaxValue(daysOFMonth);
        } else {
            mSolarDaySpinner.setMaxValue(daysOFMonth);
            mSolarDaySpinner.setMinValue(1);
        }
    }

    private void init() {
        nowMonth = Calendar.getInstance().get(Calendar.MONTH);
        nowDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        mSolarYearSpinner = (NumberPicker) findViewById(R.id.numberPicker_solar_year);
        mSolarMonthSpinner = (NumberPicker) findViewById(R.id.numberPicker_solar_month);
        mSolarDaySpinner = (NumberPicker) findViewById(R.id.numberPicker_solar_day);
        mSolarYearSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mSolarMonthSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mSolarDaySpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(mSolarYearSpinner,
                            new ColorDrawable(mContext.getResources().getColor(R.color.choose_date_picker_bkg)));
                    pf.set(mSolarMonthSpinner,
                            new ColorDrawable(mContext.getResources().getColor(R.color.choose_date_picker_bkg)));
                    pf.set(mSolarDaySpinner,
                            new ColorDrawable(mContext.getResources().getColor(R.color.choose_date_picker_bkg)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                int height = ViewUtils.rp(1);
                try {
                    pf.set(mSolarYearSpinner,
                            height);
                    pf.set(mSolarMonthSpinner,
                            height);
                    pf.set(mSolarDaySpinner,
                            height);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private EditText findEditText(NumberPicker np) {
        if (null != np) {
            for (int i = 0; i < np.getChildCount(); i++) {
                View child = np.getChildAt(i);

                if (child instanceof EditText) {
                    return (EditText) child;
                }
            }
        }

        return null;
    }

    private void setNumberPickerTextSize(NumberPicker numberPicker, float textSize) {
        EditText et = findEditText(numberPicker);
        et.setFocusable(false);
        et.setGravity(Gravity.CENTER);
        et.setTextSize(textSize);
    }

    private NumberPicker.OnValueChangeListener mOnYearChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            calendar.add(Calendar.YEAR, newVal - oldVal);
            if (calendar.get(Calendar.YEAR) == maxYear) {
                if (calendar.getTime().after(Calendar.getInstance().getTime())) {
                    calendar.setTime(Calendar.getInstance().getTime());
                }
            }

            if (calendar.get(Calendar.YEAR) == minYear) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.YEAR, minYear - maxYear);
                if (calendar.getTime().before(c.getTime())) {
                    calendar.setTime(c.getTime());
                }
            }

            updateTime(calendar);
            mYear = mSolarYearSpinner.getValue();
            onTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnMonthChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            calendar.add(Calendar.MONTH, newVal - oldVal);

            if (calendar.get(Calendar.YEAR) == maxYear) {
                mSolarMonthSpinner.setMaxValue(nowMonth + 1);
                mSolarMonthSpinner.setMinValue(1);
            } else if (calendar.get(Calendar.YEAR) == minYear) {
                mSolarMonthSpinner.setMinValue(nowMonth + 1);
                mSolarMonthSpinner.setMaxValue(12);
                if (newVal == (nowMonth + 1) && oldVal == 12) {
                    calendar.add(Calendar.YEAR, 1);
                }
            } else {
                mSolarMonthSpinner.setMinValue(1);
                mSolarMonthSpinner.setMaxValue(12);
                if (newVal == 1 && oldVal == 12) {
                    calendar.add(Calendar.YEAR, 1);
                } else if (oldVal == 1 && newVal == 12) {
                    calendar.add(Calendar.YEAR, -1);
                }
            }

            updateTime(calendar);
            onTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnDayChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            calendar.add(Calendar.DAY_OF_YEAR, newVal - oldVal);

            if (calendar.get(Calendar.YEAR) == maxYear && calendar.get(Calendar.MONTH) == nowMonth) {
                mSolarDaySpinner.setMaxValue(nowDay);
                mSolarDaySpinner.setMinValue(1);
            } else if (calendar.get(Calendar.YEAR) == minYear && calendar.get(Calendar.MONTH) == nowMonth) {
                mSolarDaySpinner.setMinValue(nowDay);
                mSolarDaySpinner.setMaxValue(daysOFMonth);
            } else {
                mSolarDaySpinner.setMaxValue(daysOFMonth);
                mSolarDaySpinner.setMinValue(1);
                if (newVal == 1 && Math.abs(oldVal - newVal) > 1) {
                    calendar.add(Calendar.MONTH, 1);
                } else if (oldVal == 1 && Math.abs(oldVal - newVal) > 1) {
                    calendar.add(Calendar.MONTH, -1);
                }
            }

            updateTime(calendar);
            mDay = mSolarDaySpinner.getValue();

            onTimeChanged();
        }
    };


    /**
     * 接口回调 参数是当前的View 年月日小时分�
     */
    public interface OnTimeChangedListener {
        void onTimeChanged(int year, int month, int day);
    }

    /**
     * 对外的公开方法
     */
    public void setOnTimeChangedListener(OnTimeChangedListener callback) {
        mOnTimeChangedListener = callback;
    }

    public void onTimeChanged() {
        if (mOnTimeChangedListener != null && calendar != null) {
            mOnTimeChangedListener.onTimeChanged(
                    mSolarYearSpinner.getValue(),
                    mSolarMonthSpinner.getValue(),
                    mSolarDaySpinner.getValue());
        }
    }

    public static class PickerFormatter implements Formatter {
        private String mSingle;
        private String mMultiple;

        public PickerFormatter(String single, String multiple) {
            mSingle = single;
            mMultiple = multiple;
        }

        @Override
        public String format(int num) {
            if (num < 10) {
                return mSingle + String.valueOf(num);

            }

            return String.valueOf(num)/* + " " + mMultiple */;
        }
    }
}
