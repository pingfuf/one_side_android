package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;

/**
 * 绑定场馆接口参数
 * <p/>
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-12
 * Time: 10:34
 * Author: pingfu
 * FIXME
 */
public class BindGymRequestParam extends BaseRequestParam {
    private static long serialVersionUID = 42L;

    /**
     * 场馆id
     */
    public long gymId;

    @Override
    protected void addRequestParams() {
        addParam("gym", gymId);
    }
}
