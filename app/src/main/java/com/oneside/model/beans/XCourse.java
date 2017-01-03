package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程数据结构
 * <p/>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-24
 * Time: 14:20
 * Author: pingfu
 * FIXME
 */
public class XCourse implements Serializable {
    private static long serialVersionUID = 42L;
    /**
     * 课程id
     */
    public String id;

    /**
     * 课程名称
     */
    public String name;

    /**
     * 课程类型
     */
    public int type;

    /**
     * 课程签约价格
     */
    public int signedPrice;

    /**
     * 课程实售价格
     */
    public int price;

    /**
     * 日期
     */
    public String date;

    /**
     * 课程开始时间
     */
    public String startTime;

    /**
     * 课程结束时间
     */
    public String endTime;

    /**
     * 课程最多报名学生数量
     */
    public int maxStudentsCount;

    /**
     * 课程描述
     */
    @JSONField(name = "desc")
    public String description;

    /**
     * 课程是否过期
     */
    @JSONField(name = "is_disabled")
    public boolean disabled;

    /**
     * 课程标签
     */
    public List<String> labels;
}
