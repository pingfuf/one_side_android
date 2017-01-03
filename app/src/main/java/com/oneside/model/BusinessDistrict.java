package com.oneside.model;

import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.LangUtils;
import com.oneside.utils.WebUtils;

public class BusinessDistrict {


    /**
     * districtName is administration unit like haidian, dongcheng, chaoyang in BeiJing
     * businessName is the name of business district
     */
    private String cityName, businessName, districtName;

    private int number = 0;
    private long id;

    public BusinessDistrict(String business, long id) {
        businessName = business;
        this.id = id;
    }

    public BusinessDistrict(JSONObject json) {
        cityName = WebUtils.getJsonString(json, "city");
        businessName = WebUtils.getJsonString(json, "business");
        districtName = WebUtils.getJsonString(json, "district");
        id = WebUtils.getJsonLong(json, "id", 0l);
        number = WebUtils.getJsonInt(json, "gyms", 0);
    }

    public static BusinessDistrict getBusinessDistrict(JSONObject j) {
        if (j == null || j.size() <= 0) {
            return null;
        }
        return new BusinessDistrict(j);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        return LangUtils.format("{city:%s; business:%s; district:%s; number:%s; id:%s}", cityName, businessName, districtName, number, id);
    }
}
