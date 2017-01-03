package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * 商业区数据结构
 * <p/>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-17
 * Time: 16:51
 * Author: pingfu
 * FIXME
 */
public class XBusinessDistrict implements Serializable {
    private static long serialVersionUID = 42L;

    /**
     * 商业区id
     */
    public long id;

    /**
     * 商业区名称
     */
    public String name;

    /**
     * 商业区别名
     */
    public String alias;

    /**
     * 商业区所在的城市
     */
    public String city;

    /**
     * 商业区编号
     */
    @JSONField(name = "area_code")
    public String code;

    /**
     * 商业区排名
     */
    public int rank;

    /**
     * 商业区商圈列表
     */
    @JSONField(name = "business_blocks")
    public List<XBusinessArea> businessAreas;
}
