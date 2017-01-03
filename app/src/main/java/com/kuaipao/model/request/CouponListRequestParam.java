package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;

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
