package com.kuaipao.model.beans;

import java.io.Serializable;

/**
 * 用户购买的私教课程数据结构
 *
 * Created by pingfu on 16-8-18.
 */
public class XUserCoachCourse implements Serializable {
    private static long serialVersionUID = 42L;

    /**
     * 私教课程订单id
     */
    public long id;

    /**
     * 课程名称
     */
    public String courseName;

    /**
     * 课程id
     */
    public long courseId;

    /**
     * 教练名称
     */
    public XCoach coach;

    /**
     * 剩余几节课
     */
    public int remainingCourses;

    /**
     * 剩余几天
     */
    public int remainingDays;


    /**
     * 课程共有几节
     */
    public int courseTimes;

    /**
     * 课程状态
     */
    public int status;
}