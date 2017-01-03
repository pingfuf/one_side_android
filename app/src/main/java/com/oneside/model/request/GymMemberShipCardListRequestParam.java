package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;

/**
 * 请求场馆会员卡列表接口参数
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-23
 * Time: 11:56
 * Author: pingfu
 * FIXME
 */
public class GymMemberShipCardListRequestParam extends BaseRequestParam {
    private static long serialVersionUID = 42L;

    /**
     * 场馆id
     */
    public long gymId;

    @Override
    protected void addRequestParams() {
        addUrlParams(gymId);
    }
}
