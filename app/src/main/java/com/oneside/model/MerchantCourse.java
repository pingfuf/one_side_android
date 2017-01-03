package com.oneside.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.WebUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/5/24.
 */
public class MerchantCourse {

    //场馆课程列表数据结构 used in MerchantInfoBookFragment
    /**
     * course_type : 2
     * coach : {"name":"王骏","img":"http://www.xxoneside.com/img/thumb/128x128/fc1b727ed9eb471caf1b3344c1a13d4a"}
     * name : 私教课1
     * max_capacity : -1
     * time : 14:00 - 20:00
     * date : 05月18日 星期三
     * labels : []
     * is_disabled : false
     * id : 73658175d2814ab69608613ac3d72e1c_1463544000
     * desc : ￥1
     */

    private int course_type;

    private CoachEntity coach;
    private String name;
    private int max_capacity;
    private String time;
    private String date;
    private boolean is_disabled;
    private String id;
    private String desc;
    private List<String> labels;

    public static MerchantCourse fromJson(JSONObject j) {
        MerchantCourse mc = new MerchantCourse();
        int course_type = WebUtils.getJsonInt(j, "course_type", -1);

        CoachEntity coach = null;
        JSONObject coachJson = WebUtils.getJsonObject(j, "coach");
        if (coachJson != null) {
            coach = new CoachEntity();
            coach.setImg(WebUtils.getJsonString(coachJson, "img", ""));
            coach.setName(WebUtils.getJsonString(coachJson, "name", ""));
        }
        String name = WebUtils.getJsonString(j, "name", "");
        int max_capacity = WebUtils.getJsonInt(j, "max_capacity", -1);
        String time = WebUtils.getJsonString(j, "time", "");
        String date = WebUtils.getJsonString(j, "date", "");
        boolean is_disabled = WebUtils.getJsonBoolean(j, "is_disabled", false);
        String id = WebUtils.getJsonString(j, "id", "");
        String desc = WebUtils.getJsonString(j, "desc", "");
        List<String> labels = new ArrayList<>();
        JSONArray array = WebUtils.getJsonArray(j, "labels");
        if (array != null) {
            int len = array.size();
            for (int i = 0; i < len; i++) {
                String tmp = array.get(i).toString();
                labels.add(tmp);
            }
        }
        mc.setName(name);
        mc.setCoach(coach);
        mc.setCourse_type(course_type);
        mc.setDate(date);
        mc.setDesc(desc);
        mc.setId(id);
        mc.setIs_disabled(is_disabled);
        mc.setLabels(labels);
        mc.setMax_capacity(max_capacity);
        mc.setTime(time);
        return mc;
    }

    public int getCourse_type() {
        return course_type;
    }

    public void setCourse_type(int course_type) {
        this.course_type = course_type;
    }

    public CoachEntity getCoach() {
        return coach;
    }

    public void setCoach(CoachEntity coach) {
        this.coach = coach;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(boolean is_disabled) {
        this.is_disabled = is_disabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<?> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public static class CoachEntity {
        private String name;
        private String img;

        /**
         * name : 王骏
         * img : http://www.xxoneside.com/img/thumb/128x128/fc1b727ed9eb471caf1b3344c1a13d4a
         */

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
