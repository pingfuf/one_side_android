package com.oneside.model;


import com.alibaba.fastjson.JSONObject;

import static com.oneside.utils.WebUtils.*;

/**
 * Created by MVEN on 16/1/26.
 */
public class UserHomePhotoImg {
    private static final String USER_HOME_PHOTO_WIDTH = "width";
    private static final String USER_HOME_PHOTO_HEIGHT = "height";
    private static final String USER_HOME_PHOTO_IMG_KEY = "img_key";
    private static final String USER_HOME_PHOTO_THUMB = "thumb";
    private static final String USER_HOME_PHOTO_ORIGINAL = "original";
    private int width;
    private int height;
    private String img_key;
    private String thumb;
    private String original;

    public static UserHomePhotoImg fromJson(JSONObject json) {
        int width = getJsonInt(json, USER_HOME_PHOTO_WIDTH, 0);
        int height = getJsonInt(json, USER_HOME_PHOTO_HEIGHT, 0);
        String img_key = getJsonString(json, USER_HOME_PHOTO_IMG_KEY, "");
        String thumb = getJsonString(json, USER_HOME_PHOTO_THUMB, "");
        String original = getJsonString(json, USER_HOME_PHOTO_ORIGINAL, "");
        UserHomePhotoImg img = new UserHomePhotoImg();
        img.setHeight(height);
        img.setImg_key(img_key);
        img.setOriginal(original);
        img.setThumb(thumb);
        img.setWidth(width);
        return img;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImg_key() {
        return img_key;
    }

    public void setImg_key(String img_key) {
        this.img_key = img_key;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
