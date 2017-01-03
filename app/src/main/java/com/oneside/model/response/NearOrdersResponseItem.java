package com.oneside.model.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by MVEN on 16/6/28.
 * <p/>
 * email: magiwen@126.com.
 */


public class NearOrdersResponseItem /*extends IJsonTransfer*/ implements Serializable{

    private static final long serialVersionUID = -6032579182286196110L;
    @JSONField(name = "gym_id")
    public long merchantID;
    @JSONField(name = "gym_name")
    public String merchantName;
    @JSONField(name = "id")
    public int id;

    @JSONField(name = "course_name")
    public String courseName;
    @JSONField(name = "time")
    public String time;
    @JSONField(name = "course_type")
    public int classType;

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("gym_id",merchantID);
        json.put("gym_name",merchantName);
        json.put("id",id);
        json.put("course_name",courseName);
        json.put("time",time);
        json.put("course_type",classType);
        return json;
    }

    public static NearOrdersResponseItem fromJson(JSONObject json) {
//        NearOrdersResponseItem item = new NearOrdersResponseItem();
//        item.classType = WebUtils.getJsonInt(json,"course_type");
//        item.courseName = WebUtils.getJsonString(json,"course_name");
//        item.time = WebUtils.getJsonString(json,"time");
//        item.id = WebUtils.getJsonInt(json,"id");
//        item.merchantName = WebUtils.getJsonString(json,"gym_name");
//        item.merchantID = WebUtils.getJsonInt(json,"gym_id");
        NearOrdersResponseItem item = JSON.parseObject(json.toString(),NearOrdersResponseItem.class);
        return item;
    }
}
