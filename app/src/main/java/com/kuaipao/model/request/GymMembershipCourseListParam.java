package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;

/**
 * ERP课程列表请求参数
 *
 * Created by pingfu on 16-5-27.
 */
public class GymMembershipCourseListParam extends BaseRequestParam {
    /**
     * 场馆id
     */
    public String gymId;

    /**
     * 日期
     */
    public String date;

    @Override
    protected void addRequestParams() {
        addParam("date", date);
        addUrlParams(gymId);
    }
}
