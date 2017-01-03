package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;

/**
 * 取消预约ERP课程请求参数
 *
 * Created by pingfu on 16-6-2.
 */
public class UnBookMembershipCourseRequestParam extends BaseRequestParam {
    /**
     * 订单id
     */
    public String orderId;

    @Override
    protected void addRequestParams() {
        addParam("order", orderId);
    }
}
