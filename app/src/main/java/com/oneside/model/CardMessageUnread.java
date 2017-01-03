package com.oneside.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.utils.WebUtils;

public class CardMessageUnread implements Serializable {
    private static final long serialVersionUID = -3710259299760138273L;

    private static final String KEY_FEED_ID = "id"; // feed id
    private static final String KEY_MSG_ID = "msg_id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TIME = "time";

    private static final String KEY_REPLY = "reply";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_REPLY_ID = "rid";
    private static final String KEY_IS_REPLY = "is_reply";
    private static final String KEY_REPLY_TO_USER = "reply_to";

    private static final String KEY_USER = "user";
    private static final String KEY_USER_NAME = "uname";
    private static final String KEY_USER_AVATAR = "avatar";
    private static final String KEY_USER_ID = "uid";
    private static final String KEY_USER_AGE_TYPE = "age_type";
    private static final String KEY_CIRCLE_SEX = "gender";

    private long feedID = -1;
    private long msgID = -1;
    private String content;
    private int type;//1-like the msg; 2-reply the msg;3follow reminider
    private String time;//format like: '2015-09-11T16:46:36'
    private boolean isReply;//0 or 1, if reply to other user, 1
    private int replyID = -1;
    private String userName;
    private String userLogo;
    private int userAgeType;
    private int userGender;// 0 unknown; 1 man; 2 woman
    private int userID;

    private int replyToUserID;
    private String replyToUserName;
    private String replyToUserLogo;
    private int replyToUserAgeType;


    public CardMessageUnread(long feedID) {
        this.feedID = feedID;
    }

    public static CardMessageUnread fromJson(JSONObject j) {
        if (j == null || j.size() == 0) {
            return null;
        }

        long feedID = WebUtils.getJsonLong(j, KEY_FEED_ID, -1l);
        long msgID = WebUtils.getJsonLong(j, KEY_MSG_ID, -1l);
        int type = WebUtils.getJsonInt(j, KEY_TYPE, -1);
        String time = WebUtils.getJsonString(j, KEY_TIME);

        CardMessageUnread cc = new CardMessageUnread(feedID);
        cc.setFeedID(feedID);
        cc.setMsgID(msgID);
        cc.setType(type);
        cc.setTime(time);

        if (type == 2) {
            JSONObject replyJson = WebUtils.getJsonObject(j, KEY_REPLY);
            if (replyJson != null) {
                String content = WebUtils.getJsonString(replyJson, KEY_CONTENT);
                boolean isReply = WebUtils.getJsonInt(replyJson, KEY_IS_REPLY, 0) == 1 ? true : false;
                int replyID = WebUtils.getJsonInt(replyJson, KEY_REPLY_ID, -1);

                cc.setContent(content);
                cc.setReply(isReply);
                cc.setReplyID(replyID);

                if (isReply) {
                    JSONObject replyToUserJson = WebUtils.getJsonObject(replyJson, KEY_REPLY_TO_USER);
                    if (replyToUserJson != null) {
                        String replyToUserName = WebUtils.getJsonString(replyToUserJson, KEY_USER_NAME);
                        String replyToUserLogo = WebUtils.getJsonString(replyToUserJson, KEY_USER_AVATAR);
                        int replyToUserID = WebUtils.getJsonInt(replyToUserJson, KEY_USER_ID);
                        int replyToUserAgeType = WebUtils.getJsonInt(replyToUserJson, KEY_USER_AGE_TYPE, -1);
                        cc.setReplyToUserName(replyToUserName);
                        cc.setReplyToUserLogo(replyToUserLogo);
                        cc.setReplyToUserID(replyToUserID);
                        cc.setReplyToUserAgeType(replyToUserAgeType);
                    }
                }
            }
        }

        JSONObject userJson = WebUtils.getJsonObject(j, KEY_USER);
        if (userJson != null) {
            String userName = WebUtils.getJsonString(userJson, KEY_USER_NAME);
            String userLogo = WebUtils.getJsonString(userJson, KEY_USER_AVATAR);
            int userID = WebUtils.getJsonInt(userJson, KEY_USER_ID);
            int ageType = WebUtils.getJsonInt(userJson, KEY_USER_AGE_TYPE, -1);
            int userGender = WebUtils.getJsonInt(userJson, KEY_CIRCLE_SEX, 0);
            cc.setUserName(userName);
            cc.setUserLogo(userLogo);
            cc.setUserID(userID);
            cc.setUserAgeType(ageType);
            cc.setUserGender(userGender);
        }

        return cc;
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        try {
            j.put(KEY_FEED_ID, feedID);
            j.put(KEY_MSG_ID, msgID);
            j.put(KEY_CONTENT, content);
            j.put(KEY_TIME, time);
            j.put(KEY_TYPE, type);
            j.put(KEY_IS_REPLY, isReply ? 1 : 0);
            j.put(KEY_REPLY_ID, replyID);

            JSONObject userJson = new JSONObject();
            userJson.put(KEY_USER_NAME, userName);
            userJson.put(KEY_USER_AVATAR, userLogo);
            userJson.put(KEY_USER_ID, userID);
            userJson.put(KEY_USER_AGE_TYPE, userAgeType);
            userJson.put(KEY_CIRCLE_SEX, userGender);
            j.put(KEY_USER, userJson);

            return j;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        JSONObject j = toJson();
        return j == null ? super.toString() : j.toString();
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    /***
     * @param type 1-like the msg; 2-reply the msg
     */
    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean isReply) {
        this.isReply = isReply;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public int getUserAgeType() {
        return userAgeType;
    }

    public void setUserAgeType(int userAgeType) {
        this.userAgeType = userAgeType;
    }

    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public int getReplyToUserID() {
        return replyToUserID;
    }

    public void setReplyToUserID(int replyToUserID) {
        this.replyToUserID = replyToUserID;
    }

    public int getReplyToUserAgeType() {
        return replyToUserAgeType;
    }

    public void setReplyToUserAgeType(int replyToUserAgeType) {
        this.replyToUserAgeType = replyToUserAgeType;
    }

    public String getReplyToUserName() {
        return replyToUserName;
    }

    public void setReplyToUserName(String replyToUserName) {
        this.replyToUserName = replyToUserName;
    }

    public String getReplyToUserLogo() {
        return replyToUserLogo;
    }

    public void setReplyToUserLogo(String replyToUserLogo) {
        this.replyToUserLogo = replyToUserLogo;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }


}
