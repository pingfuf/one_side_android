package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;

/**
 * Created by pingfu on 16-6-27.
 */
public class CouponListRequestParam extends BaseRequestParam {
    /**
     * 请求分页
     */
    public int page;

    @Override
    protected void addRequestParams() {
        addParam("page", page);
    }
}
