package com.oneside.base.net.model;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 后端返回结果数据结构
 * <p/>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-23
 * Time: 13:36
 * Author: pingfu
 * FIXME
 */
public class BaseResult implements Serializable {

    /**
     * 网络返回的code
     */
    @JSONField(name = "errcode")
    public int code;

    /**
     * 网络返回的msg
     */
    @JSONField(name = "errmsg")
    public String msg;

    /**
     * 返回的JSON数据的data部分，如果返回结果不是JSONObject类型，在子类中重新定义data
     */
    public JSONObject data;

    /**
     * 处理解析结果
     *
     * @param jsonObject 返回的json数据
     */
    public void arrangeResponseData(JSONObject jsonObject) {
    }
}