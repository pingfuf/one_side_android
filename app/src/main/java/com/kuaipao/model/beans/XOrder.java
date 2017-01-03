package com.kuaipao.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by pingfu on 16-11-7.
 */
public class XOrder implements Serializable {
    public long id;

    @JSONField(name = "coach_id")
    public long coachId;

    public long gymId;

    public int price;

    public int quantity;

    public int status;

    @JSONField(name = "individual_course")
    public XCourse course;

    public int remaining;
}
