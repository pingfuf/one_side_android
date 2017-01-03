package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;

/**
 * ERP课程详情请求参数
 *
 * Created by pingfu on 16-5-27.
 */
public class GymMembershipCourseDetailParam extends BaseRequestParam {
    /**
     * 课程id
     */
    public String courseId;

    @Override
    protected void addRequestParams() {
        addUrlParams(courseId);
    }
}
