package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by pingfu on 16-9-12.
 */
public class XCard implements Serializable {
    private static long serialVersionUID = 42L;

    /**
     * 会员卡iD
     */
    public long id;

    /**
     * 卡片名称
     */
    public String name;

    /**
     * 会员卡类型
     */
    public String cardType;

    /**
     * 卡片价格
     */
    public int price;

    /**
     * 卡片购买时间（开始时间）
     */
    public String beginTime;

    /**
     * 卡片结束时间（什么时候过期）
     */
    public String endTime;

    /**
     * 卡片描述
     */
    public String description;

    public int status;
}
