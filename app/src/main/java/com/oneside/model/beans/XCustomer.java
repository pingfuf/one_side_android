package com.oneside.model.beans;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by pingfu on 16-9-1.
 */
public class XCustomer extends XUser {
    private static long serialVersionUID = 42L;

    public int status;

    @JSONField(name = "insert_time")
    public Date insertTime;

    @JSONField(name = "is_active")
    public boolean isActive;

    public XGym gym;

    @JSONField(name = "membership_card")
    public JSONObject membershipCard;

    @JSONField(name = "mc_count")
    public int mcCount;


}
