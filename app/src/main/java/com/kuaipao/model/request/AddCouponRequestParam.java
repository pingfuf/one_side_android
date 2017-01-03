package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;

/**
 * Created by pingfu on 16-6-28.
 */
public class AddCouponRequestParam extends BaseRequestParam {
    public String code;

    @Override
    protected void addRequestParams() {
        addParam("code", code);
    }
}
