package com.oneside.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.oneside.utils.WebUtils.*;
import static com.oneside.utils.LangUtils.*;

/**
 * Created by Magi on 2015/11/24.
 */
public class PersonalCourseList implements Serializable {
    private static final long serialVersionUID = 8897914708666828379L;
    private static final String KEY_COURSE_TYPE = "course_type";
    private static final String KEY_DESC = "desc";
    private static final String KEY_COACH_TYPE = "coach_type";
    private static final String KEY_PRICE = "amount";
    private static final String KEY_NAME = "name";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_COURSE_ID = "id";
    private static final String KEY_MAX_CAPACITY = "max_capacity";
    private static final String KEY_LABELS = "labels";
    /**
     * course_type : 1 #课程类型
     * amount : 12 #价格
     * name : 测试 # 名称
     * desc : 哈哈哈 # 描述
     * start_time : 2015-11-21T09:00:00
     * coach_type : 1 # 0 普通课程 1 私教课程 2 私教体验课
     * id : 7b62c53ab3d44d63874da7a8d36beb62_1448067600
     * end_time : 2015-11-21T19:00:00
     */
    private List<String> labels;
    private int course_type;
    private float amount;
    private String name;
    private String desc;
    private Date start_time;
    private int coach_type;
    private String id;
    private Date end_time;
    private int max_capacity;

    public static PersonalCourseList fromJson(JSONObject json) {
        if (json == null || json.size() == 0)
            return null;

        int course_type = getJsonInt(json, KEY_COURSE_TYPE);
        int amount = getJsonInt(json, KEY_PRICE);
        String name = getJsonString(json, KEY_NAME);
        String desc = getJsonString(json, KEY_DESC);
        String start_time = getJsonString(json, KEY_START_TIME);
        int coach_type = getJsonInt(json, KEY_COACH_TYPE);
        String id = getJsonString(json, KEY_COURSE_ID);
        JSONArray array = getJsonArray(json, KEY_LABELS);
        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++) {
            String j = (String) array.get(i);
            labels.add(j);
        }
        String end_time = getJsonString(json, KEY_END_TIME);
        int max_capacity = getJsonInt(json, KEY_MAX_CAPACITY);
        PersonalCourseList pc = new PersonalCourseList();
        if (isNotEmpty(start_time))
            pc.setStart_time(formatTypeNormalDate(start_time));
        if (isNotEmpty(end_time))
            pc.setEnd_time(formatTypeNormalDate(end_time));
        pc.setLabels(labels);
        pc.setName(name);
        pc.setId(id);
        pc.setDesc(desc);
        pc.setMax_capacity(max_capacity);
        pc.setCourse_type(course_type);
        pc.setAmount(((float) amount) / 100);
        pc.setCoach_type(coach_type);
        return pc;
    }

    public void setCourse_type(int course_type) {
        this.course_type = course_type;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public void setCoach_type(int coach_type) {
        this.coach_type = coach_type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public int getCourse_type() {
        return course_type;
    }

    public float getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public String getDesc() {
        return desc;
    }

    public Date getStart_time() {
        return start_time;
    }

    public int getCoach_type() {
        return coach_type;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public String getId() {
        return id;
    }

    public Date getEnd_time() {
        return end_time;
    }
}
