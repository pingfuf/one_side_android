package com.kuaipao.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guoming on 31/10/16.
 */
public class CoachMember implements Serializable {

    @JSONField(name = "member")
    private XUser user;

    @JSONField(name = "quantity")
    private int allCount = 0;

    @JSONField(name = "remaining")
    private int remainCount = 0;

    @JSONField(name = "latest_item")
    private SimpleCoachService coachService;

    public XUser getUser() {
        return user;
    }

    public void setUser(XUser user) {
        this.user = user;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(int remainCount) {
        this.remainCount = remainCount;
    }

    public SimpleCoachService getCoachService() {
        return coachService;
    }

    public void setCoachService(SimpleCoachService coachService) {
        this.coachService = coachService;
    }


}
