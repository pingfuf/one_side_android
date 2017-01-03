package com.oneside.model;

import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.LogUtils;
import com.oneside.utils.WebUtils;

import java.io.Serializable;

/**
 * Created by Magi on 2015/12/3.
 */
public class Fans implements Serializable {

    private static final String FANS_SIGNATURE = "bio";
    private static final String FANS_AGE = "age";
    private static final String FANS_LOGO = "headimg";
    private static final String FANS_NICKNAME = "nickname";
    private static final String FANS_ID = "id";
    private static final String FANS_GENDER = "sex";
    private static final String FANS_FOLLOWED = "is_followed";
    /**
     * name : 昵称
     * gender : 性别
     * age : 年龄
     * intro : 简介
     * id : id
     * img_url : 头像url
     */

    private String name;
    private int gender;
    private int age;
    private String intro;
    private int id;
    private String img_url;
    private boolean is_followed;

    public Fans(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fans that = (Fans) o;

        if (gender != that.gender) return false;
        if (age != that.age) return false;
        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (intro != null ? !intro.equals(that.intro) : that.intro != null) return false;
        return !(img_url != null ? !img_url.equals(that.img_url) : that.img_url != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + gender;
        result = 31 * result + age;
        result = 31 * result + (intro != null ? intro.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (img_url != null ? img_url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PersonalCoach{" +
                "is_followed" + String.valueOf(is_followed) + '\'' +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", intro='" + intro + '\'' +
                ", id=" + id +
                ", img_url='" + img_url + '\'' +
                '}';
    }

    public static Fans fromJson(JSONObject json) {
        if (json == null || json.size() == 0) {
            return null;
        }
        String name = WebUtils.getJsonString(json, FANS_NICKNAME, "");
        int age = WebUtils.getJsonInt(json, FANS_AGE, 4);
        int gender = WebUtils.getJsonInt(json, FANS_GENDER, 1);
        String intro = WebUtils.getJsonString(json, FANS_SIGNATURE, "");
        int id = WebUtils.getJsonInt(json, FANS_ID, -1);
        String img_url = WebUtils.getJsonString(json, FANS_LOGO, "");
        boolean is_followed = WebUtils.getJsonBoolean(json, FANS_FOLLOWED, false);
        Fans pc = new Fans(id);
        pc.setAge(age);
        pc.setFollowed(is_followed);
        pc.setGender(gender);
        pc.setAvatar(img_url);
        pc.setIntro(intro);
        pc.setName(name);
        return pc;
    }

    public JSONObject toJson() {
        try {
            JSONObject json = new JSONObject();
            json.put(FANS_NICKNAME, name);
            json.put(FANS_AGE, age);
            json.put(FANS_GENDER, gender);
            json.put(FANS_LOGO, img_url);
            json.put(FANS_ID, id);
            json.put(FANS_FOLLOWED, is_followed);
            json.put(FANS_SIGNATURE, intro);
            return json;
        } catch (Exception e) {
            LogUtils.e(e, "parse PersonalCoach failed");
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFollowed(boolean is_followed) {
        this.is_followed = is_followed;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
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


    public int getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public boolean isFollowed() {
        return is_followed;
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
