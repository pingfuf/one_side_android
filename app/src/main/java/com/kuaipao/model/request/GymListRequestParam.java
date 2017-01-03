package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.model.LocationCoordinate2D;

/**
 * 场馆列表页请求参数
 *
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-06
 * Time: 10:52
 * Author: pingfu
 * FIXME
 */
public class GymListRequestParam extends BaseRequestParam {
    private static long serialVersionUID = 42L;

    //经纬度
    public LocationCoordinate2D location;

    //当前城市
    public String city;

    //场馆类型 0-'全部', 1-'器械健身', 2-游泳, 3-'瑜伽', 4-'舞蹈', 5-'单车', 6-'武术'
    public int gymsType;

    //支持会员卡类型
    public int supportCardType;

    //商业区id
    public long areaId;

    //商圈
    public String district;

    //排序方式
    public int sortType;

    //偏移位置，用于分页
    public int offset;

    //分页限制，每页取几条数据
    public int limit;

    @Override
    protected void addRequestParams() {
        if(location != null) {
            addParam("lat", location.getLatitude());
            addParam("lng", location.getLongitude());
            //一直使用高德地图，这一个参数写死
            addParam("coordsys", "gaode");
        }

        addParam("city", city);
        addParam("category", gymsType);
        addParam("area", areaId);
        addParam("district", district);

        String filter = "[";
        filter += supportCardType;
        filter += "," + sortType + "]";
        addParam("filter", filter);

        addParam("offset", offset);
        addParam("limit", limit);
    }
}