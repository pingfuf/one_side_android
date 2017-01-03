package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pingfu on 16-11-1.
 */
public class XReceiveRecord implements Serializable {
    private static long serialVersionUID = 42L;

    public long id;

    @JSONField(name = "follow_up_date")
    public Date date;

    public XCoach user;

    public XMember member;

    @JSONField(name = "remark")
    public String desc;
}
