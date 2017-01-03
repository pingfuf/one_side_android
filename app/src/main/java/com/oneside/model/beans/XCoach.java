package com.oneside.model.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 教练数据结构
 * <p/>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-24
 * Time: 14:19
 * Author: pingfu
 * FIXME
 */
public class XCoach extends XUser {
    private static long serialVersionUID = 42L;

    /**
     * 场馆id
     */
    public String gymId;

    /**
     * 教练性别, 1代表男，2代表女，其他的不知道什么意思
     */
    public int sex;

    /**
     * 教练年龄
     */
    public int age;

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
     * 教练的描述
     */
    public String desc;

    /**
     * 教练标签
     */
    public List<String> tags;
}