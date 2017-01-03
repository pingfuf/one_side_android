package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;

/**
 * 教练详情请求接口参数
 * <p/>
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-24
 * Time: 14:55
 * Author: pingfu
 * FIXME
 */
public class CoachInfoRequestParam extends BaseRequestParam {
    private static long serialVersionUID = 42L;

    /**
     * 教练id
     */
    public String coachId;

    @Override
    protected void addRequestParams() {
        addParam("id", coachId);
    }
}
