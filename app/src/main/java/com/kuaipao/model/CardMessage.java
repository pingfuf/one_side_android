package com.kuaipao.model;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

import java.util.Date;

public class CardMessage {

    // 'datetime': ,
    // 'msg_id': ,
    // 'title':,
    // 'text':,
    // 'source': '系统消息'
    private long msgId;
    private String title;
    private String text;
    private String courseID;
    /**
     * 1 用户购买或续费成功 2 用户预约成功 3 用户取消成功 4 用户爽约 5 用户预约失败 6 签到成功
     */
    private int type;
    private Date date;
    private String pre_desc;
    private String remark;
    private boolean isViewed = false;
    private String gym_id;
    private int card_info;
    private String source;

    public CardMessage(JSONObject json) {
        msgId = WebUtils.getJsonLong(json, "msg_id", 0l);
        title = WebUtils.getJsonString(json, "title", "");
        text = WebUtils.getJsonString(json, "text", "");
        courseID = WebUtils.getJsonString(json, "course_id", "");
        type = WebUtils.getJsonInt(json, "type");
        card_info = WebUtils.getJsonInt(json, "card_info");
        isViewed = WebUtils.getJsonBoolean(json, "viewd", false);
        pre_desc = WebUtils.getJsonString(json, "pre_desc", "");
        remark = WebUtils.getJsonString(json, "remark", "");
        gym_id = WebUtils.getJsonString(json, "gym_id", "");
        source = WebUtils.getJsonString(json, "source", "");
        String time = WebUtils.getJsonString(json, "datetime", "");
        if (LangUtils.isNotEmpty(time)) {
            date = LangUtils.formatTypeNormalDate(time);
        }
        com.kuaipao.utils.LogUtils.d("cardmessage date = %s", date);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof CardMessage) {
            return msgId == ((CardMessage) o).getMsgId();
        }
        return false;
    }

    public static CardMessage getMessageByJson(JSONObject json) {
        if (json == null || json.size() <= 0) {
            return null;
        }
        return new CardMessage(json);
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean isViewed) {
        this.isViewed = isViewed;
    }

    public JSONObject jsonDict() {
        JSONObject json = new JSONObject();
        try {
            json.put("msg_id", msgId);
            json.put("title", title);
            json.put("msg_id", msgId);
            json.put("text", text);
            json.put("gym_id", gym_id);
            json.put("course_id", courseID);
            json.put("pre_desc", pre_desc);
            json.put("remark", remark);
            json.put("info", card_info);
            json.put("type", type);
            json.put("viewd", isViewed);
            json.put("source", source);
            json.put("datetime", date == null ? "" : LangUtils.formatTypeNormalTime(date));
            return json;
        } catch (JSONException e) {

        }
        return null;
    }

    public String toString() {
        JSONObject json = jsonDict();
        return json == null ? "" : json.toString();
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCardInfo(int info) {
        this.card_info = info;
    }

    public int getCardInfo() {
        return card_info;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getGYMID() {
        return gym_id;
    }

    public void setGYMID(String gymid) {
        gym_id = gymid;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setPreDesc(String pre_desc) {
        this.pre_desc = pre_desc;
    }

    public String getPreDesc() {
        return pre_desc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
