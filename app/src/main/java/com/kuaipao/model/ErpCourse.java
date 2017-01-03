package com.kuaipao.model;

import com.alibaba.fastjson.JSONObject;

import static com.kuaipao.utils.WebUtils.*;

/**
 * Created by MVEN on 16/5/24.
 */
public class ErpCourse {

    private static final String COACH_ID = "id";
    private static final String COACH_AVATAR = "avatar";

    private static final String COACH_NAME = "name";
    private static final String COURSE_NAME = "name";
    private static final String COURSE_IS_DISABLED = "is_disabled";
    private static final String COURSE_LABEL = "label";
    private static final String COURSE_TIME = "time";
    private static final String COURSE_DATE = "date";
    private static final String ID = "id";


    private int coachID;
    private String avatar;
    private String coachName;
    private String courseName;
    private boolean isDisabled = false;
    private String courselabel;
    private String courseTime;
    private String courseDate;
    private int id;

    private ErpCourse fromJson(JSONObject j) {
        ErpCourse erpCourse = new ErpCourse();
        JSONObject coach = getJsonObject(j, "coach");
        if (coach != null) {
            int coachID = getJsonInt(coach, COACH_ID, -1);
            String avatar = getJsonString(coach, COACH_AVATAR, "");
            String coachname = getJsonString(coach, COACH_NAME, "");
            erpCourse.setCoachID(coachID);
            erpCourse.setAvatar(avatar);
            erpCourse.setCoachName(coachname);
        }
        String courseName = getJsonString(j, COURSE_NAME, "");
        boolean isDisabled = getJsonBoolean(j, COURSE_IS_DISABLED, false);
        String courselabel = getJsonString(j, COURSE_LABEL, "");
        String courseTime = getJsonString(j, COURSE_TIME, "");
        String courseDate = getJsonString(j, COURSE_DATE, "'");
        int id = getJsonInt(j, ID, -1);
        erpCourse.setCourseDate(courseDate);
        erpCourse.setCoachID(id);
        erpCourse.setCourselabel(courselabel);
        erpCourse.setCourseName(courseName);
        erpCourse.setCourseTime(courseTime);
        erpCourse.setDisabled(isDisabled);
        return erpCourse;
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public String getCourselabel() {
        return courselabel;
    }

    public void setCourselabel(String courselabel) {
        this.courselabel = courselabel;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
