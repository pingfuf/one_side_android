package com.oneside.ui.pic;

import android.content.Context;
import android.text.TextUtils;

import com.oneside.base.view.filter.FilterItemView;
import com.oneside.base.view.filter.FilterView;
import com.oneside.base.view.filter.SingleColumnItemView;
import com.oneside.base.view.filter.model.FilterData;
import com.oneside.base.view.filter.model.SingleColumnData;
import com.oneside.base.view.filter.model.SingleColumnItemData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fupingfu on 2017/1/18.
 */
public class PicFilterManager {
    private static final int MENU_TYPE = 100;
    private static final int CONTENT_TYPE = 101;

    private SingleColumnItemView mMenuTypeView;
    private SingleColumnItemView mContentTypeView;
    private SingleColumnData menuData;
    private SingleColumnData contentData;

    private FilterView mFilterView;
    private Context mContext;
    private PicFilterManager.onFilterChosenListener mFilterChosenListener;
    private HashMap<Integer, Integer> mChosenResultMap;
    public static ArrayList<PicFilterItem> mPicFilterItems;
    static {
        mPicFilterItems = new ArrayList<>();
        mPicFilterItems.add(new PicFilterItem(0, "内涵漫画"));
        mPicFilterItems.add(new PicFilterItem(1, "搞笑图片"));
        mPicFilterItems.add(new PicFilterItem(2, "美女写真"));
    }

    public PicFilterManager(Context context, FilterView filterView) {
        mContext = context;
        mFilterView = filterView;

        initData();
        initFilterView();
    }

    public void setFilterChosenListener(PicFilterManager.onFilterChosenListener filterChosenListener) {
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
        menuData.title = "图片";
        menuData.items = new ArrayList<>();

        SingleColumnItemData itemData1 = new SingleColumnItemData();
        itemData1.type = 0;
        itemData1.itemType = 0;
        itemData1.name = "图片";
        menuData.items.add(itemData1);

        SingleColumnItemData itemData2 = new SingleColumnItemData();
        itemData2.type = 1;
        itemData2.itemType = 1;
        itemData2.name = "小视频";
        menuData.items.add(itemData2);

        contentData = new SingleColumnData();
        contentData.title = "内涵漫画";
        contentData.items = new ArrayList<>();
        for(int i = 0; i < mPicFilterItems.size(); i++) {
            SingleColumnItemData itemData = new SingleColumnItemData();
            itemData.itemType = mPicFilterItems.get(i).type;
            itemData.type = MENU_TYPE;
            itemData.name = mPicFilterItems.get(i).name;
            itemData.position = i;
            contentData.items.add(itemData);
        }

        mChosenResultMap.put(MENU_TYPE, 0);
        mChosenResultMap.put(CONTENT_TYPE, 0);
    }

    private void freshContentTypeView(String tag, FilterData data, int dataType) {
        if(dataType == 0) {
            contentData = new SingleColumnData();
            contentData.title = "短篇";
            contentData.items = new ArrayList<>();
            for(int i = 0; i < mPicFilterItems.size(); i++) {
                SingleColumnItemData itemData = new SingleColumnItemData();
                itemData.itemType = mPicFilterItems.get(i).type;
                itemData.type = MENU_TYPE;
                itemData.name = mPicFilterItems.get(i).name;
                itemData.position = i;
                contentData.items.add(itemData);
            }
        } else {
            contentData = new SingleColumnData();
            contentData.title = "默认";
            contentData.items = new ArrayList<>();
        }

        mContentTypeView.update(contentData.items);
    }

    private void chosenFilter() {
        if(mFilterChosenListener == null) {
            return;
        }

        int menuType = mChosenResultMap.get(MENU_TYPE);
        int contentType = mChosenResultMap.get(CONTENT_TYPE);
        mFilterChosenListener.onFilterChosen(menuType, contentType);
    }


    interface onFilterChosenListener {
        void onFilterChosen(int menuType, int contentType);
    }

    static class PicFilterItem {
        public int type;
        public String name;

        public PicFilterItem(){}

        public PicFilterItem(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }
}
