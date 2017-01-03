package com.kuaipao.model.beans;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 本地存储的商圈信息数据结构
 *
 * Created by pingfu on 16-6-6.
 */
public class XBusinessFilterData {
    private static final String BD_SAVED_KEY = "bd_saved_key_";
    private static final String LAST_SAVE_DATE = "last_save_date_";
    private static final String SAVE_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static final int BETWEEN_DAY = 2;

    /**
     * 商圈的JSON数据
     */
    private JSONObject mBusinessFilterData;

    /**
     * 商圈所在的city
     */
    private String mCity;

    /**
     * 是否隐藏没有场馆的商圈
     */
    private boolean mHideNoGymDistrict;

    public XBusinessFilterData(String city, boolean hideNoGymDistrict) {
        mCity = city;
        mHideNoGymDistrict = hideNoGymDistrict;
    }


    /**
     * 本地存储的商圈信息是否有效
     *
     * @return 是否有效
     */
    public boolean isLocalSavedDataValid() {
        if(LangUtils.isEmpty(mCity)) {
            return false;
        }

        String lastSavedDateKey = getSavedDateKey();

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
     * 得到本地存储的商圈信息，如果本地存储的信息无效，返回null
     *
     * @return 本地存储的商圈信息
     */
    public JSONObject getLocalSavedData() {
        if(LangUtils.isEmpty(mCity) || !isLocalSavedDataValid()) {
            return null;
        }

        return WebUtils.parseJsonObject(IOUtils.getPreferenceValue(getSavedDistrictDataKey()));
    }

    /**
     * 存储商圈信息
     *
     * @param filterData 商圈信息
     */
    public void saveDistrictData(JSONObject filterData) {
        if(filterData == null) {
            return;
        }

        mBusinessFilterData = filterData;
        String filterDataKey = getSavedDistrictDataKey();
        String lastSavedKey = getSavedDateKey();
        SimpleDateFormat format = new SimpleDateFormat(SAVE_DATE_FORMAT, Locale.getDefault());
        String dateStr = format.format(new Date());
        IOUtils.savePreferenceValue(lastSavedKey, dateStr);
        IOUtils.savePreferenceValue(filterDataKey, mBusinessFilterData.toString());
    }

    private String getSavedDistrictDataKey() {
        String strFlag = String.format("%s_%s", mCity, mHideNoGymDistrict);

        return BD_SAVED_KEY + strFlag;
    }

    private String getSavedDateKey() {
        return LAST_SAVE_DATE + mCity;
    }
}
