package com.kuaipao.model.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by MVEN on 16/6/28.
 * <p/>
 * email: magiwen@126.com.
 */


public class XXNearOrdersResponseItem extends NearOrdersResponseItem {

    private static final long serialVersionUID = -693113778827024788L;
    @JSONField(name = "order_code")
    public String orderCode;

    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("order_code", orderCode);
        return json;
    }

    public  static XXNearOrdersResponseItem fromJson(JSONObject json) {
//        XXNearOrdersResponseItem xxItem = new XXNearOrdersResponseItem();
//        xxItem.classType = WebUtils.getJsonInt(json,"course_type");
//        xxItem.courseName = WebUtils.getJsonString(json,"course_name");
//        xxItem.time = WebUtils.getJsonString(json,"time");
//        xxItem.id = WebUtils.getJsonInt(json,"id");
//        xxItem.merchantName = WebUtils.getJsonString(json,"gym_name");
//        xxItem.merchantID = WebUtils.getJsonInt(json,"gym_id");
//        xxItem.orderCode = WebUtils.getJsonString(json,"order_code");

        XXNearOrdersResponseItem xxItem = JSON.parseObject(json.toString(), XXNearOrdersResponseItem.class);
        return xxItem;
    }
}
