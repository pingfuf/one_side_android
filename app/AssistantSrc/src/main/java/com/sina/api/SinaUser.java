/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sina.api;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.WebUtils;

/**
 * 用户信息结构体。
 *
 * @author SINA
 * @since 2013-11-24
 */
public class SinaUser {

    /**
     * 用户UID（int64）
     */
    public String id;
    /**
     * 字符串型的用户 UID
     */
    public String idstr;
    /**
     * 用户昵称
     */
    public String screen_name;
    /**
     * 友好显示名称
     */
    public String name;
    /**
     * 用户所在省级ID
     */
    public int province;
    /**
     * 用户所在城市ID
     */
    public int city;
    /**
     * 用户所在地
     */
    public String location;
    /**
     * 用户个人描述
     */
    public String description;
    /**
     * 用户博客地址
     */
    public String url;
    /**
     * 用户头像地址，50×50像素
     */
    public String profile_image_url;
    /**
     * 用户的微博统一URL地址
     */
    public String profile_url;
    /**
     * 用户的个性化域名
     */
    public String domain;
    /**
     * 用户的微号
     */
    public String weihao;
    /**
     * 性别，m：男、f：女、n：未知
     */
    public String gender;
    /**
     * 粉丝数
     */
    public int followers_count;
    /**
     * 关注数
     */
    public int friends_count;
    /**
     * 微博数
     */
    public int statuses_count;
    /**
     * 收藏数
     */
    public int favourites_count;
    /**
     * 用户创建（注册）时间
     */
    public String created_at;
    /**
     * 暂未支持
     */
    public boolean following;
    /**
     * 是否允许所有人给我发私信，true：是，false：否
     */
    public boolean allow_all_act_msg;
    /**
     * 是否允许标识用户的地理位置，true：是，false：否
     */
    public boolean geo_enabled;
    /**
     * 是否是微博认证用户，即加V用户，true：是，false：否
     */
    public boolean verified;
    /**
     * 暂未支持
     */
    public int verified_type;
    /**
     * 用户备注信息，只有在查询用户关系时才返回此字段
     */
    public String remark;
    /** 用户的最近一条微博信息字段 */
//    public Status status;
    /**
     * 是否允许所有人对我的微博进行评论，true：是，false：否
     */
    public boolean allow_all_comment;
    /**
     * 用户大头像地址
     */
    public String avatar_large;
    /**
     * 用户高清大头像地址
     */
    public String avatar_hd;
    /**
     * 认证原因
     */
    public String verified_reason;
    /**
     * 该用户是否关注当前登录用户，true：是，false：否
     */
    public boolean follow_me;
    /**
     * 用户的在线状态，0：不在线、1：在线
     */
    public int online_status;
    /**
     * 用户的互粉数
     */
    public int bi_followers_count;
    /**
     * 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
     */
    public String lang;

    /**
     * 注意：以下字段暂时不清楚具体含义，OpenAPI 说明文档暂时没有同步更新对应字段
     */
    public String star;
    public String mbtype;
    public String mbrank;
    public String block_word;

    public static SinaUser parse(String jsonString) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            return SinaUser.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SinaUser parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        SinaUser user = new SinaUser();
        user.id = WebUtils.getJsonString(jsonObject, "id", "");
        user.idstr = WebUtils.getJsonString(jsonObject, "idstr", "");
        user.screen_name = WebUtils.getJsonString(jsonObject, "screen_name", "");
        user.name = WebUtils.getJsonString(jsonObject, "name", "");
        user.province = WebUtils.getJsonInt(jsonObject, "province", -1);
        user.city = WebUtils.getJsonInt(jsonObject, "city", -1);
        user.location = WebUtils.getJsonString(jsonObject, "location", "");
        user.description = WebUtils.getJsonString(jsonObject, "description", "");
        user.url = WebUtils.getJsonString(jsonObject, "url", "");
        user.profile_image_url = WebUtils.getJsonString(jsonObject, "profile_image_url", "");
        user.profile_url = WebUtils.getJsonString(jsonObject, "profile_url", "");
        user.domain = WebUtils.getJsonString(jsonObject, "domain", "");
        user.weihao = WebUtils.getJsonString(jsonObject, "weihao", "");
        user.gender = WebUtils.getJsonString(jsonObject, "gender", "");
        user.followers_count = WebUtils.getJsonInt(jsonObject, "followers_count", 0);
        user.friends_count = WebUtils.getJsonInt(jsonObject, "friends_count", 0);
        user.statuses_count = WebUtils.getJsonInt(jsonObject, "statuses_count", 0);
        user.favourites_count = WebUtils.getJsonInt(jsonObject, "favourites_count", 0);
        user.created_at = WebUtils.getJsonString(jsonObject, "created_at", "");
        user.following = WebUtils.getJsonBoolean(jsonObject, "following", false);
        user.allow_all_act_msg = WebUtils.getJsonBoolean(jsonObject, "allow_all_act_msg", false);
        user.geo_enabled = WebUtils.getJsonBoolean(jsonObject, "geo_enabled", false);
        user.verified = WebUtils.getJsonBoolean(jsonObject, "verified", false);
        user.verified_type = WebUtils.getJsonInt(jsonObject, "verified_type", -1);
        user.remark = WebUtils.getJsonString(jsonObject, "remark", "");
        //user.status             = jsonObject.optString("status", ""); // XXX: NO Need ?
        user.allow_all_comment = WebUtils.getJsonBoolean(jsonObject, "allow_all_comment", true);
        user.avatar_large = WebUtils.getJsonString(jsonObject, "avatar_large", "");
        user.avatar_hd = WebUtils.getJsonString(jsonObject, "avatar_hd", "");
        user.verified_reason = WebUtils.getJsonString(jsonObject, "verified_reason", "");
        user.follow_me = WebUtils.getJsonBoolean(jsonObject, "follow_me", false);
        user.online_status = WebUtils.getJsonInt(jsonObject, "online_status", 0);
        user.bi_followers_count = WebUtils.getJsonInt(jsonObject, "bi_followers_count", 0);
        user.lang = WebUtils.getJsonString(jsonObject, "lang", "");

        // 注意：以下字段暂时不清楚具体含义，OpenAPI 说明文档暂时没有同步更新对应字段含义
        user.star = WebUtils.getJsonString(jsonObject, "star", "");
        user.mbtype = WebUtils.getJsonString(jsonObject, "mbtype", "");
        user.mbrank = WebUtils.getJsonString(jsonObject, "mbrank", "");
        user.block_word = WebUtils.getJsonString(jsonObject, "block_word", "");

        return user;
    }
}
