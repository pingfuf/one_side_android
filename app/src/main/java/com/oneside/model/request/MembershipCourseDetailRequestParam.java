package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;

/**
 * ERP课程详情页请求参数
 *
 * Created by pingfu on 16-6-1.
 */
public class MembershipCourseDetailRequestParam extends BaseRequestParam {
    /**
     * 课程id
     */
    public String courseId;

    /**
     * 日期
     */
    public String date;

    @Override
    protected void addRequestParams() {

        addParam("date", date);
        addUrlParams(courseId);
    }
}
