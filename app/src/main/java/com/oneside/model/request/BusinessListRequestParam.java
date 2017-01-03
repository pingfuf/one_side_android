package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;

/**
 * 请求商圈列表接口
 * <p/>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-17
 * Time: 16:57
 * Author: pingfu
 * FIXME
 */
public class BusinessListRequestParam extends BaseRequestParam {
    private static long serialVersionUID = 42L;
    
    /**
     * 所在的城市
     */
    public String city;

    /**
     * 是否过滤掉没有场馆的商圈
     */
    public boolean isFilterNoGymBusiness;

    @Override
    protected void addRequestParams() {
        addParam("city", city);
        addParam("lazy", isFilterNoGymBusiness);
    }
}
