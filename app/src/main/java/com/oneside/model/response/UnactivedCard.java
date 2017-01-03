package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.utils.LogUtils;
import com.oneside.utils.WebUtils;

import java.io.Serializable;

/**
 * Created by MVEN on 16/8/8.
 * <p/>
 * email: magiwen@126.com.
 */


public class UnactivedCard implements Serializable {
    public long id;
    public String name;

    public static UnactivedCard fromJson(JSONObject json) {
        if (json == null)
            return null;
        UnactivedCard card = new UnactivedCard();
        long id = WebUtils.getJsonLong(json, "id", 0l);
        String name = WebUtils.getJsonString(json, "name", "");
        card.id = id;
        card.name = name;
        return card;
    }

    public JSONObject toJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("id", id);
            return json;
        } catch (Exception e) {
            LogUtils.e(e, "parse PersonalCoach failed");
        }
        return null;
    }
}
