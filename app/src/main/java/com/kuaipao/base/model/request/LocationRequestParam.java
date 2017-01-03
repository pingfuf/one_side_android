package com.kuaipao.base.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.model.LocationCoordinate2D;

import java.util.HashMap;

/**
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-09
 * Time: 14:34
 * Author: pingfu
 * FIXME
 */
public class LocationRequestParam extends BaseRequestParam {
    public LocationCoordinate2D usrLocation;
    public String city;
    public LocationCoordinate2D leftTop;
    public LocationCoordinate2D rightBottom;

    @Override
    public void addRequestParams() {

    }
}
