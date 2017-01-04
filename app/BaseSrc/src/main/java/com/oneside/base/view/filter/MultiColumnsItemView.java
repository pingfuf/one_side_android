package com.oneside.base.view.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.oneside.base.view.filter.model.FilterData;
import com.oneside.base.view.filter.model.MultiColumnItemData;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-10
 * Time: 12:55
 * Author: pingfu
 * FIXME
 */
public class MultiColumnsItemView extends FilterItemView<List<MultiColumnItemData>> {
    private String mTitle;
    private LinearLayout mLeftContainer;
    private LinearLayout mRightContainer;
    private ListView mLeftListView;
    private ListView mRightListView;
    private SingleColumnListAdapter mLeftListAdapter;
    private SingleColumnListAdapter mRightListAdapter;
    private List<String> mLeftListItems;
    private List<String> mRightListItems;

    private List<MultiColumnItemData> mItemDatas;
    private int mLeftItemsSelectedPosition = 0;

    private int mCurrentLeftSelectedPosition;
    private int mCurrentRightSelectedPosition;

    public MultiColumnsItemView(Context context, String title) {
        super(context);
        mTitle = title;

        inflate(context, R.layout.filter_multi_columns_item, this);
        mLeftContainer = (LinearLayout) findViewById(R.id.ll_left_content);
        mRightContainer = (LinearLayout) findViewById(R.id.ll_right_content);
        mLeftListView = (ListView) findViewById(R.id.lv_left);
        mRightListView = (ListView) findViewById(R.id.lv_right);

        initLeftListView();
        initRightListView();
    }

    private void initLeftListView() {
        mLeftListItems = new ArrayList<String>();
        mLeftListAdapter = new SingleColumnListAdapter(getContext());
        mLeftListView.setAdapter(mLeftListAdapter);
        mLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateRightList(position);
                mLeftListAdapter.setChosenPosition(position);
                if(mCurrentLeftSelectedPosition == position) {
                    mRightListAdapter.setChosenPosition(mCurrentRightSelectedPosition);
                    mLeftListView.post(new Runnable() {
                        @Override
                        public void run() {
                            //setSelection只有在post中才有用？？
                            mRightListView.setSelection(mCurrentRightSelectedPosition);
                        }
                    });
                } else {
                    mRightListView.setSelection(0);
                    mRightListAdapter.setChosenPosition(0);
                }
            }
        });
        mLeftListView.setDividerHeight((int) ViewUtils.pr(getContext(), 0.5f));
        mLeftListAdapter.setSelectTextColorId(R.color.text_color_gray_black);
    }

    private void initRightListView() {
        mRightListItems = new ArrayList<String>();
        mRightListAdapter = new SingleColumnListAdapter(getContext());
        mRightListView.setAdapter(mRightListAdapter);
        mRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MultiColumnItemData itemData = getItemData(mLeftItemsSelectedPosition).items.get(position);
                if(itemChosenListener != null) {
                    itemChosenListener.onItemChosen(mTitle, itemData, MULTI_COLUMN_TYPE);
                }
                if(onItemClickListener != null) {
                    onItemClickListener.onFilterItemClick(mTitle, itemData, MULTI_COLUMN_TYPE);
                }
                mRightListAdapter.setChosenPosition(position);
                mCurrentRightSelectedPosition = position;
                mCurrentLeftSelectedPosition = mLeftItemsSelectedPosition;
            }
        });
        mRightListView.setDividerHeight((int) ViewUtils.pr(getContext(), 0.5f));
        mRightListAdapter.setBackgroundColorId(1);
        mRightListAdapter.setSelectTextColorId(R.color.papaya_primary_color);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void update(List<MultiColumnItemData> items) {
        mItemDatas = items;
        List<String> itemList = new ArrayList<String>();
        for(int i = 0; i < items.size(); i++) {
            itemList.add(items.get(i).name);
        }
        mLeftListItems.clear();
        mLeftListItems.addAll(itemList);
        mLeftListAdapter.notifyDataSetChanged();
        updateRightList(mLeftItemsSelectedPosition);
        mCurrentLeftSelectedPosition = mLeftItemsSelectedPosition;
    }

    @Override
    public int getItemHeight() {
        return mLeftListAdapter.getCount() * ITEM_SIZE;
    }

    @Override
    public MultiColumnItemData getItemData(int position) {
        if(mItemDatas == null || position >= mItemDatas.size()) {
            return null;
        } else {
            return mItemDatas.get(position);
        }
    }

    private void updateRightList(int position) {
        if(LangUtils.isEmpty(mItemDatas)) {
            return;
        }
        mLeftItemsSelectedPosition = position;
        List<MultiColumnItemData> items = mItemDatas.get(position).items;
        mRightListItems.clear();
        for(int i = 0; i < items.size(); i++) {
            mRightListItems.add(items.get(i).name);
        }
        mRightListAdapter.notifyDataSetChanged();
    }
}
