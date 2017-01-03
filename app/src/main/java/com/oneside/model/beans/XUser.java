package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by pingfu on 16-8-11.
 */
public class XUser implements Serializable {
    private static long serialVersionUID = 42L;

    public long id;

    /**
     * 姓名
     */
    public String name;

    /**
     * 性别，1代表男，2代表女
     */
    public int sex;

    /**
     * 用户的性别：m->male, f->female
     */
    public String gender;

    /**
     * 生日
     */
    public String birthday;

    /**
     * 头像
     */
    public String avatar;

    /**
     * 电话
     */
    public String phone;

    /**
     * 是否是管理员
     */
    public boolean isAdmin;

    @JSONField(name = "insert_time")
    public String beginTime;
}
