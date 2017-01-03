package com.oneside.model;


import com.alibaba.fastjson.JSONObject;

import static com.oneside.utils.WebUtils.*;

/**
 * Created by MVEN on 16/3/7.
 */
public class MerchantFans {

    /**
     * checkin_count : 12
     * headimg : http:76b813402a956991929e0dbcfd
     * nickname : willxu
     * id : 30519
     */

    private static final String MERCHANT_CHECKIN_COUNT = "checkin_count";
    private static final String MERCHANT_HEADIMG = "headimg";
    private static final String MERCHANT_NICKNAME = "nickname";
    private static final String MERCHANT_FANS_ID = "id";
    private static final String MERCHANT_IS_FOLLOWED = "is_followed";
    private int checkin_count;
    private String headimg;
    private String nickname;
    private int id;
    private boolean isFollowed = false;

    public static MerchantFans fromJson(JSONObject json) {
        MerchantFans mf = new MerchantFans();
        int checkin_count = getJsonInt(json, MERCHANT_CHECKIN_COUNT, -1);
        String headimg = getJsonString(json, MERCHANT_HEADIMG, "");
        String nickname = getJsonString(json, MERCHANT_NICKNAME, "");
        int id = getJsonInt(json, MERCHANT_FANS_ID, -1);
        boolean isFollowed = getJsonBoolean(json, MERCHANT_IS_FOLLOWED, false);
        mf.setFollowed(isFollowed);
        mf.setCheckinCount(checkin_count);
        mf.setHeadimg(headimg);
        mf.setId(id);
        mf.setNickname(nickname);
        return mf;
    }

    public void setFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public void setCheckinCount(int checkin_count) {
        this.checkin_count = checkin_count;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCheckinCount() {
        return checkin_count;
    }

    public String getHeadimg() {
        return headimg;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isFollowed() {
        return isFollowed;

    }

    public int getId() {
        return id;
    }
}
