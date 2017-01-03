package com.kuaipao.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by guoming on 6/30/16.
 */
public class CardSimpleClass implements Serializable{

    private static final long serialVersionUID = 614596932304527276L;


//    "notice": null,
//            "end": "2016-06-28T19:00:00",
//            "name": "\u666e\u62c9\u63d0",
//            "start": "2016-06-28T18:00:00",
//            "type": 3,
//            "id": "e875779a403d4c15b656a767c241915b_1467108000"




    @JSONField(name = "type")
    private int coachType;//0 普通课程; 1 私教课程; 2 私教体验课
    @JSONField(name = "id")
    private String classId;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "start")
    private Date start;
    @JSONField(name = "end")
    private Date end;
    @JSONField(name = "notice")
    private String notice;

    public int getCoachType() {
        return coachType;
    }

    public void setCoachType(int coachType) {
        this.coachType = coachType;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }


}
