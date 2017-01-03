package com.kuaipao.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * 用户激活场馆卡返回的用户数据
 *
 * Created by pingfu on 16-7-22.
 */
public class XActiveUserInfo implements Serializable {
    @JSONField(name = "avatar")
    public String headerUrl;

    public String name;

    public String phone;

    public String email;

    public String regioncode;

    public Persona persona;

    public static class Persona {
        public String character;

        public String gender;

        public String generation;

        public UserLocation location;

        public List<String> tags;
    }

    public static class UserLocation {
        public String city;
        public String country;
        public String province;
    }
}
