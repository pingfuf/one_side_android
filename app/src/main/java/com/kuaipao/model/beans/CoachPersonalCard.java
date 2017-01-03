package com.kuaipao.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by pingfu on 16-9-12.
 */
public class CoachPersonalCard extends XCard {
    private static long serialVersionUID = 42L;

    /**
     * 剩余课程
     */
    @JSONField(name = "remaining")
    public int remainCourses;

    @JSONField(name = "insert_time")
    public String beginTime;

    public String date;

    public boolean isShowDate;

    /**
     * 教练头像
     */
    @JSONField(name = "member")
    public XCoach coach;

    @JSONField(name = "individual_course")
    public XCourse course;
}
