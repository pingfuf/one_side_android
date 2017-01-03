package com.kuaipao.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.InjectUtils;
import com.kuaipao.manager.CardManager;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pingfu on 16-5-27.
 */
public class XDateSelectedView extends LinearLayout implements View.OnClickListener {
    @From(R.id.layout_yesterday)
    private RelativeLayout rlYesterday;

    @From(R.id.tv_yesterday)
    private TextView tvYesterday;

    @From(R.id.tv_today)
    private TextView tvToday;

    @From(R.id.layout_tomorrow)
    private RelativeLayout rlTomorrow;

    @From(R.id.tv_tomorrow)
    private TextView tvTomorrow;

    private Date today;
    private Date mSelectedDate;
    private OnDateSelectedListener onDateSelectedListener;

    public XDateSelectedView(Context context) {
        super(context);
        init();
    }

    public XDateSelectedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        onDateSelectedListener = listener;
        afterChooseDate(today, false);
    }

    private void init() {
        inflate(getContext(), R.layout.v_date_selected, this);
        InjectUtils.autoInject(this);
        rlYesterday.setOnClickListener(this);
        rlTomorrow.setOnClickListener(this);
        today = new Date();
        mSelectedDate = today;
    }

    private void afterChooseDate(Date date, boolean startRequest) {
        if(date == null) {
            return;
        }
        SimpleDateFormat formatWeekAndDay =
                new SimpleDateFormat(getContext().getString(R.string.fomat_tab2_date_with_week2), Locale.getDefault());
        SimpleDateFormat formatWeek =
                new SimpleDateFormat(getContext().getString(R.string.fomat_tab2_week), Locale.getDefault());
        SimpleDateFormat formatDay =
                new SimpleDateFormat(getContext().getString(R.string.fomat_tab2_date), Locale.getDefault());
        Date today = new Date();
        Date nextWeekOfToday = LangUtils.dateByAddingTimeDay(today, 6);
        if (LangUtils.isSameDay(date, today)) {
            tvToday.setText(getContext().getString(R.string.today));
            rlYesterday.setBackgroundColor(getResources().getColor(R.color.transparency));
        } else {
            if (LangUtils.isSameDay(date, nextWeekOfToday)) {
                rlTomorrow.setBackgroundColor(getResources().getColor(R.color.transparency));
            } else {
                rlTomorrow.setBackgroundResource(R.drawable.item_trans_pressed_gray_bkg);
            }
            tvToday.setText(formatWeekAndDay.format(date));
            rlYesterday.setBackgroundResource(R.drawable.item_trans_pressed_gray_bkg);
        }

        Date lastDate = LangUtils.dateByAddingTimeDay(date, -1);
        tvYesterday.setText(LangUtils.isSameDay(lastDate, today) ? getContext().getString(R.string.today) : formatDay.format(lastDate));

        Date nextDate = LangUtils.dateByAddingTimeDay(date, 1);
        tvTomorrow.setText(LangUtils.isSameDay(nextDate, today) ? getContext().getString(R.string.today) : formatWeek.format(nextDate));
        mSelectedDate = date;

        if(onDateSelectedListener != null && startRequest) {
            onDateSelectedListener.selectDate(date);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlTomorrow) {
            Date nextWeekOfToday = LangUtils.dateByAddingTimeDay(new Date(), 6);
            if (LangUtils.isSameDay(mSelectedDate, nextWeekOfToday)) {
                ViewUtils.showToast(getResources().getString(R.string.not_support_7_after_today), Toast.LENGTH_SHORT);
            } else {
                CardManager.logUmengEvent(Constant.UMENG_EVENT_DATE_PICKER);
                afterChooseDate(LangUtils.dateByAddingTimeDay(mSelectedDate, 1), true);
            }
        } else if (v == rlYesterday) {
            today = new Date();
            if (LangUtils.isSameDay(mSelectedDate, today)) {
                ViewUtils.showToast(getResources().getString(R.string.not_support_before_today),
                        Toast.LENGTH_SHORT);
            } else {
                CardManager.logUmengEvent(Constant.UMENG_EVENT_DATE_PICKER);
                afterChooseDate(LangUtils.dateByAddingTimeDay(mSelectedDate, -1), true);
            }
        }
    }

    public interface OnDateSelectedListener {
        void selectDate(Date date);
    }
}
