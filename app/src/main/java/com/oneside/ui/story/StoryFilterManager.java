package com.oneside.ui.story;

import android.content.Context;
import android.text.TextUtils;

import com.oneside.base.view.filter.FilterItemView;
import com.oneside.base.view.filter.FilterView;
import com.oneside.base.view.filter.MultiColumnsItemView;
import com.oneside.base.view.filter.SingleColumnItemView;
import com.oneside.base.view.filter.model.FilterData;
import com.oneside.base.view.filter.model.SingleColumnData;
import com.oneside.base.view.filter.model.SingleColumnItemData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fupingfu on 2017/1/11.
 */

public class StoryFilterManager {
    private static final int MENU_TYPE = 100;
    private static final int CONTENT_TYPE = 101;

    private SingleColumnItemView mMenuTypeView;
    private SingleColumnItemView mContentTypeView;
    private SingleColumnData menuData;
    private SingleColumnData contentData;

    private FilterView mFilterView;
    private Context mContext;
    private onFilterChosenListener mFilterChosenListener;
    private HashMap<Integer, Integer> mChosenResultMap;
    public static ArrayList<StoryFilterItem> mStoryFilterItems;
    static {
        mStoryFilterItems = new ArrayList<>();
        mStoryFilterItems.add(new StoryFilterItem(0, "短篇", "dp"));
        mStoryFilterItems.add(new StoryFilterItem(1, "长篇", "cp"));
        mStoryFilterItems.add(new StoryFilterItem(2, "校园", "xy"));
        mStoryFilterItems.add(new StoryFilterItem(3, "医院", "yy"));
        mStoryFilterItems.add(new StoryFilterItem(4, "家里", "jl"));
        mStoryFilterItems.add(new StoryFilterItem(5, "民间", "mj"));
        mStoryFilterItems.add(new StoryFilterItem(6, "灵异", "lj"));
        mStoryFilterItems.add(new StoryFilterItem(7, "原创", "yc"));
        mStoryFilterItems.add(new StoryFilterItem(8, "内涵", "neihanguigushi"));
    }

    public StoryFilterManager(Context context, FilterView filterView) {
        mContext = context;
        mFilterView = filterView;

        initData();
        initFilterView();
    }

    public void setFilterChosenListener(onFilterChosenListener filterChosenListener) {
        mFilterChosenListener = filterChosenListener;
    }

    private void initFilterView() {
        mMenuTypeView = new SingleColumnItemView(mContext, menuData.title);
        mMenuTypeView.update(menuData.items);
        mMenuTypeView.setItemChosenListener(new FilterItemView.OnItemChosenListener() {
            @Override
            public void onItemChosen(String tag, FilterData data, int dataType) {
                freshContentTypeView(tag, data, dataType);
                mChosenResultMap.put(MENU_TYPE, data.type);
                chosenFilter();
            }
        });

        mContentTypeView = new SingleColumnItemView(mContext, contentData.title);
        mContentTypeView.update(contentData.items);
        mContentTypeView.setItemChosenListener(new FilterItemView.OnItemChosenListener() {
            @Override
            public void onItemChosen(String tag, FilterData data, int dataType) {
                SingleColumnItemData itemData = (SingleColumnItemData) data;
                mChosenResultMap.put(CONTENT_TYPE, itemData.itemType);
                chosenFilter();
            }
        });

        mFilterView.addFilterItemView(mMenuTypeView);
        mFilterView.addFilterItemView(mContentTypeView);
    }

    private void initData() {
        mChosenResultMap = new HashMap<>();

        menuData = new SingleColumnData();
        menuData.title = "鬼故事";
        menuData.items = new ArrayList<>();

        SingleColumnItemData itemData1 = new SingleColumnItemData();
        itemData1.type = 0;
        itemData1.itemType = 0;
        itemData1.name = "鬼故事";
        menuData.items.add(itemData1);

        SingleColumnItemData itemData2 = new SingleColumnItemData();
        itemData2.type = 1;
        itemData2.itemType = 1;
        itemData2.name = "笑话";
        menuData.items.add(itemData2);

        contentData = new SingleColumnData();
        contentData.title = "短篇";
        contentData.items = new ArrayList<>();
        for(int i = 0; i < mStoryFilterItems.size(); i++) {
            SingleColumnItemData itemData = new SingleColumnItemData();
            itemData.itemType = mStoryFilterItems.get(i).type;
            itemData.type = MENU_TYPE;
            itemData.name = mStoryFilterItems.get(i).name;
            itemData.position = i;
            contentData.items.add(itemData);
        }

        mChosenResultMap.put(MENU_TYPE, 0);
        mChosenResultMap.put(CONTENT_TYPE, 0);
    }

    private void freshContentTypeView(String tag, FilterData data, int dataType) {
        if(dataType == FilterItemView.SINGLE_COLUMN_TYPE) {
            SingleColumnItemData itemData = (SingleColumnItemData) data;
            if(itemData != null && itemData.itemType == 0) {
                contentData = new SingleColumnData();
                contentData.title = "短篇";
                contentData.items = new ArrayList<>();
                for(int i = 0; i < mStoryFilterItems.size(); i++) {
                    SingleColumnItemData item = new SingleColumnItemData();
                    item.itemType = mStoryFilterItems.get(i).type;
                    item.type = MENU_TYPE;
                    item.name = mStoryFilterItems.get(i).name;
                    item.position = i;
                    contentData.items.add(item);
                }
                mFilterView.updateFilterTitle("短篇", "短篇");
            } else {
                contentData = new SingleColumnData();
                contentData.title = "默认";
                contentData.items = new ArrayList<>();
                contentData.items.clear();
                mFilterView.updateFilterTitle("短篇", "默认");
            }
        }

        mContentTypeView.update(contentData.items);
    }

    private void chosenFilter() {
        if(mFilterChosenListener == null) {
            return;
        }

        int menuType = mChosenResultMap.get(MENU_TYPE);
        int contentType = mChosenResultMap.get(CONTENT_TYPE);
        String contentCode = getFilterContent(menuType, contentType);
        mFilterChosenListener.onFilterChosen(menuType, contentType, contentCode);
    }

    private String getFilterContent(int menuType, int contentTpye) {
        return mStoryFilterItems.get(contentTpye).code;
    }

    interface onFilterChosenListener {
        void onFilterChosen(int menuType, int contentType, String content);
    }

    static class StoryFilterItem {
        public int type;
        public String name;
        public String code;

        public StoryFilterItem(){}

        public StoryFilterItem(int type, String name, String code) {
            this.type = type;
            this.name = name;
            this.code = code;
        }
    }
}
