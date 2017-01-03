package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * 场馆数据结构
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-17
 * Time: 18:44
 * Author: pingfu
 * FIXME
 */
public class XGym implements Serializable {
    private static long serialVersionUID = 42L;

    /**
     * 场馆id
     */
    public long id;

    /**
     * 场馆id
     */
    public String name;

    /**
     * 场馆评分，5分制
     */
    @JSONField(name = "rating")
    public float score;

    /**
     * 场馆地址
     */
    @JSONField(name = "location")
    public String address;

    /**
     * 场馆logo的url
     */
    @JSONField(name = "logo")
    public String logoUrl;

    /**
     * 场馆是否支持小熊通卡
     */
    @JSONField(name = "xx_supported")
    public boolean isSupportXXCard;

    @JSONField(name = "is_followed")
    public boolean isCared;

    /**
     * 场馆距离
     */
    public float distance;

    /**
     * 关注场馆的人员数量
     */
    @JSONField(name = "fans_count")
    public int fansCount;

    /**
     * 支持的卡片类型
     */
    @JSONField(name = "gym_tags")
    public List<GymSupportCard> supportCards;

    /**
     * 电话
     */
    public String phone;

    /**
     * 维度
     */
    public String lat;

    /**
     * 经度
     */
    public String lon;
}
