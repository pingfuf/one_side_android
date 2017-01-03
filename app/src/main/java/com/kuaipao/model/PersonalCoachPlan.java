package com.kuaipao.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.kuaipao.utils.WebUtils.*;

/**
 * Created by Magi on 2015/11/23.
 */
public class PersonalCoachPlan implements Serializable {


    private static final long serialVersionUID = -2777989633878829946L;
    /**
     * name : ceshi
     * max_capacity : 10
     * course_type : 1
     * amount : 100
     * id : 1
     * desc : 描述
     */

    private static final String COACH_PLAN_NAME = "name";
    private static final String COACH_PLAN_MAX_CAPACITY = "max_capacity";
    private static final String COACH_PLAN_COURSE_TYPE = "course_type";
    private static final String COACH_PLAN_AMOUNT = "sell_price";
    private static final String COACH_PLAN_ID = "id";
    private static final String COACH_PLAN_DESC = "desc";
    private static final String COACH_PLAN_LABELS = "labels";
    private String name;
    private int max_capacity;
    private int course_type;
    private float amount;
    private int id;
    private String desc;
    private List<String> labels;


    public static PersonalCoachPlan fromJson(JSONObject json) {
        if (json == null || json.size() == 0)
            return null;
        String name = getJsonString(json, COACH_PLAN_NAME, "");
        int max_capacity = getJsonInt(json, COACH_PLAN_MAX_CAPACITY, 1);
        int course_type = getJsonInt(json, COACH_PLAN_COURSE_TYPE, 1);
        int amount = getJsonInt(json, COACH_PLAN_AMOUNT, -1);
        int id = getJsonInt(json, COACH_PLAN_ID, -1);
        String desc = getJsonString(json, COACH_PLAN_DESC, "");
        List<String> labels = new ArrayList<String>();
        JSONArray array = getJsonArray(json, COACH_PLAN_LABELS);
        if (array != null)
            for (int i = 0; i < array.size(); i++) {
                String j = (String) array.get(i);
                labels.add(j);
            }
        PersonalCoachPlan coachPlan = new PersonalCoachPlan();
        coachPlan.setLabels(labels);
        coachPlan.setAmount(((float) amount) / 100);
        coachPlan.setCourse_type(course_type);
        coachPlan.setDesc(desc);
        coachPlan.setId(id);
        coachPlan.setMax_capacity(max_capacity);
        coachPlan.setName(name);
        return coachPlan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public void setCourse_type(int course_type) {
        this.course_type = course_type;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public int getCourse_type() {
        return course_type;
    }

    public float getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public String getDesc() {
        return desc;
    }
}
