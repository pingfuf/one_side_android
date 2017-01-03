package com.oneside.ui.home;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.oneside.base.view.filter.FilterItemView;
import com.oneside.base.view.filter.FilterView;
import com.oneside.base.view.filter.MultiColumnsItemView;
import com.oneside.base.view.filter.SingleColumnItemView;
import com.oneside.base.view.filter.model.FilterData;
import com.oneside.base.view.filter.model.MultiColumnData;
import com.oneside.base.view.filter.model.MultiColumnItemData;
import com.oneside.base.view.filter.model.SingleColumnData;
import com.oneside.base.view.filter.model.SingleColumnItemData;
import com.oneside.model.beans.XBusinessArea;
import com.oneside.model.beans.XBusinessDistrict;
import com.oneside.model.beans.XBusinessFilterData;
import com.oneside.model.response.BusinessListResponse;
import com.oneside.model.response.CardTypeFilterResponse;
import com.oneside.utils.LangUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.CheckForNull;

import static com.oneside.utils.LangUtils.isEmpty;

/**
 * 场馆列表Filter管理
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-10
 * Time: 10:20
 * Author: pingfu
 * FIXME
 */
public class GymFilterManager {
    public static final int TIME_TYPE = 100;
    public static final int CARD_TYPE = 110;
    public static final int BUSINESS_TYPE = 120;
    public static final int SORT_TYPE = 130;

    public static final String CART_TYPE_TAG = "全部";
    public static final String BUSINESS_TYPE_TAG = "商圈";
    public static final String SORT_TYPE_TAG = "智能筛选";
    public static final String NEARBY = "附近";
    public static final String ALL = "全部";

    private Context mContext;
    private FilterView mFilterView;

    private FilterItemView mCardTypeFilterItemView;
    private FilterItemView mBusinessTypeFilterItemView;
    private FilterItemView mSortTypeFilterItemView;
    private static SingleColumnData mCardTypeFilterData;
    private static SingleColumnData mSortTypeFilterData;
    private Map<String, FilterData> mChosenFilterDataMap;

    static {
        //初始化Filter数据
        //没想到，卡片类型选项写死
        mCardTypeFilterData = new SingleColumnData();
        mCardTypeFilterData.title = CART_TYPE_TAG;
        mCardTypeFilterData.items = new ArrayList<SingleColumnItemData>();
        SingleColumnItemData itemData1 = new SingleColumnItemData();
        itemData1.type = CARD_TYPE;
        itemData1.name = "全部";
        itemData1.position = 0;
        itemData1.itemType = -1;  //参数意义见Wiki
        mCardTypeFilterData.items.add(itemData1);

        SingleColumnItemData itemData2 = new SingleColumnItemData();
        itemData2.type = CARD_TYPE;
        itemData2.name = "三次通卡";
        itemData2.position = 1;
        itemData2.itemType = 1;
        mCardTypeFilterData.items.add(itemData2);

        SingleColumnItemData itemData3 = new SingleColumnItemData();
        itemData3.type = CARD_TYPE;
        itemData3.name = "有氧空间";
        itemData3.position = 2;
        itemData3.itemType = 2;
        mCardTypeFilterData.items.add(itemData3);

        //更没想到，排序类型也写死？要后端干嘛？？？？
        mSortTypeFilterData = new SingleColumnData();
        mSortTypeFilterData.title = SORT_TYPE_TAG;
        mSortTypeFilterData.items = new ArrayList<SingleColumnItemData>();
        SingleColumnItemData itemData4 = new SingleColumnItemData();
        itemData4.type = SORT_TYPE;
        itemData4.name = "全部";
        itemData4.position = 0;
        itemData4.itemType = -1;
        mSortTypeFilterData.items.add(itemData4);
        SingleColumnItemData itemData5 = new SingleColumnItemData();
        itemData5.type = SORT_TYPE;
        itemData5.name = "月卡";
        itemData5.position = 1;
        itemData5.itemType = 4;
        mSortTypeFilterData.items.add(itemData5);

        SingleColumnItemData itemData6 = new SingleColumnItemData();
        itemData6.type = SORT_TYPE;
        itemData6.name = "单次卡";
        itemData6.position = 2;
        itemData6.itemType = 3;
        mSortTypeFilterData.items.add(itemData6);

        SingleColumnItemData itemData7 = new SingleColumnItemData();
        itemData7.type = SORT_TYPE;
        itemData7.name = "年卡";
        itemData7.position = 3;
        itemData7.itemType = 5;
        mSortTypeFilterData.items.add(itemData7);
    }

    //FilterView的回调函数接口
    private FilterItemView.OnItemChosenListener mItemChosenListener = new FilterItemView.OnItemChosenListener() {
        @Override
        public void onItemChosen(String tag, FilterData data, int dataType) {
            if(mChosenFilterDataMap != null) {
                mChosenFilterDataMap.put(tag, data);
            }
            onFilterItemChosen(data, dataType);
        }
    };

