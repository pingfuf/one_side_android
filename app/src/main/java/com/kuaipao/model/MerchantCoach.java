package com.kuaipao.model;

import com.alibaba.fastjson.JSONObject;

import static com.kuaipao.utils.WebUtils.*;

/**
 * Created by MVEN on 16/3/9.
 */
public class MerchantCoach {

    /**
     * imgUrl : http://www.xxkuaipao.com/img/7eb008b588d74d369d87 293d6c99c333
     * id : 48
     * name :
     */
    private static final String MERCHANT_COACH_IMG_URL = "img_url";
    private static final String MERCHANT_COACH_NAME = "name";
    private static final String MERCHANT_COACH_ID = "id";

    private String imgUrl;
    private int id;
    private String name;

    public static MerchantCoach fromJson(JSONObject json) {
        MerchantCoach mc = new MerchantCoach();
        String imgUrl;
        int id;
        String name;
        imgUrl = getJsonString(json, MERCHANT_COACH_IMG_URL, "");
        id = getJsonInt(json, MERCHANT_COACH_ID, -1);
        name = getJsonString(json, MERCHANT_COACH_NAME, "");
        mc.setId(id);
        mc.setImgUrl(imgUrl);
        mc.setName(name);
        return mc;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
