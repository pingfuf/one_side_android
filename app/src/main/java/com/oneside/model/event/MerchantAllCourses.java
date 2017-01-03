package com.oneside.model.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oneside.model.MerchantCourse;
import com.oneside.utils.WebUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/5/24.
 */
public class MerchantAllCourses extends WebBaseEvent {

    List<MerchantCourse> courses;
    List<MerchantCourse> coachCourses;

    public MerchantAllCourses(boolean bResult) {
        super(bResult);
        if (courses == null)
            courses = new ArrayList<>();
        if (coachCourses == null) {
            coachCourses = new ArrayList<>();
        }
    }

    public static MerchantAllCourses fromJson(JSONObject json) {
        if (json == null)
            return new MerchantAllCourses(true);

        List<MerchantCourse> courses = new ArrayList<>();
        List<MerchantCourse> coachCourses = new ArrayList<>();

        MerchantAllCourses mac = new MerchantAllCourses(true);

        JSONArray courseArray = WebUtils.getJsonArray(json, "courses");
        int len;
        if (courseArray != null) {
            len = courseArray.size();
            for (int i = 0; i < len; i++) {
                courses.add(MerchantCourse.fromJson((JSONObject) courseArray.get(i)));
            }
        }
        JSONArray coachCourseArray = WebUtils.getJsonArray(json, "coach_courses");
        if (coachCourseArray != null) {
            len = coachCourseArray.size();
            for (int i = 0; i < len; i++) {
                coachCourses.add(MerchantCourse.fromJson((JSONObject) coachCourseArray.get(i)));
            }
        }
        mac.setCoachCourses(coachCourses);
        mac.setCourses(courses);
        return mac;
    }

    private void setCourses(List<MerchantCourse> courses) {
        this.courses = courses;
    }

    private void setCoachCourses(List<MerchantCourse> coachCourses) {
        this.coachCourses = coachCourses;
    }

    public List<MerchantCourse> getCourses() {
        return courses;
    }

    public List<MerchantCourse> getCoachCourses() {
        return coachCourses;
    }
}
