package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;

/**
 * 私教课程接口参数
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-24
 * Time: 17:56
 * Author: pingfu
 * FIXME
 */
public class CoachPersonalCourseRequestParam extends BaseRequestParam {
    /**
     * 课程id
     */
    public String courseId;

    @Override
    protected void addRequestParams() {
        addParam("combo_id", courseId);
    }
}
