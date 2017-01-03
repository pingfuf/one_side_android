package com.oneside.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.WebUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonalCourse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -798674141174636289L;


    // need to be confirmed with web!!!
    private static final String KEY_COURSE_ID = "id";
    private static final String KEY_MERCHANT_ID = "gym_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COURSE_TYPE = "course_type";
    private static final String KEY_DESC = "desc";
    private static final String KEY_COACH_ID = "coach_id";
    private static final String KEY_COACH_NAME = "coach_name";
    private static final String KEY_COACH_IMG = "coach_img";
    private static final String KEY_COACH_TYPE = "coach_type";
    private static final String KEY_COACH_GENDER = "coach_gender";
    private static final String KEY_COACH_AGE = "coach_age";
    private static final String KEY_AMOUNT = "price";

    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_MAX_CAPACITY = "max_capacity";
    private static final String KEY_ARRANGEMENT = "arrangement";

    private static final String KEY_TYPE_TIPS = "labels";


    private static final String KEY_REMAINING = "remaining";
    private static final String KEY_IS_ORDERED = "is_ordered";
    private static final String KEY_IS_CLASS_BOOKABLE = "is_bookable";

    private String courseID;
    private long merchantID;
    private String name;
    private String desc;
    private int courseType;//  0-'全部', 1-'器械健身', 2-游泳, 3-'瑜伽', 4-'舞蹈', 5-'单车'
    private int coachID;
    private String coachName;
    private String coachImg;

    private enum CoachType {
        COMMON, SINGLE, EXPERIENCE;
    }

    private int coachType;//0 普通课程; 1 私教课程; 2 私教体验课

    private int amount;
    private Date startTime;
    private Date endTime;
    private int maxCapacity;
    private String arrangement;
    private List<String> typeTips;

    private int gender;// 0 unknown; 1 man; 2 woman
    private int ageType;// -1: unknown; 0: 50s; 1: 60s; 2: 70s; 3: 80s; 4: 90s; 5: 00s

    private boolean isOrdered;// 已经预约过?
    private int remaining;// 剩余人数，约满为0， -1为不限制预约人数
    private boolean isClassBookable;// 课程本身是否可预约，预约已结束？

    private PersonalCourse(String classID) {
        this.courseID = classID;
    }

    @Override
    public String toString() {
        JSONObject j = toJson();
        return j == null ? super.toString() : j.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof PersonalCourse) {
            if (this.merchantID == (((PersonalCourse) o).getMerchantID())
                    && this.courseID.equals(((PersonalCourse) o).getCourseID())) {
                return true;
            }
        }
        return false;
    }

    public static PersonalCourse fromJson(JSONObject j) {
        if (j == null || j.size() == 0) {
            return null;
        }
        String classID = WebUtils.getJsonString(j, KEY_COURSE_ID, "");
        long merchantID = WebUtils.getJsonLong(j, KEY_MERCHANT_ID, 0l);
        String name = WebUtils.getJsonString(j, KEY_NAME, "");
        int courseType = WebUtils.getJsonInt(j, KEY_COURSE_TYPE, 0);
        String desc = WebUtils.getJsonString(j, KEY_DESC, "");
//
        int coachID = WebUtils.getJsonInt(j, KEY_COACH_ID, 0);
        String coachName = WebUtils.getJsonString(j, KEY_COACH_NAME, "");
        String coachImg = WebUtils.getJsonString(j, KEY_COACH_IMG, "");
        int coachType = WebUtils.getJsonInt(j, KEY_COACH_TYPE, 0);
        int amount = WebUtils.getJsonInt(j, KEY_AMOUNT, 0);

        String strStartTime = WebUtils.getJsonString(j, KEY_START_TIME, "");
        Date startTime = LangUtils.formatDate(strStartTime, "yyyy-MM-dd'T'HH:mm:ss");
        String strEndTime = WebUtils.getJsonString(j, KEY_END_TIME, "");
        Date endTime = LangUtils.formatDate(strEndTime, "yyyy-MM-dd'T'HH:mm:ss");
        int maxCapacity = WebUtils.getJsonInt(j, KEY_MAX_CAPACITY, 0);
        String arrangement = WebUtils.getJsonString(j, KEY_ARRANGEMENT, "");

        JSONArray typeTipsArray = WebUtils.getJsonArray(j, KEY_TYPE_TIPS);
        ArrayList<String> typeTipsList = (ArrayList<String>) WebUtils.jsonToArrayString(typeTipsArray);
        int gender = WebUtils.getJsonInt(j, KEY_COACH_GENDER, 0);
        int ageType = WebUtils.getJsonInt(j, KEY_COACH_AGE, -1);

        int remaining = WebUtils.getJsonInt(j, KEY_REMAINING, 0);
        boolean isClassBookable = WebUtils.getJsonBoolean(j, KEY_IS_CLASS_BOOKABLE, true);
        boolean isOrdered = WebUtils.getJsonBoolean(j, KEY_IS_ORDERED, false);

        PersonalCourse cc = new PersonalCourse(classID);
        cc.setMerchantID(merchantID);
        cc.setName(name);
        cc.setCourseType(courseType);
        cc.setDesc(desc);
        cc.setCoachID(coachID);
        cc.setCoachName(coachName);
        cc.setCoachImg(coachImg);
        cc.setCoachType(coachType);
        cc.setAmount(amount);
        cc.setStartTime(startTime);
        cc.setEndTime(endTime);
        cc.setMaxCapacity(maxCapacity);
        cc.setArrangement(arrangement);
        cc.setTypeTips(typeTipsList);
        cc.setGender(gender);
        cc.setAgeType(ageType);
        cc.setRemaining(remaining);
        cc.setIsOrdered(isOrdered);
        cc.setIsClassBookable(isClassBookable);
        return cc;
    }

    public JSONObject toJson() {
        try {
            JSONObject json = new JSONObject();
            json.put(KEY_COURSE_ID, this.courseID);
            json.put(KEY_MERCHANT_ID, this.merchantID);
            json.put(KEY_NAME, this.name);
            json.put(KEY_COURSE_TYPE, this.courseType);
            json.put(KEY_DESC, this.desc);

            json.put(KEY_COACH_ID, this.coachID);
            json.put(KEY_COACH_NAME, this.coachName);
            json.put(KEY_COACH_IMG, this.coachImg);
            json.put(KEY_COACH_TYPE, this.coachType);
            json.put(KEY_AMOUNT, this.amount);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            json.put(KEY_START_TIME, format.format(this.startTime));
            json.put(KEY_END_TIME, format.format(this.endTime));
            json.put(KEY_MAX_CAPACITY, this.maxCapacity);
            json.put(KEY_ARRANGEMENT, this.arrangement);
            json.put(KEY_TYPE_TIPS, WebUtils.java2jsonValue(this.typeTips));

            json.put(KEY_COACH_GENDER, gender);
            json.put(KEY_COACH_AGE, ageType);

            json.put(KEY_REMAINING, this.remaining);
            json.put(KEY_IS_CLASS_BOOKABLE, this.isClassBookable);
            json.put(KEY_IS_ORDERED, this.isOrdered);
            return json;
        } catch (Exception e) {
            LogUtils.w(e, " parse CardClass serial error");

        }
        return null;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public long getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(long merchantID) {
        this.merchantID = merchantID;
    }


    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getCoachImg() {
        return coachImg;
    }

    public void setCoachImg(String coachImg) {
        this.coachImg = coachImg;
    }

    public int getCoachType() {
        return coachType;
    }

    public void setCoachType(int coachType) {
        this.coachType = coachType;
    }

//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

//    public double getSinglePrice() {
//        return singlePrice;
//    }
//
//    public void setSinglePrice(double singlePrice) {
//        this.singlePrice = singlePrice;
//    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getArrangement() {
        return arrangement;
    }

    public void setArrangement(String arrangement) {
        this.arrangement = arrangement;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAgeType() {
        return ageType;
    }

    public void setAgeType(int ageType) {
        this.ageType = ageType;
    }

    public void generateTypeTips() {
        if (this.typeTips == null)
            this.typeTips = new ArrayList<String>();
        if (this.maxCapacity >= 1) {
            this.typeTips.add(0, String.format("1V%d", this.maxCapacity));
        }
    }

    public List<String> getTypeTips() {
        return typeTips;
    }

    public void setTypeTips(List<String> typeTips) {
        this.typeTips = typeTips;
        generateTypeTips();
    }


    public boolean isExperienceCourse() {
        return this.coachType == CoachType.EXPERIENCE.ordinal();
    }

    public boolean isSingCourse() {
        return this.coachType == CoachType.SINGLE.ordinal();
    }


    public boolean isOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public boolean isClassBookable() {
        return isClassBookable;
    }

    public void setIsClassBookable(boolean isClassBookable) {
        this.isClassBookable = isClassBookable;
    }
}