    private MultiColumnData mBusinessFilterData;
    private List<XBusinessDistrict> mBusinessDistrictList;
    private ChosenFilterData mChosenFilterData;
    private IFilterChosenHandler mFilterChosenHandler;

    public GymFilterManager(Context context, FilterView filterView) {
        mContext = context;
        mFilterView = filterView;
        mBusinessFilterData = new MultiColumnData();
        mBusinessFilterData.items = new ArrayList<>();
        mBusinessFilterData.title = BUSINESS_TYPE_TAG;
        mChosenFilterDataMap = new HashMap<>();
        mChosenFilterData = new ChosenFilterData();
    }

    public JSONObject getLocalFilterData(String city, boolean hideNoGyms) {
        XBusinessFilterData filterData = new XBusinessFilterData(city, hideNoGyms);
        return filterData.getLocalSavedData();
    }

    public void saveNewestFilterData(String city, boolean hideNoGyms, JSONObject data) {
        if(data == null) {
            return;
        }
        XBusinessFilterData filterData = new XBusinessFilterData(city, hideNoGyms);
        filterData.saveDistrictData(data);
    }

    /**
     * 设置Filter数据
     */
    public void setData() {
        mFilterView.refreshTitle();
        mCardTypeFilterItemView = createSingleColumnItemView(mCardTypeFilterData);
        mBusinessTypeFilterItemView = createMultiColumnItemView(mBusinessFilterData);
        mSortTypeFilterItemView = createSingleColumnItemView(mSortTypeFilterData);
        mFilterView.addFilterItemView(mCardTypeFilterItemView, 0.3f);
        mFilterView.addFilterItemView(mBusinessTypeFilterItemView, 0.4f);
        mFilterView.addFilterItemView(mSortTypeFilterItemView, 0.3f);
    }

    public void setFilterChosenHandler(IFilterChosenHandler chosenHandler) {
        mFilterChosenHandler = chosenHandler;
    }

    /**
     * 更新卡片类型Filter下拉菜单
     *
     * @param cardTypeItems 卡片类型接口返回的数据
     */
    public void updateCardTypeFilter(List<CardTypeFilterResponse.CardTypeFilterItem> cardTypeItems) {
        if(LangUtils.isEmpty(cardTypeItems)) {
            return;
        }
        mCardTypeFilterData.items.clear();
        for(int i = 0; i < cardTypeItems.size(); i++) {
            CardTypeFilterResponse.CardTypeFilterItem item = cardTypeItems.get(i);
            if(item == null) {
                continue;
            }

            SingleColumnItemData data = new SingleColumnItemData();
            data.name = item.label;
            data.position = i;
            data.type = CARD_TYPE;
            data.itemType = item.id;
            mCardTypeFilterData.items.add(data);
        }
        mCardTypeFilterItemView.update(mCardTypeFilterData.items);
    }

    /**
     * 更新商圈类型下拉菜单
     *
     * @param responseResult 商圈类型接口返回结果
     */
    public void updateFilter(BusinessListResponse responseResult) {
        if(responseResult == null || responseResult.data == null) {
            return;
        }
        updateFilterData(responseResult);
        mBusinessTypeFilterItemView.update(mBusinessFilterData.items);
    }

    /**
     * 更新场馆类型下拉菜单
     *
     * @param sortTypeItems 场馆类型接口返回结果
     */
    public void updateSortTypeFilter(List<CardTypeFilterResponse.CardTypeFilterItem> sortTypeItems) {
        mSortTypeFilterData.items.clear();
        for(int i = 0; i < sortTypeItems.size(); i++) {
            CardTypeFilterResponse.CardTypeFilterItem item = sortTypeItems.get(i);
            if(item == null) {
                continue;
            }

            SingleColumnItemData data = new SingleColumnItemData();
            data.name = item.label;
            data.position = i;
            data.type = CARD_TYPE;
            data.itemType = item.id;
            mSortTypeFilterData.items.add(data);
        }
        mSortTypeFilterItemView.update(mSortTypeFilterData.items);
    }

    public void hideFilterView() {
        if(mFilterView != null) {
            mFilterView.hidePopupWindow();
        }
    }

    private void updateFilterData(BusinessListResponse result) {
        updateBusinessDistricts(result);

        if(mBusinessFilterData == null) {
            mBusinessFilterData = new MultiColumnData();
            mBusinessFilterData.items = new ArrayList<>();
        }
        mBusinessFilterData.items.clear();
        for(int i = 0; i < mBusinessDistrictList.size(); i++) {
            XBusinessDistrict district = mBusinessDistrictList.get(i);
            MultiColumnItemData itemData = new MultiColumnItemData();
            itemData.items = new ArrayList<>();
            itemData.name = district.name;
            itemData.x = 0;
            itemData.y = i;

            if(!LangUtils.isEmpty(district.businessAreas)) {
                for(int j = 0; j < district.businessAreas.size(); j++) {
                    XBusinessArea businessArea = district.businessAreas.get(j);
                    if(businessArea != null) {
                        MultiColumnItemData data = new MultiColumnItemData();
                        data.name = businessArea.name;
                        data.x = i;
                        data.y = j;
                        data.parentData = itemData;
                        itemData.items.add(data);
                    }
                }
            }
            mBusinessFilterData.items.add(itemData);
        }
    }

