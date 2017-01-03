package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;

/**
 * 预约ERP课程接口参数
 *
 * Created by pingfu on 16-6-2.
 */
public class BookMembershipCourseRequestParam extends BaseRequestParam {
    /**
     * 课程id
     */
    public String courseId;

    @Override
    protected void addRequestParams() {
        addParam("course", courseId);
    }
}
