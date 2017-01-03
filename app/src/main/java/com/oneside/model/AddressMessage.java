package com.oneside.model;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.WebUtils;

public class AddressMessage {

    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TYPE = "type";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_MERCHANTID = "merchant_id";
    
    private String mName;
    private String mAddress;
    private String mType;
    private double mLatitude;
    private double mLongitude;
    private long mMerchantID = -1l;

    /**
     * @param name
     * @param address
     * @param type      home company normal
     * @param latitude
     * @param longitude
     */
    public AddressMessage(String name, String address, String type, double latitude,
                          double longitude, long merchantID) {
        mName = name;
        mAddress = address;
        mType = type;
        mLatitude = latitude;
        mLongitude = longitude;
        mMerchantID = merchantID;
    }

    public AddressMessage(String name, String address, double latitude, double longitude) {
        mName = name;
        mAddress = address;
        // mType = type;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public static AddressMessage fromJson(JSONObject json) {
        String name = WebUtils.getJsonString(json, KEY_NAME);
        String address = WebUtils.getJsonString(json, KEY_ADDRESS, "");
        String type = WebUtils.getJsonString(json, KEY_TYPE, "");
        double latitude = WebUtils.getJsonDouble(json, KEY_LATITUDE, 0l);
        double longitude = WebUtils.getJsonDouble(json, KEY_LONGITUDE, 0l);
        long mMerchantID = WebUtils.getJsonLong(json, KEY_MERCHANTID, 0l);
        return new AddressMessage(name, address, type, latitude, longitude, mMerchantID);
    }

    public static AddressMessage fromJsonForUpload(JSONObject json) {
        String name = WebUtils.getJsonString(json, "name");
        String address = WebUtils.getJsonString(json, "addr", "");
        double latitude = WebUtils.getJsonDouble(json, "lat", 0l);
        double longitude = WebUtils.getJsonDouble(json, "lng", 0l);
        return new AddressMessage(name, address, latitude, longitude);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_NAME, mName);
            json.put(KEY_ADDRESS, mAddress);
            json.put(KEY_TYPE, mType);
            json.put(KEY_LATITUDE, mLatitude);
            json.put(KEY_LONGITUDE, mLongitude);
            json.put(KEY_MERCHANTID, mMerchantID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject toJsonForUpload() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", mName);
            json.put("addr", mAddress);
            // json.put(KEY_TYPE, mType);
            json.put("lat", mLatitude);
            json.put("lng", mLongitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setLat(double latitude) {
        mLatitude = latitude;
    }

    public void setLon(double longitude) {
        mLongitude = longitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public long getMerchantID() {
        return mMerchantID;
    }

    public double getLat() {
        return mLatitude;
    }

    public double getLon() {
        return mLongitude;
    }

    public String toString() {
        return "Address:" + mAddress + "\tName:" + mName + "\tType:" + mType + "\tLatitude:"
                + mLatitude + "\tLongitude:" + mLongitude;
    }

    @Override
    public boolean equals(Object o) {
        AddressMessage am = (AddressMessage) o;

        return this.mType.equals(am.mType) && this.mMerchantID == am.mMerchantID
                && this.mName.equals(am.mName);
    }

}
