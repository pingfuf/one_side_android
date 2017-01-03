package com.oneside.model;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.WebUtils;

import static com.oneside.utils.WebUtils.*;

public class CardCircleComments {

    private static final String KEY_CIRCLE_REPLY_ID = "rid";
    private static final String KEY_CIRCLE_MESSAGE_ID = "msg_id";
    private static final String KEY_CIRCLE_USR_CONTENT = "content";
    private static final String KEY_CIRCLE_IS_AUTHOR = "is_author";
    private static final String KEY_CIRCLE_USR_TIME = "create_time";

    private static final String KEY_CIRCLE_USER = "user";
    private static final String KEY_CIRCLE_USR_LOGO = "avatar";
    private static final String KEY_CIRCLE_USR_SEX = "gender";//
    private static final String KEY_CIRCLE_USR_AGE = "age_type";//
    private static final String KEY_CIRCLE_USER_ID = "uid";
    private static final String KEY_CIRCLE_USR_NAME = "uname";
    private static final String KEY_CIRCLE_REPLY_TO = "reply_to";
    private static final String KEY_CIRCLE_IS_REPLY = "is_reply";

    private String mUsrLogo, mUsrName, mCommentsTime, mToName, mContent;
    private int mUsrID = -1;
    private int mCircleID = -1;
    private long mMessageID = -1l;
    private int mUsrSex = 0, mUsrAge = -1;
    private boolean isAuthor = false;
    private int isReply = 0;

    public CardCircleComments(long MessageID) {
        mMessageID = MessageID;
    }

    public static CardCircleComments fromJson(JSONObject json) {
        if (json == null || json.size() == 0)
            return null;
        int mCircleID = getJsonInt(json, KEY_CIRCLE_REPLY_ID, -1);
        long mMessageID = getJsonLong(json, KEY_CIRCLE_MESSAGE_ID, -1l);

        String mCommentsTime = getJsonString(json, KEY_CIRCLE_USR_TIME, "");
        String mContent = getJsonString(json, KEY_CIRCLE_USR_CONTENT, "");
        boolean isAuthor = (getJsonInt(json, KEY_CIRCLE_IS_AUTHOR, 0) == 1) ? true : false;
        int isReply = getJsonInt(json, KEY_CIRCLE_IS_REPLY, 0);

        JSONObject userJson = WebUtils.getJsonObject(json, KEY_CIRCLE_USER);
        String mUsrLogo = getJsonString(userJson, KEY_CIRCLE_USR_LOGO, "");
        int mUsrSex = getJsonInt(userJson, KEY_CIRCLE_USR_SEX, 0);
        String mUsrName = getJsonString(userJson, KEY_CIRCLE_USR_NAME, "");
        int mUsrID = getJsonInt(userJson, KEY_CIRCLE_USER_ID, -1);
        int mUsrAge = getJsonInt(userJson, KEY_CIRCLE_USR_AGE, -1);

        CardCircleComments ccc = new CardCircleComments(mMessageID);
        ccc.setAge(mUsrAge);
        ccc.setAuthor(isAuthor);
        ccc.setCircleID(mCircleID);
        ccc.setContent(mContent);
        if (isReply == 1) {
            JSONObject toJson = WebUtils.getJsonObject(json, KEY_CIRCLE_REPLY_TO);
            String mToUsrName = getJsonString(toJson, KEY_CIRCLE_USR_NAME, "");
            ccc.setToName(mToUsrName);
        }
        ccc.setLogo(mUsrLogo);
        ccc.setIsReply(isReply);
        ccc.setUserID(mUsrID);
        ccc.setName(mUsrName);
        ccc.setSex(mUsrSex);
        ccc.setTime(mCommentsTime);
        return ccc;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_CIRCLE_REPLY_ID, mCircleID);
            json.put(KEY_CIRCLE_MESSAGE_ID, mMessageID);
            json.put(KEY_CIRCLE_USR_LOGO, mUsrLogo);
            json.put(KEY_CIRCLE_USR_SEX, mUsrSex);
            json.put(KEY_CIRCLE_USR_NAME, mUsrName);
            json.put(KEY_CIRCLE_USR_AGE, mUsrAge);
            json.put(KEY_CIRCLE_USR_TIME, mCommentsTime);
            json.put(KEY_CIRCLE_USR_CONTENT, mContent);
            json.put(KEY_CIRCLE_IS_AUTHOR, isAuthor ? 1 : 0);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean isAuthor) {
        this.isAuthor = isAuthor;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public boolean isReply() {
        if (isReply == 1)
            return true;
        else
            return false;
    }

    public void setCircleID(int circleID) {
        mCircleID = circleID;
    }

    public int getCircleID() {
        return mCircleID;
    }

    public void setMessageID(long MessageID) {
        mMessageID = MessageID;
    }

    public long getMessageID() {
        return mMessageID;
    }

    public void setLogo(String logo) {
        mUsrLogo = logo;
    }

    public String getLogo() {
        return mUsrLogo;
    }

    public void setSex(int sex) {
        mUsrSex = sex;
    }

    public int getSex() {
        return mUsrSex;
    }

    public void setName(String name) {
        mUsrName = name;
    }

    public String getName() {
        return mUsrName;
    }

    public void setUserID(int mUsrID) {
        this.mUsrID = mUsrID;
    }

    public int getUserID() {
        return mUsrID;
    }

    public void setAge(int age) {
        mUsrAge = age;
    }

    public int getAge() {
        return mUsrAge;
    }

    public void setTime(String time) {
        mCommentsTime = time;
    }

    public String getTime() {
        return mCommentsTime;
    }

    public void setToName(String toName) {
        mToName = toName;
    }

    public String getToName() {
        return mToName;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getConent() {
        return mContent;
    }
}
