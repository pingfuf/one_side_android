package com.kuaipao.model;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;
import java.util.Date;

public class BuyCardOrder {
    // trade_no": "papaya14314291318171330217", // 订单号
    // "end": "2016-06-12",
    // "start": "2016-05-13",
    // "total_fee": 9900, //购买价格,单位是分
    // "buy_time": "2015-05-12T19:16:40", //购买时间
    // "latest": true // 最新一次购买

    private String orderNumber;
    private Date startTime, endTime, buyTime;
    private int price = 0;
    private boolean latest = false;


    public BuyCardOrder(JSONObject json) {
        orderNumber = WebUtils.getJsonString(json, "trade_no", "");
        String start = WebUtils.getJsonString(json, "start", "");
        startTime = LangUtils.formatDate(start, "yyyy-MM-dd");
        String end = WebUtils.getJsonString(json, "end", "");
        endTime = LangUtils.formatDate(end, "yyyy-MM-dd");

        price = WebUtils.getJsonInt(json, "total_fee", 0);

        String buy = WebUtils.getJsonString(json, "buy_time", "");
        buyTime = LangUtils.formatDate(buy, "yyyyMMdd'T'HHmmss");
        latest = WebUtils.getJsonBoolean(json, "latest");
    }

    public static BuyCardOrder getBuyOrderByJson(JSONObject json) {
        if (json == null || json.size() <= 0) {
            return null;
        }
        return new BuyCardOrder(json);
    }

    public JSONObject jsonDict() {
        try {
            JSONObject json = new JSONObject();
            json.put("trade_no", orderNumber);
            json.put("start", startTime);
            json.put("end", endTime);
            json.put("total_fee", price);
            json.put("buy_time", buyTime);
            json.put("latest", latest);
            return json;
        } catch (JSONException e) {
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof BuyCardOrder) {
            return orderNumber != null ? orderNumber.equals(((BuyCardOrder) o).getOrderNumber()) : false;
        }
        return false;
    }

    public String toString() {
        JSONObject j = jsonDict();
        return j == null ? super.toString() : j.toString();
    }


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

}
