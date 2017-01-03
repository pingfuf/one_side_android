package com.oneside.model.beans;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by pingfu on 16-11-7.
 */
public class XActionNote implements Serializable {

    public XActionNote(){
        actions = new ArrayList<>();
        imageUrls = new ArrayList<>();
        bodyParts = new HashMap<>();
    }
    public long id = 0;

    public List<XAction> actions;

    @JSONField(name = "remain_minutes")
    public int remainMinutes  = 0;

    @JSONField(name = "body_parts")
    public HashMap<String, List<String>> bodyParts;

    @JSONField(name = "imgs")
    public List<String> imageUrls;

    public String desc;

    @JSONField(name = "aerobic_exercise")
    public String aerobicTraining;

    public int calories;
    public int distance;
    public int strength;

    @JSONField(name = "speed_resistance")
    public int speed;

    public int status;

    public int method;

    public void setBodyPartsByJson(JSONObject json){
        if (json == null){
            bodyParts = null;
        }
        bodyParts = new HashMap<>();
        Set<String> keys = json.keySet();
        for (String key : keys){
            JSONArray array = json.getJSONArray(key);
            if (array == null || array.size() <= 0){
                continue;
            }
            List<String> list = new ArrayList<>(array.size());
            for (int i = 0;i < array.size(); i ++){
                list.add(array.getString(i));
            }
            bodyParts.put(key, list);

        }

    }
}
