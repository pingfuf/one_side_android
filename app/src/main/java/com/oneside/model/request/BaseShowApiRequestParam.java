package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;

/**
 * 易源数据中心的请求参数基类
 *
 * Created by fupingfu on 2017/1/4.
 */
public class BaseShowApiRequestParam extends BaseRequestParam {
    public String secret = "8f00d7d21c6645719b4d4f47713b4030";
    public String appId = "30094";
    private long currentTime = System.currentTimeMillis();

    @Override
    protected void addRequestParams() {
        super.addRequestParams();
        addParam("showapi_appid", appId);
        addParam("showapi_sign", secret);
        addParam("showapi_timestamp", currentTime);
    }
}
