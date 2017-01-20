package com.oneside.base.utils;

import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.oneside.CardApplication;
import com.oneside.base.model.bean.XLocation;

/**
 * 定位辅助工具，提供开始定位，结束定位的功能
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-09
 * Time: 14:32
 * Author: pingfu
 * FIXME
 */
public class XLocationHelper {
    private static XLocationHelper instance;

    private XMapLocationHelper mMapLocationListener;
    private LocationManagerProxy mLocationManagerProxy;

    private XLocationHelper() {
        mMapLocationListener = new XMapLocationHelper();
    }

    public synchronized static XLocationHelper getInstance() {
        XLocationHelper aInstance = instance;
        if (instance == null) {
            synchronized (XLocationHelper.class) {
                aInstance = instance;
                if (aInstance == null) {
                    aInstance = new XLocationHelper();
                    instance = aInstance;
                }
            }
        }

        return aInstance;
    }

    /**
     * 设置定位回调接口
     *
     * @param l 定位结束回调接口
     */
    public void setLocationListener(XLocationListener l) {
        if (mMapLocationListener != null) {
            mMapLocationListener.setLocationListener(l);
        }
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        if (mLocationManagerProxy == null) {
            mLocationManagerProxy = LocationManagerProxy.getInstance(CardApplication.application);
        }

        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.removeUpdates(mMapLocationListener);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 10 * 1000, 500,
                mMapLocationListener);
    }

    /**
     * 结束定位，为了减少电量消耗或网络流量，
     */
    public void stopLocation() {
        if (mLocationManagerProxy != null && mMapLocationListener != null) {
            mLocationManagerProxy.removeUpdates(mMapLocationListener);
            mLocationManagerProxy.destroy();
        }
        mLocationManagerProxy = null;
    }

    /**
     * 定位返回接口，定位结束后通过回调传递XXLocation信息
     */
    public interface XLocationListener {
        void onLocationReceived(XLocation location);
    }

    /**
     * 实现高德的定位回调接口
     */
    private static class XMapLocationHelper implements AMapLocationListener {
        private XLocationListener mLocationListener;

        public void setLocationListener(XLocationListener l) {
            mLocationListener = l;
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            XLocation location = convert2XXLocation(aMapLocation);
            if (mLocationListener != null) {
                mLocationListener.onLocationReceived(location);
            }
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        private XLocation convert2XXLocation(AMapLocation mapLocation) {
            if (mapLocation == null) {
                return null;
            }

            XLocation location = new XLocation();
            location.roadCode = mapLocation.getRoad();
            location.roadName = mapLocation.getRoad();
            location.cityCode = mapLocation.getCityCode();
            location.cityName = mapLocation.getCity();
            location.provinceCode = mapLocation.getProvince();
            location.provinceName = mapLocation.getProvince();
            location.countryCode = mapLocation.getCountry();
            location.countryName = mapLocation.getCountry();
            location.address = mapLocation.getAddress();
            location.district = mapLocation.getDistrict();
            location.poiId = mapLocation.getPoiId();
            location.poiName = mapLocation.getPoiName();
            location.floor = mapLocation.getFloor();
            location.adCode = mapLocation.getAdCode();

            return location;
        }
    }
}
