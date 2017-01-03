package com.kuaipao.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResponseData;
import com.kuaipao.base.net.model.BaseResult;

/**
 * 私教套餐接口返回结果
 *
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-24
 * Time: 17:41
 * Author: pingfu
 * FIXME
 */
public class CoachPersonalCourseResponse extends BaseResult {
    private static long serialVersionUID = 42L;

    public CoachPersonalCourseResponseData data;
    public static class CoachPersonalCourseResponseData extends BaseResponseData {
        //课程id
        public String id;

        //课程名称
        public String name;

        //课程类型
        @JSONField(name = "course_type")
        public String courseType;

        //课程介绍（课程的安排）
        public String introduce;

        @JSONField(name = "max_capacity")
        public int maxStudentsCount;


        @JSONField(name = "signed_price")
        public int signedPrice;

        @JSONField(name = "sell_price")
        public int price;

        //课程安排描述
        @JSONField(name = "desc")
        public String personalCourseDesc;

        //课程安排
        public String arrangement;

        //场馆名称
        @JSONField(name = "gym_name")
        public String gymName;

        //换购过程
        public String process;

        @JSONField(name = "coach_id")
        public long coachId;

        @JSONField(name = "coach_name")
        public String coachName;

        @JSONField(name = "coach_gender")
        public int coachSex;

        @JSONField(name = "coach_age")
        public int coachAge;
    }
}
