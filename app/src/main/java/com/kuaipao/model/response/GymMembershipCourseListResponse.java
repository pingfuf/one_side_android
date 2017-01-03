package com.kuaipao.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResponseData;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.XCoach;

import java.io.Serializable;
import java.util.List;

/**
 * ERP课程列表返回结果
 *
 * Created by pingfu on 16-5-27.
 */
public class GymMembershipCourseListResponse extends BaseResult {
    public GymMembershipCourseListData data;

    public static class GymMembershipCourseListData extends BaseResponseData {
        //课程列表
        public List<GymMembershipCourse> courses;
    }

    /**
     * 场馆ERP课程返回数据结构
     */
    public static class GymMembershipCourse implements Serializable{
        public String id;
        public String name;
        public String label;
        @JSONField(name = "is_disabled")
        public boolean isDisabled;
        public String date;
        public String time;

        public MembershipCoach coach;
    }

    public static class MembershipCoach extends XCoach {
        @JSONField(name = "avatar")
        public String imgUrl;
    }
}
