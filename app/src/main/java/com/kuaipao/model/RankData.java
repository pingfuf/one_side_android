package com.kuaipao.model;


import com.alibaba.fastjson.JSONObject;

import static com.kuaipao.utils.WebUtils.*;

/**
 * Created by MVEN on 16/1/26.
 */
public class RankData implements Comparable<RankData> {

    /**
     * checkin : 1
     * rank_num : 1
     * calories : 272
     * large : http://www.xxkuaipao.com/img/thumb/198x198/1efdb3059f3649b79e56e01a03eb8749
     * small : http://www.xxkuaipao.com/img/thumb/132x132/1efdb3059f3649b79e56e01a03eb8749
     * medium : http://www.xxkuaipao.com/img/thumb/168x168/1efdb3059f3649b79e56e01a03eb8749
     * tiny : http://www.xxkuaipao.com/img/thumb/108x108/1efdb3059f3649b79e56e01a03eb8749
     * nickname : 我
     * id : 9783
     */
    private static String RANK_DATA_CHECKIN = "checkin";
    private static String RANK_DATA_RANK_NUM = "rank_num";
    private static String RANK_DATA_CALORIES = "calories";
    private static String RANK_DATA_IMG_LARGE = "large";
    private static String RANK_DATA_IMG_SMALL = "small";
    private static String RANK_DATA_IMG_MEDIUM = "medium";
    private static String RANK_DATA_IMG_TINY = "tiny";
    private static String RANK_DATA_USR_NICKNAME = "nickname";
    private static String RANK_DATA_USR_ID = "id";
    private int checkin;
    private int rank_num;
    private int calories;
    private String large;
    private String small;
    private String medium;
    private String tiny;
    private String nickname;
    private int id;
    private boolean isFollowed = false;

    public static RankData fromJson(JSONObject json) {
        int checkin = getJsonInt(json, RANK_DATA_CHECKIN, 0);
        int rank_num = getJsonInt(json, RANK_DATA_RANK_NUM, 1);
        int calories = getJsonInt(json, RANK_DATA_CALORIES, 0);
        JSONObject user = getJsonObject(json, "user");
//        String large = getJsonString(img, RANK_DATA_IMG_LARGE, "");
//        String small = getJsonString(img, RANK_DATA_IMG_SMALL, "");
//        String medium = getJsonString(img, RANK_DATA_IMG_MEDIUM, "");
//        String tiny = getJsonString(img, RANK_DATA_IMG_TINY, "");
        String nickname = getJsonString(user, RANK_DATA_USR_NICKNAME, "");
        String small = getJsonString(user, "headimg");
        int id = getJsonInt(user, RANK_DATA_USR_ID, 0);
        RankData rankData = new RankData();
        rankData.setCalories(calories);
        rankData.setCheckin(checkin);
        rankData.setId(id);
//        rankData.setLarge(large);
//        rankData.setMedium(medium);
        rankData.setSmall(small);
        rankData.setRank_num(rank_num);
//        rankData.setTiny(tiny);
        rankData.setNickname(nickname);
        return rankData;
    }

    public void setFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public void setCheckin(int checkin) {
        this.checkin = checkin;
    }

    public void setRank_num(int rank_num) {
        this.rank_num = rank_num;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public void setTiny(String tiny) {
        this.tiny = tiny;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCheckin() {
        return checkin;
    }

    public int getRank_num() {
        return rank_num;
    }

    public int getCalories() {
        return calories;
    }

    public String getLarge() {
        return large;
    }

    public String getSmall() {
        return small;
    }

    public String getMedium() {
        return medium;
    }

    public String getTiny() {
        return tiny;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    @Override
    public int compareTo(RankData another) {
        if (this.rank_num > another.rank_num)
            return 1;
        else if (this.rank_num < another.rank_num)
            return -1;
        else
            return 0;
    }
}
