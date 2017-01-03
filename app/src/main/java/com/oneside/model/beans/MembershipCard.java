package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员卡公共类
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-11
 * Time: 13:39
 * Author: pingfu
 * FIXME
 */
public class MembershipCard extends XCard {
    private static long serialVersionUID = 42L;

    /**
     * 会员卡标题
     */
    public String title;

    /**
     * 会员卡描述
     */
    @JSONField(name = "desc")
    public String description;

    /**
     * 会员卡图标
     */
    public XIcon icon;

    /**
     * 会员卡促销价格
     */
    @JSONField(name = "pay_fee")
    public int price;

    @JSONField(name = "insert_time")
    public String beginTime;

    @JSONField(name = "end_date")
    public String endTime;

    @JSONField(name = "refund_time")
    public Date refundTime;

    /**
     * 会员卡平时价格
     */
    @JSONField(name = "org_price")
    public int commonPrice;

    public String date;

    public boolean isShowDate;
}
