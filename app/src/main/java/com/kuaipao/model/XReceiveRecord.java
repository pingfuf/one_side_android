package com.kuaipao.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.model.beans.XCustomer;
import com.kuaipao.model.beans.XUser;

import java.io.Serializable;

/**
 * Created by pingfu on 16-9-13.
 */
public class XReceiveRecord implements Serializable {
    public XCustomer member;

    public XUser user;

    @JSONField(name = "follow_up_date")
    public String date;

    @JSONField(name = "remark")
    public String description;
}
