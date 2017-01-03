package com.oneside.manager;

import java.util.List;

import com.oneside.R;
import com.oneside.model.LocationCoordinate2D;
import com.oneside.utils.Constant;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

public class CardLocationManager {
    private LocationCoordinate2D mLocationCoordinate;
    private String mCityName;
    private String mAddress;
    private String mDistrict;
    private volatile List<String> supportCityList;

    private static CardLocationManager mCardLocationManager = null;

    private CardLocationManager() {
        // nothing
//        mCityName = IOUtils.getPreferenceValue(Constant.PREFERENCE_KEY_SELETED_CITY_NAME);
    }

    public static CardLocationManager getInstance() {
        if (mCardLocationManager == null)
            mCardLocationManager = new CardLocationManager();
        return mCardLocationManager;
    }

    public LocationCoordinate2D getLocation() {
        if (mLocationCoordinate != null) {
            return mLocationCoordinate;
        } else {
//      mLocationCoordinate = new LocationCoordinate2D(39.9995623926, 116.3398764221);
        }
        return mLocationCoordinate;
    }

    public void setLocation(double lat, double lng) {
        if (lat < 0 || lng < 0) {
            mLocationCoordinate = null;
            return;
        }
        if (mLocationCoordinate == null) {
            mLocationCoordinate = new LocationCoordinate2D(lat, lng);
        } else {
            mLocationCoordinate.setLatitude(lat);
            mLocationCoordinate.setLongitude(lng);
        }
    }

    public String getCityName() {
        if(LangUtils.isEmpty(mCityName))
            return ViewUtils.getString(R.string.default_city);
        return mCityName;
    }

    public void setCityName(String city) {
        if (LangUtils.isEmpty(city)) {
            return;
        }
        this.mCityName = city;
//        IOUtils.savePreferenceValue(Constant.PREFERENCE_KEY_SELETED_CITY_NAME, city != null ? city : "");
    }

    public String getLocationName() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getDistrict() {
        return mDistrict;
    }

    public void setDistrict(String district) {
        this.mDistrict = district;
    }

    public List<String> getSupportCityList() {
        return supportCityList;
    }

    public void setSupportCityList(List<String> supportCityList) {
        this.supportCityList = supportCityList;
    }

    public boolean isCitySupported(String city) {
        if (this.supportCityList == null || this.supportCityList.size() < 1) {
            return false;
        }

        for (String strSupportCity : supportCityList) {
            if (strSupportCity.contains(city)) {
                return true;
            }
        }
        return false;
    }
}