    /**
     * 生成单列下拉菜单
     *
     * @param itemData 下拉菜单数据
     * @return
     */
    private FilterItemView createSingleColumnItemView(SingleColumnData itemData) {
        SingleColumnItemView itemView = new SingleColumnItemView(mContext, itemData.title);
        itemView.update(itemData.items);
        itemView.setItemChosenListener(mItemChosenListener);

        return itemView;
    }

    /**
     * 生成多列下拉菜单
     *
     * @param itemData 多列下拉菜单数据
     * @return
     */
    private FilterItemView createMultiColumnItemView(MultiColumnData itemData) {
        MultiColumnsItemView itemView = new MultiColumnsItemView(mContext, itemData.title);
        itemView.update(itemData.items);
        itemView.setItemChosenListener(mItemChosenListener);

        return itemView;
    }

    /**
     * 用户点击Filter的某一项
     *
     * @param itemData 点击的数据
     * @param type     点击的数据类型
     */
    private void onFilterItemChosen(FilterData itemData, int type) {
        if(itemData == null) {
            return;
        }

        //卡片类型筛选缓存
        FilterData cardTypeData = mChosenFilterDataMap.get(CART_TYPE_TAG);
        if(cardTypeData != null) {
            mChosenFilterData.cardType = cardTypeData.itemType;
        } else {
            mChosenFilterData.cardType = -1;
        }

        //场馆类型筛选缓存
        FilterData sortTypeData = mChosenFilterDataMap.get(SORT_TYPE_TAG);
        if(sortTypeData != null) {
            mChosenFilterData.sortType = sortTypeData.itemType;
        } else {
            mChosenFilterData.sortType = -1;
        }

        //商圈类型筛选缓存
        FilterData businessTypeData = mChosenFilterDataMap.get(BUSINESS_TYPE_TAG);
        if(businessTypeData != null) {
            MultiColumnItemData data = (MultiColumnItemData) businessTypeData;
            if(mBusinessDistrictList != null && data.x < mBusinessDistrictList.size()) {
                XBusinessDistrict district = mBusinessDistrictList.get(data.x);
                mChosenFilterData.district = district.name;
                if(data.y < district.businessAreas.size()) {
                    XBusinessArea area = district.businessAreas.get(data.y);
                    mChosenFilterData.areaName = area.name;
                    mChosenFilterData.areaId = area.id;
                }
                //附近商圈做特殊处理
                if(NEARBY.equals(mChosenFilterData.areaName)) {
                    mChosenFilterData.district = "";
                }
            }
        }

        if(mFilterChosenHandler != null) {
            mFilterChosenHandler.onFilterChosen(mChosenFilterData);
        }
    }

    /**
     * 计算距离当前日期前几天或后几天的日期，如果day为正数，则计算后几天的日期，否则计算前几天的日期
     *
     * @param date 当前日期
     * @param day  距离天数
     * @return 日期
     */
    public Date dateByAddingTimeDay(@CheckForNull Date date, int day) {
        return new Date(date.getTime() + day * 86400000l);
    }

    private void updateBusinessDistricts(BusinessListResponse result) {
        if(mBusinessDistrictList == null) {
            mBusinessDistrictList = new ArrayList<>();
        }
        mBusinessDistrictList.clear();

        //添加热门商圈
        XBusinessDistrict district = createHotDistrict(result.data.hotDistrict);
        XBusinessArea nearbyArea = createArea(district.city, district.name, 0, NEARBY);
        district.businessAreas.add(0, nearbyArea);
        mBusinessDistrictList.add(district);

        //添加其他商圈
        if(LangUtils.isEmpty(result.data.businessDistricts)) {
            return;
        }
        for(int i = 0; i < result.data.businessDistricts.size(); i++) {
            district = result.data.businessDistricts.get(i);
            XBusinessArea allArea = createArea(district.city, district.name, 0, ALL);
            district.businessAreas.add(0, allArea);
            mBusinessDistrictList.add(district);
        }
    }

    private XBusinessDistrict createHotDistrict(List<XBusinessArea> businessAreas) {
        XBusinessDistrict district = new XBusinessDistrict();
        district.name = "热门商圈";
        district.businessAreas = businessAreas;

        return district;
    }

    private XBusinessArea createArea(String city, String district, int count, String name) {
        XBusinessArea area = new XBusinessArea();
        area.city = city;
        area.district = district;
        area.gymCount = count;
        area.name = name;
        area.id = 0;

        return area;
    }

    public interface IFilterChosenHandler {
        void onFilterChosen(ChosenFilterData data);
    }

    /**
     * Filter选项的数据结构
     */
    public static class ChosenFilterData {
        //卡片类型筛选结果
        public int cardType;

        //排序类型筛选结果
        public int sortType;

        //商圈名称
        public String areaName;

        //商圈id
        public long areaId;

        //商业区名称
        public String district;
    }
}