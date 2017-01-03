package com.kuaipao.model;

// import static com.kiwi.utils.LangUtils.*;

import java.io.Serializable;
import java.util.HashMap;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LogUtils;


/**
 * @author Guo Ming
 */
public class LocationCoordinate2D implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5042072426050327161L;

    private double latitude;
    private double longitude;

    //need to be confirmed with web!!!
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @param values 0, latitude, 1, longtitude
     */
    public LocationCoordinate2D(double[] values) {
        if (values == null || values.length < 2) {
            return;
        }
        latitude = values[0];
        longitude = values[1];
    }

    public LocationCoordinate2D(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    @Override
    public String toString() {
        JSONObject j = toJson();
        return j == null ? "" : j.toString();// format("{\"lat\":%f,\"lng\":%f}", getLatitude(),
        // getLongitude());
    }

    public JSONObject toJson() {
        try {
            JSONObject dict = new JSONObject();
            dict.put(KEY_LAT, getLatitude());
            dict.put(KEY_LNG, getLongitude());
            return dict;
        } catch (Exception e) {
            LogUtils.w(e, " parse event serial error");

        }
        return null;
    }

    public static LatLng toMapData(LocationCoordinate2D location) {
        if (location == null) {
            return null;
        }
        return new LatLng(location.latitude, location.longitude);
    }

    public static LocationCoordinate2D fromJson(JSONObject j) {
        if (j == null || j.size() == 0) {
            return null;
        }
        try {
            double lat = Double.parseDouble(String.valueOf(j.get(KEY_LAT)));
            double lng = Double.parseDouble(String.valueOf(j.get(KEY_LNG)));
            return new LocationCoordinate2D(new double[]{lat, lng});
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_LAT, getLatitude());
        map.put(KEY_LNG, getLongitude());
        return map;
    }

    public static LocationCoordinate2D fromHashMap(HashMap<String, Object> m) {
        if (m == null || m.isEmpty()) {
            return null;
        }
        try {
            double lat = Double.parseDouble(String.valueOf(m.get(KEY_LAT)));
            double lng = Double.parseDouble(String.valueOf(m.get(KEY_LNG)));
            return new LocationCoordinate2D(new double[]{lat, lng});
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof LocationCoordinate2D) {
            float betweenDistance = AMapUtils.calculateLineDistance(toMapData(this),
                    LocationCoordinate2D.toMapData((LocationCoordinate2D) o));// m
            if (betweenDistance < 200.0f)// <200m
                return true;
            //      return getLatitude() == ((LocationCoordinate2D) o).getLatitude()
//          && getLongitude() == ((LocationCoordinate2D) o).getLongitude();
        }
        return false;
    }

    public boolean locationIsValid() {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }

    @SuppressWarnings("unchecked")
    public static LocationCoordinate2D createLocationByDict(Object dict) {
        if (dict == null) {
            return null;
        }
        if (dict instanceof HashMap) {
            return fromHashMap((HashMap<String, Object>) dict);
        } else if (dict instanceof JSONObject) {
            return fromJson((JSONObject) dict);
        } else if (dict instanceof byte[]) {
            return (LocationCoordinate2D) IOUtils.deserialize((byte[]) dict);
        } else if (dict instanceof JSONArray) {
            JSONArray array = (JSONArray) dict;
            if (array.size() != 2) {
                return null;
            }
            try {
                double lat = Double.parseDouble(String.valueOf(array.get(0)));
                double lng = Double.parseDouble(String.valueOf(array.get(1)));
                return new LocationCoordinate2D(new double[]{lat, lng});
            } catch (Exception e) {

            }
        } else if (dict instanceof LocationCoordinate2D)
            return (LocationCoordinate2D) dict;
        return null;
    }
}
