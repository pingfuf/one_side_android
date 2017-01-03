package com.kuaipao.base.view.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kuaipao.base.view.filter.model.FilterData;
import com.kuaipao.base.view.filter.model.SingleColumnItemData;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 单栏的下拉菜单
 *
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-10
 * Time: 11:47
 * Author: pingfu
 * FIXME
 */
public class SingleColumnItemView extends FilterItemView <List<SingleColumnItemData>> {
    private String mTitle;
    private ListView mListView;
    private SingleColumnListAdapter mListAdapter;
    private List<String> mItems;
    private List<SingleColumnItemData> itemDatas;
    public SingleColumnItemView(Context context, String title) {
        super(context);
        mTitle = title;

        inflate(context, R.layout.filter_single_column_item, this);
        mListView = (ListView) findViewById(R.id.list_item);
        mItems = new ArrayList<String>();
        mListAdapter = new SingleColumnListAdapter(context);
        mListAdapter.setSelectTextColorId(R.color.papaya_primary_color);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleColumnItemData itemData = (SingleColumnItemData) getItemData(position);
                if(itemChosenListener != null) {
                    itemChosenListener.onItemChosen(mTitle, itemData, SINGLE_COLUMN_TYPE);
                }
                if(onItemClickListener != null) {
                    onItemClickListener.onFilterItemClick(mTitle, getItemData(position), SINGLE_COLUMN_TYPE);
                }
                mListAdapter.setChosenPosition(position);
            }
        });
        mListView.setDividerHeight((int)ViewUtils.pr(getContext(), 0.5f));
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void update(List<SingleColumnItemData> items) {
        if(LangUtils.isEmpty(items)) {
            return;
        }

        mItems.clear();
        for(int i = 0; i < items.size(); i++) {
            mItems.add(items.get(i).name);
        }
        itemDatas = items;
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemHeight() {
        return mListAdapter.getCount() * ITEM_SIZE;
    }

    @Override
    public FilterData getItemData(int position) {
        if(itemDatas == null || position >= itemDatas.size()) {
            return null;
        } else {
            return itemDatas.get(position);
        }
    }
}
