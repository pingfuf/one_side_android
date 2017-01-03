package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 商圈数据结构
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-17
 * Time: 16:48
 * Author: pingfu
 * FIXME
 */
public class XBusinessArea implements Serializable {
    private static long serialVersionUID = 42L;

    /**
     * 商圈ID
     */
    public long id;

    /**
     * 商圈所在的city
     */
    public String city;

    /**
     * 商圈所在的商业区
     */
    public String district;

    /**
     * 商圈名称
     */
    @JSONField(name = "business")
    public String name;

    /**
     * 商圈拥有的场馆数量
     */
    @JSONField(name = "gyms")
    public int gymCount;
}
