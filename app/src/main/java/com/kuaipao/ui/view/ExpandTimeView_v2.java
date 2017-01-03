package com.kuaipao.ui.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kuaipao.ui.adapter.ExpandListItemAdapter;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.manager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ExpandTimeView_v2 extends LinearLayout {

    private ListView lvLeft;
    private ListView lvRight;
    private ArrayList<String> mLeftList = new ArrayList<String>();
    private ArrayList<String> mRightList = new ArrayList<String>();
    //  private ArrayList<String> mRightTitleList = new ArrayList<String>();
    private SparseArray<List<String>> mLeftIndexOfRightList =
            new SparseArray<List<String>>();
    private ExpandListItemAdapter mAdapterRight;
    private ExpandListItemAdapter mAdapterLeft;
    private int mLeftChoosePosition = 0;
    private int mLeftClickPosition = 0;
    private int mRightChoosePosition = 0;
    private String mShowTitle = getResources().getString(R.string.time_tip);

    //  private ListView mListView;
//  private ArrayList<String> mListDateText = new ArrayList<String>(7);
    private OnSelectListener mOnSelectListener;
//  private ExpandListItemAdapter mAdapter;
//  private String mShowTitle = getResources().getString(R.string.time_tip);
//  private int mSelectedPosition = 0;

    public String getShowText() {
        return mShowTitle;
    }

    public ExpandTimeView_v2(Context context) {
        super(context);
        init(context);
    }

    @SuppressLint("NewApi")
    public ExpandTimeView_v2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ExpandTimeView_v2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mShowTitle = getResources().getString(R.string.time_tip);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expand_time_view_v2, this, true);
        setBackgroundResource(R.color.transparency);
        lvLeft = (ListView) findViewById(R.id.lv_left);
        lvRight = (ListView) findViewById(R.id.lv_right);
//    setBackgroundResource(R.color.layout_background);
        lvLeft.setBackgroundColor(getResources().getColor(R.color.layout_background));
        lvRight.setBackgroundColor(getResources().getColor(R.color.layout_background));

        mAdapterLeft =
                new ExpandListItemAdapter(context, mLeftList, R.drawable.ic_select_list_bg,
                        R.drawable.expand_left_item_normal_bg, R.color.text_color_gray_black,
                        R.color.text_color_deep_gray);
        mAdapterLeft.setSelectedPositionNoNotify(mLeftChoosePosition);
        lvLeft.setAdapter(mAdapterLeft);
        mAdapterLeft.setOnItemClickListener(new ExpandListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < mLeftIndexOfRightList.size()) {
                    mRightList.clear();
                    mRightList.addAll(mLeftIndexOfRightList.get(position));
//          mRightTitleList.clear();
//          for (BusinessDistrict bd : mRightList) {
//            mRightTitleList.add(bd.getBusinessName());
//          }
                    mLeftClickPosition = position;
                    if (mLeftClickPosition != mLeftChoosePosition) {
                        mAdapterRight.setSelectedPositionNoNotify(-1);
                    } else
                        mAdapterRight.setSelectedPositionNoNotify(mRightChoosePosition);
                    mAdapterRight.notifyDataSetChanged();
                }
            }
        });
        if (mLeftClickPosition < mLeftIndexOfRightList.size()) {
            mRightList.addAll(mLeftIndexOfRightList.get(mLeftClickPosition));
        }
        mAdapterRight =
                new ExpandListItemAdapter(context, mRightList,
                        R.drawable.expand_right_item_selected_bg, R.drawable.expand_right_item_normal_bg,
                        R.color.papaya_primary_color, R.color.text_color_gray_black);
        mAdapterRight.setSelectedPositionNoNotify(mRightChoosePosition);
        lvRight.setAdapter(mAdapterRight);
        mAdapterRight.setOnItemClickListener(new ExpandListItemAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {
                if (LangUtils.isEmpty(mRightList.get(position))) {
                    return;
                }

                mRightChoosePosition = position;
                mLeftChoosePosition = mLeftClickPosition;
                if (mOnSelectListener != null) {
//          if(position == 0)
//            mShowTitle = mLeftList.get(mLeftClickPosition);//mListDateText.get(position);
//          else{
                    SimpleDateFormat formatDay =
                            new SimpleDateFormat(getResources().getString(R.string.fomat_tab2_date2), Locale.getDefault());

                    mShowTitle = formatDay.format(LangUtils.dateByAddingTimeDay(new Date(), mLeftClickPosition));
//          }
                    mOnSelectListener.onSelectItem(mLeftClickPosition, position);
                }

            }
        });
        if (mRightChoosePosition < mRightList.size())
            mShowTitle = mRightList.get(mRightChoosePosition);
        setListViewSelect();

        prepareLeftListDateText(new Date());

    }

    public void updateTodayDate(Date selectedDate) {
        mShowTitle = getResources().getString(R.string.time_tip);
        prepareLeftListDateText(selectedDate);
        mLeftChoosePosition = mRightChoosePosition = 0;
        defaultChooseListView();
    }

    private void prepareLeftListDateText(Date startDate) {
        mLeftList.clear();
        mLeftChoosePosition = mLeftClickPosition = 0;
        SimpleDateFormat formatWeekAndDay =
                new SimpleDateFormat(getResources().getString(R.string.fomat_tab2_date_with_week3), Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            String dateStr;
            if (i == 0) {
                dateStr = getResources().getString(R.string.today);
            } else {
                dateStr = formatWeekAndDay.format(LangUtils.dateByAddingTimeDay(startDate, i));
            }
            mLeftList.add(dateStr);
        }

        mLeftIndexOfRightList.clear();
        for (int i = 0; i < mLeftList.size(); i++) {
            String[] chooseDates = getResources().getStringArray(R.array.gym_choose_date);
            ArrayList<String> listRight = new ArrayList<String>();
            for (String d : chooseDates) {
                listRight.add(d);
            }
            int currentRightCount = listRight.size();
            int currentLeftCount = mLeftList.size();
            for (int j = 0; j < currentLeftCount - currentRightCount; j++) {
                listRight.add("");
            }
            mLeftIndexOfRightList.put(i, listRight);
        }

        mRightChoosePosition = 0;
        mRightList.clear();

        mAdapterLeft.setSelectedPosition(mLeftChoosePosition);
        mAdapterRight.setSelectedPosition(mRightChoosePosition);
    }

    public void defaultChooseListView() {
        mAdapterLeft.setSelectedPosition(mLeftChoosePosition);
        if (mLeftChoosePosition < mLeftIndexOfRightList.size()) {
            mRightList.clear();
            mRightList.addAll(mLeftIndexOfRightList.get(mLeftChoosePosition));
            mLeftClickPosition = mLeftChoosePosition;
            if (mLeftClickPosition != mLeftChoosePosition) {
                mAdapterRight.setSelectedPositionNoNotify(-1);
            } else
                mAdapterRight.setSelectedPositionNoNotify(mRightChoosePosition);
            mAdapterRight.notifyDataSetChanged();
        }
        setListViewSelect();
    }

    public void setListViewSelect() {
        lvLeft.setSelection(mLeftChoosePosition);
        lvRight.setSelection(mRightChoosePosition);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelectItem(int leftPsition, int rightPostion);
    }

}
