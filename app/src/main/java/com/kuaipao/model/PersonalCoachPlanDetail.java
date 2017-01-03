package com.kuaipao.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

import static com.kuaipao.utils.WebUtils.*;

/**
 * Created by Magi on 2015/11/23.
 */
public class PersonalCoachPlanDetail implements Serializable {

    private static final long serialVersionUID = 2618737375780339719L;


    /**
     * gym_name : 超越先锋健身（回龙观店）
     * coach_id : 70b475d1ffcf4e598b0fab1c629a9e5f
     * process : 兑换流程
     * id : 1
     * desc : 描述
     * name : ceshi
     * coach_name : '测试教练123123'
     * max_capacity : 10
     * arrangement : 安排
     * course_type : 1 # [u'器械健身', u'游泳', u'瑜伽', u'舞蹈', u'单车', u'武术'] '器械健身为1，以此类推
     * amount : 100
     */

    private static final String COACH_ID = "coach_id";
    private static final String COACH_GYM_NAME = "gym_name";
    private static final String COACH_PROCESS = "process";
    private static final String COACH_DESC = "desc";
    private static final String COACH_PLAN_ID = "id";
    private static final String COACH_NAME = "name";
    private static final String COACH_COACH_NAME = "coach_name";
    private static final String COACH_MAX_CAPACITY = "max_capacity";
    private static final String COACH_ARRANGEMENT = "arrangement";
    private static final String COACH_COURSE_TYPE = "course_type";
    private static final String COACH_GENDER = "coach_gender";
    private static final String COACH_AGE = "coach_age";
    private static final String COACH_SIGNED_PRICE = "signed_price";
    private static final String COACH_SELL_PRICE = "sell_price";
    private static final String COACH_INTRODUCE = "introduce";

    private String gym_name;
    private String coach_id;
    private String process;
    private String id;
    private String desc;
    private String name;
    private String coach_name;
    private int coach_gender;
    private int coach_age;
    private int max_capacity;
    private String arrangement;
    private int course_type;
    private float sell_price;
    private float signed_price;
    private String introduce;


    public static PersonalCoachPlanDetail fromJson(JSONObject json) {
        if (json == null || json.size() == 0)
            return null;
        int coach_gender = getJsonInt(json, COACH_GENDER, 1);
        int coach_age = getJsonInt(json, COACH_AGE, 1);
        String coach_id = getJsonString(json, COACH_ID, "");
        String gym_name = getJsonString(json, COACH_GYM_NAME, "");
        String process = getJsonString(json, COACH_PROCESS, "");
        String id = getJsonString(json, COACH_PLAN_ID, "");
        String desc = getJsonString(json, COACH_DESC, "");
        String name = getJsonString(json, COACH_NAME, "");
        String coach_name = getJsonString(json, COACH_COACH_NAME, "");
        int max_capacity = getJsonInt(json, COACH_MAX_CAPACITY, 1);
        String arrangement = getJsonString(json, COACH_ARRANGEMENT, "");
        int course_type = getJsonInt(json, COACH_COURSE_TYPE, 1);
        int sell_price = getJsonInt(json, COACH_SELL_PRICE, -1);
        int signed_price = getJsonInt(json, COACH_SIGNED_PRICE, -1);
        String introduce = getJsonString(json, COACH_INTRODUCE, "");
        PersonalCoachPlanDetail p = new PersonalCoachPlanDetail();
        p.setSell_price(((float) sell_price) / 100);
        p.setSigned_price(((float) signed_price) / 100);
        p.setIntroduce(introduce);
        p.setCourse_type(course_type);
        p.setDesc(desc);
        p.setId(id);
        p.setMax_capacity(max_capacity);
        p.setName(name);
        p.setArrangement(arrangement);
        p.setCoach_name(coach_name);
        p.setCoach_age(coach_age);
        p.setCoach_gender(coach_gender);
        p.setProcess(process);
        p.setGym_name(gym_name);
        p.setCoach_id(coach_id);
        return p;
    }

    public void setGym_name(String gym_name) {
        this.gym_name = gym_name;
    }

    public void setSell_price(float sell_price) {
        this.sell_price = sell_price;
    }

    public void setSigned_price(float signed_price) {
        this.signed_price = signed_price;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoach_name(String coach_name) {
        this.coach_name = coach_name;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public void setCoach_age(int coach_age) {
        this.coach_age = coach_age;
    }

    public void setCoach_gender(int coach_gender) {
        this.coach_gender = coach_gender;
    }

    public void setArrangement(String arrangement) {
        this.arrangement = arrangement;
    }

    public void setCourse_type(int course_type) {
        this.course_type = course_type;
    }


    public String getGym_name() {
        return gym_name;
    }

    public float getSell_price() {
        return sell_price;
    }

    public float getSigned_price() {
        return signed_price;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public String getProcess() {
        return process;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public String getArrangement() {
        return arrangement;
    }

    public int getCourse_type() {
        return course_type;
    }


    public int getCoach_gender() {
        return coach_gender;
    }

    public int getCoach_age() {
        return coach_age;
    }

}
