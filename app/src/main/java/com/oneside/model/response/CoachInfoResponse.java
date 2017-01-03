package com.oneside.model.response;

import com.oneside.base.net.model.BaseResult;

import java.util.List;

/**
 * 教练详情接口返回结果
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-24
 * Time: 15:13
 * Author: pingfu
 * FIXME
 */
public class CoachInfoResponse extends BaseResult {
    /**
     * 教练id
     */
    public String id;

    /**
     * 场馆id
     */
    public String gymId;

    /**
     * 教练名称
     */
    public String name;

    /**
     * 教练性别
     */
    public String sex;

    /**
     * 教练年龄
     */
    public int age;

    /**
     * 教练手机号
     */
    public String phone;

    /**
     * 教练证书
     */
    public String credential;

    /**
     * 教练介绍
     */
    public String intro;

    /**
     * 教练头像URL
     */
    public String imgUrl;

    /**
     * 教练标签
     */
    public List<String> tags;


}
