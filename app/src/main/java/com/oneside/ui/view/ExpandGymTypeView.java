package com.oneside.ui.view;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.oneside.ui.adapter.ExpandListItemAdapter;
import com.oneside.R;

public class ExpandGymTypeView extends LinearLayout {

    private ListView mListView;
    private String[] mGymTypeNames;
    private OnSelectListener mOnSelectListener;
    private ExpandListItemAdapter mAdapter;
    private String mShowTitle = getResources().getString(R.string.traning_class);
    private int mSelectedPosition = 0;

    public String getShowText() {
        return mShowTitle;
    }

    public ExpandGymTypeView(Context context) {
        super(context);
        init(context);
    }

    @SuppressLint("NewApi")
    public ExpandGymTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ExpandGymTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mGymTypeNames = getResources().getStringArray(R.array.bd_gym_types);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expand_gym_type_view, this, true);
        setBackgroundResource(R.color.transparency);
        mListView = (ListView) findViewById(R.id.lv_gyms_type);
        mAdapter =
                new ExpandListItemAdapter(context, Arrays.asList(mGymTypeNames),
                        R.drawable.expand_single_item_selected_bg, R.drawable.expand_single_item_normal_bg,
                        R.color.papaya_primary_color, R.color.text_color_gray_black);

        mAdapter.setSelectedPositionNoNotify(mSelectedPosition);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ExpandListItemAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                if (mOnSelectListener != null) {
                    mShowTitle = mGymTypeNames[position];
                    mOnSelectListener.getValue(position, mGymTypeNames[position]);
                }
            }
        });
    }

    public void updateSelected(String showText) {
        if (showText == null) {
            return;
        }
        for (int j = 0; j < mGymTypeNames.length; j++) {
            if (mGymTypeNames[j].equals(showText.trim())) {
                mAdapter.setSelectedPosition(j);
                mSelectedPosition = j;
                break;
            }
        }
        setListViewSelect();
    }

    public void setListViewSelect() {
        mListView.setSelection(mSelectedPosition);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(int gymType, String strGymTypeName);
    }

}