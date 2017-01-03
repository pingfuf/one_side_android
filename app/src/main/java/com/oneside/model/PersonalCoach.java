package com.oneside.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.LogUtils;
import com.oneside.utils.WebUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class PersonalCoach implements Serializable {
    private static final long serialVersionUID = -798674141174636569L;

    private static final String PERSONAL_COACH_NAME = "name";
    private static final String PERSONAL_COACH_TAGS = "tags";
    private static final String PERSONAL_COACH_GENDER = "gender";
    private static final String PERSONAL_COACH_AGE = "age";

    private static final String PERSONAL_COACH_PHONE = "phone";
    private static final String PERSONAL_GYM_GID = "gid";
    private static final String PERSONAL_COACH_CREDENTIALS = "credentials";
    private static final String PERSONAL_COACH_INTRO = "intro";
    private static final String PERSONAL_COACH_ID = "id";
    private static final String PERSONAL_COACH_IMG_URL = "img_url";
    /**
     * name : 教练昵称
     * tags : 教练tag
     * gender : 教练性别
     * age : 教练年龄
     * phone : 教练电话
     * gid : 教练所属场馆id
     * credentials : 教练认证
     * intro : 教练简介
     * id : 教练id
     * img_url : 头像url
     */

    private String name;
    private List<String> tags;
    private int gender;
    private int age;
    private String phone;
    private int gid;
    private String credentials;
    private String intro;
    private int id;
    private String img_url;

    public PersonalCoach(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonalCoach that = (PersonalCoach) o;

        if (gender != that.gender) return false;
        if (age != that.age) return false;
        if (gid != that.gid) return false;
        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (credentials != null ? !credentials.equals(that.credentials) : that.credentials != null)
            return false;
        if (intro != null ? !intro.equals(that.intro) : that.intro != null) return false;
        return !(img_url != null ? !img_url.equals(that.img_url) : that.img_url != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + gender;
        result = 31 * result + age;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + gid;
        result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
        result = 31 * result + (intro != null ? intro.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (img_url != null ? img_url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PersonalCoach{" +
                "name='" + name + '\'' +
                ", tags='" + tags + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", gid=" + gid +
                ", credentials='" + credentials + '\'' +
                ", intro='" + intro + '\'' +
                ", id=" + id +
                ", img_url='" + img_url + '\'' +
                '}';
    }

    public static PersonalCoach fromJson(JSONObject json) {
        if (json == null || json.size() == 0) {
            return null;
        }
        String name = WebUtils.getJsonString(json, PERSONAL_COACH_NAME, "");
        JSONArray tags = WebUtils.getJsonArray(json, PERSONAL_COACH_TAGS);
        int age = WebUtils.getJsonInt(json, PERSONAL_COACH_AGE, 4);
        int gender = WebUtils.getJsonInt(json, PERSONAL_COACH_GENDER, 1);
        String phone = WebUtils.getJsonString(json, PERSONAL_COACH_PHONE, "");
        int gid = WebUtils.getJsonInt(json, PERSONAL_GYM_GID, -1);
        String credentials = WebUtils.getJsonString(json, PERSONAL_COACH_CREDENTIALS, "");
        String intro = WebUtils.getJsonString(json, PERSONAL_COACH_INTRO, "");
        int id = WebUtils.getJsonInt(json, PERSONAL_COACH_ID, -1);
        String img_url = WebUtils.getJsonString(json, PERSONAL_COACH_IMG_URL, "");
        List<String> tag = new ArrayList<String>();
        if (null != tags)
            for (int i = 0; i < tags.size(); i++) {
                tag.add(WebUtils.getJsonString(tags, i));
            }
        PersonalCoach pc = new PersonalCoach(id);
        pc.setAge(age);
        pc.setCredentials(credentials);
        pc.setGender(gender);
        pc.setGid(gid);
        pc.setAvatar(img_url);
        pc.setIntro(intro);
        pc.setName(name);
        pc.setPhone(phone);
        pc.setTags(tag);
        return pc;
    }

    public JSONObject toJson() {
        try {
            JSONObject json = new JSONObject();
            json.put(PERSONAL_COACH_NAME, name);
            json.put(PERSONAL_COACH_AGE, age);
            json.put(PERSONAL_COACH_GENDER, gender);
            json.put(PERSONAL_COACH_IMG_URL, img_url);
            json.put(PERSONAL_COACH_CREDENTIALS, credentials);
            json.put(PERSONAL_COACH_PHONE, phone);
            json.put(PERSONAL_GYM_GID, gid);
            json.put(PERSONAL_COACH_ID, id);
            json.put(PERSONAL_COACH_INTRO, intro);
            json.put(PERSONAL_COACH_TAGS, tags);
            return json;
        } catch (Exception e) {
            LogUtils.e(e, "parse PersonalCoach failed");
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvatar(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public int getGid() {
        return gid;
    }

    public String getCredentials() {
        return credentials;
    }

    public String getIntro() {
        return intro;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return img_url;
    }
}
