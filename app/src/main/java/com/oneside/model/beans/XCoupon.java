package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 优惠券数据结构
 *
 * Created by pingfu on 16-6-27.
 */
public class XCoupon implements Serializable {

    private static final long serialVersionUID = -7971360917931251834L;
    /**
     * 优惠券id
     */
    public long id;

    /**
     * 优惠券名称
     */
    public String name;

    /**
     * 优惠券过期时间
     */
    public String endTime;

    /**
     * 优惠券价值
     */
    public int price;

    /**
     * 优惠券最小消费
     */
    public int minConsume;

    /**
     * 优惠券类型：0为通用，1为三次通卡，2为有氧空间
     */
    public int type;

    /**
     * 优惠券描述
     */
    @JSONField(name = "restrict_desc")
    public String description;

    /**
     * 优惠券状态：0为未使用，1为正在使用，2为已使用，3为已过期
     */
    public int status;
}
