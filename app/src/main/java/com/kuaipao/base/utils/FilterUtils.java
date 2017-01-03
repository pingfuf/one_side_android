package com.kuaipao.base.utils;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.manager.CardDataManager;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.umd.cs.findbugs.annotations.CheckForNull;


/**
 * Created by pingfu on 16-6-3.
 */
public class FilterUtils {
    private static final String LAST_SAVE_DATE = "last_save_date";
    private static final String BD_SAVED_KEY = "bd_saved_key";
    private static final String SAVE_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String CARD_TYPE_FILTER = "card_type_filter";
    private static final String SORT_TYPE_FILTER = "sort_type_filter";


    private static final int BETWEEN_DAY = 2;

    public static boolean isLocalSavedFilterDataValid(String city, boolean hideNoGyms) {
        if(LangUtils.isEmpty(city)) {
            return false;
        }

        String strFlag = String.format("%s_%s", city, hideNoGyms);
        String lastSavedDateKey = getSharedPreferenceKey(LAST_SAVE_DATE, strFlag);

        //判断当前的商圈存储时间是否失效
        String strLastSaveDate = IOUtils.getPreferenceValue(lastSavedDateKey);
        boolean flag;
        try {
            Date lastSaveDate = new SimpleDateFormat(SAVE_DATE_FORMAT, Locale.getDefault()).parse(strLastSaveDate);
            Date validDate = LangUtils.dateByAddingTimeDay(new Date(), 0 - BETWEEN_DAY);
            flag = lastSaveDate.after(validDate);
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 得到本地存储的商圈信息，如果返回null，表示本地没有存储改城市的商圈信息
     *
     * @param city       商圈所在的城市
     * @param hideNoGyms 是否隐藏没有场馆的商圈
     * @return 本地存储的信息
     */
    public static JSONObject getLocalBusinessDistrictData(String city, boolean hideNoGyms) {
        if(LangUtils.isEmpty(city) || !isLocalSavedFilterDataValid(city, hideNoGyms)) {
            return null;
        }

        String savedBdKey = IOUtils.getPreferenceValue(getSharedPreferenceKey(BD_SAVED_KEY, city));
        if(LangUtils.isEmpty(savedBdKey)) {
            return null;
        }

        String savedBdData = IOUtils.getPreferenceValue(savedBdKey);
        return WebUtils.parseJsonObject(savedBdData);
    }

    public static JSONObject getLocalCardTypeFilterData() {
        return WebUtils.parseJsonObject(IOUtils.getPreferenceValue(CARD_TYPE_FILTER));
    }

    public static JSONObject getLocalSordTypeFilterData() {
        return WebUtils.parseJsonObject(IOUtils.getPreferenceValue(SORT_TYPE_FILTER));
    }

    /**
     * 更新最近的存储时间
     */
    public static void updateLastestFilterSavedTime(String city) {
        SimpleDateFormat format = new SimpleDateFormat(SAVE_DATE_FORMAT, Locale.getDefault());
        String dateStr = format.format(new Date());
        IOUtils.savePreferenceValue(LAST_SAVE_DATE, dateStr);
    }

    /**
     * 保存最新的商圈信息
     *
     * @param city       什么城市的商圈
     * @param hideNoGyms 是否隐藏没有场馆的商圈
     * @param data       商圈数据
     */
    public static void saveNewestBusinessDistrictsData(String city, boolean hideNoGyms, JSONObject data) {
        if(data == null) {
            return;
        }

        String strFlag = String.format("%s_%s", city, hideNoGyms);
        String savedBdKey = getSharedPreferenceKey(BD_SAVED_KEY, strFlag);
        IOUtils.savePreferenceValue(savedBdKey, data.toString());
    }

    /**
     * 保存最近的场馆卡片筛选信息
     *
     * @param data 卡片筛选信息数据
     */
    public static void saveNewestCardTypeFilterData(JSONObject data) {
        if(data == null) {
            return;
        }
        IOUtils.savePreferenceValue(CARD_TYPE_FILTER, data.toString());
    }

    /**
     * 保存最近的排序筛选信息
     *
     * @param data 卡片筛选信息数据
     */
    public static void saveNewestSortTypeFilterData(JSONObject data) {
        if(data == null) {
            return;
        }
        IOUtils.savePreferenceValue(SORT_TYPE_FILTER, data.toString());
    }

    /**
     * 得到城市商圈信息的本地缓存key
     *
     * @param defaultKey   defaultKey
     * @param keyParamStr  变化的key
     * @return 真正的key
     */
    private static String getSharedPreferenceKey(String defaultKey, String keyParamStr) {
        return defaultKey + "_" + keyParamStr;
    }

    private static boolean doesCityBdHaveSaved(String city, boolean hideNoGyms) {
        String strFlag = String.format("%s_%s", city, hideNoGyms);
        String key = IOUtils.getPreferenceValue(getSharedPreferenceKey(BD_SAVED_KEY, strFlag));

        return !LangUtils.isEmpty(key);
    }
}
