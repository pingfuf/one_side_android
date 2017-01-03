package com.kuaipao.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 场馆会员卡类型的数据结构
 *
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-16
 * Time: 16:15
 * Author: pingfu
 * FIXME
 */
public class GymSupportCard implements Serializable {
    private static long serialVersionUID = 42L;
    /**
     * 特征名称
     */
    @JSONField(name = "desc")
    public String name;

    /**
     * 特征类型
     */
    public int type;

    /**
     * 卡片特征
     */
    @JSONField(name = "char")
    public String tag;

    /**
     * 卡片特征颜色
     */
    public String color;
}
