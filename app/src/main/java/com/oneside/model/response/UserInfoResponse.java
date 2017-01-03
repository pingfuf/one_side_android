package com.oneside.model.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.inject.From;
import com.oneside.base.net.model.BaseResponseData;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.GymSupportCard;
import com.oneside.model.beans.XGym;
import com.oneside.model.beans.XRole;
import com.oneside.utils.WebUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 场馆列表页面返回结果
 * <p/>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-06
 * Time: 12:31
 * Author: pingfu
 * FIXME
 */
public class UserInfoResponse extends BaseResult {
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

    public String gender;

    /**
     * 生日
     */
    public String birthday;

    /**
     * 头像
     */
    @JSONField(name = "avatar_url")
    public String avatar;

    /**
     * 电话
     */
    public String phone;

    /**
     * 是否是管理员
     */
    @JSONField(name = "is_admin")
    public boolean isAdmin;


    public String desc;

    @JSONField(name = "gym_list")
    public List<XGym> gymList;

    @JSONField(name = "role_list")
    public List<XRole> roleList;

    @Override
    public void arrangeResponseData(JSONObject dataStr) {
        super.arrangeResponseData(dataStr);
    }
}