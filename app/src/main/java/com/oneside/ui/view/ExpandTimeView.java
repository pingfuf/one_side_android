package com.oneside.ui.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.oneside.ui.adapter.ExpandListItemAdapter;
import com.oneside.utils.LangUtils;
import com.oneside.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ExpandTimeView extends LinearLayout {

    private ListView mListView;
    private ArrayList<String> mListDateText = new ArrayList<String>(7);
    private OnSelectListener mOnSelectListener;
    private ExpandListItemAdapter mAdapter;
    private String mShowTitle = getResources().getString(R.string.time_tip);
    private int mSelectedPosition = 0;

    public String getShowText() {
        return mShowTitle;
    }

    public ExpandTimeView(Context context) {
        super(context);
        init(context);
    }

    @SuppressLint("NewApi")
    public ExpandTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ExpandTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        prepareListDateText(new Date());
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expand_gym_type_view, this, true);
        setBackgroundResource(R.color.transparency);
        mListView = (ListView) findViewById(R.id.lv_gyms_type);
        mAdapter =
                new ExpandListItemAdapter(context, mListDateText,
                        R.drawable.expand_single_item_selected_bg, R.drawable.expand_single_item_normal_bg,
                        R.color.papaya_primary_color, R.color.text_color_gray_black);

        mAdapter.setSelectedPositionNoNotify(mSelectedPosition);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ExpandListItemAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                if (mOnSelectListener != null) {
                    if (position == 0)
                        mShowTitle = mListDateText.get(position);
                    else {
                        SimpleDateFormat formatDay =
                                new SimpleDateFormat(getResources().getString(R.string.fomat_tab2_date2), Locale.getDefault());

                        mShowTitle = formatDay.format(LangUtils.dateByAddingTimeDay(new Date(), position));
                    }
                    mOnSelectListener.onSelectItem(position);
                }
            }
        });
    }

    public void updateTodayDate(Date selectedDate) {
        mShowTitle = getResources().getString(R.string.time_tip);
        prepareListDateText(selectedDate);
        mSelectedPosition = 0;
        mAdapter.setSelectedPosition(mSelectedPosition);
    }

    private void prepareListDateText(Date startDate) {
        mListDateText.clear();
        SimpleDateFormat formatWeekAndDay =
                new SimpleDateFormat(getResources().getString(R.string.fomat_tab2_date_with_week3), Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            String dateStr;
            if (i == 0) {
                dateStr = getResources().getString(R.string.today);
            } else {
                dateStr = formatWeekAndDay.format(LangUtils.dateByAddingTimeDay(startDate, i));
            }
            mListDateText.add(dateStr);
        }
    }

//  public void updateSelected(String showText) {
//    if (showText == null) {
//      return;
//    }
//    for (int j = 0; j < mListDateText.size(); j++) {
//      if (mListDateText.get(j).equals(showText.trim())) {
//        mAdapter.setSelectedPosition(j);
//        mSelectedPosition = j;
//        break;
//      }
//    }
//    setListViewSelect();
//  }


    public void setListViewSelect() {
        mListView.setSelection(mSelectedPosition);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void onSelectItem(int position);
    }

}
