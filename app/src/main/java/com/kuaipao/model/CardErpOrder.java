package com.kuaipao.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.utils.LangUtils;

import static com.kuaipao.utils.LangUtils.getEnumObj;
import static com.kuaipao.utils.WebUtils.*;

import java.util.Date;

import static com.kuaipao.utils.WebUtils.getJsonLong;
import static com.kuaipao.utils.WebUtils.getJsonString;

/**
 * Created by guoming on 5/31/16.
 */
public class CardErpOrder {
//    {
//        "code": 0,
//            "data": {
//        "has_more": false,
//                "orders": [
//        {
//            "status": 0,
//                "course_name": "自由搏击",
//                "order_time": "2016-05-09 17:23:05",
//                "start_time": "12:00:00",
//                "gym": {
//            "name": "禅洲子午瑜伽馆",
//                    "id": 123
//        },
//            "end_time": "12:50:00",
//                "date": "2016-05-09",
//                "course_id": 3251,
//                "id": 55
//        }
//        ]
//    }
//    }

    @JSONField(name = "id")
    private long orderId;
    @JSONField(name = "course_id")
    private long classId;
    @JSONField(name = "status")
    private CardOrder.OrderStatus status = CardOrder.OrderStatus.OrderStatusUnused;
    @JSONField(name = "course_name")
    private String className;
    @JSONField(name = "gym")
    private CardSimpleMerchant merchant;
    @JSONField(name = "start_time")
    private Date startDate;
    @JSONField(name = "end_time")
    private Date endDate;


    private int category = 1;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public CardOrder.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(CardOrder.OrderStatus status) {
        this.status = status;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public CardSimpleMerchant getMerchant() {
        return merchant;
    }

    public void setMerchant(CardSimpleMerchant merchant) {
        this.merchant = merchant;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isExpired() {
        return endDate == null ? true : endDate.getTime() < System.currentTimeMillis();
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public long getMerchantId(){
        return merchant == null ? 0 : merchant.getMerchantID();
    }

    public String getMerchantName(){
        return merchant == null ? "" : merchant.getName();
    }
//    "status": 0,
//            "course_name": "自由搏击",
//            "order_time": "2016-05-09T23:05",
//            "start_time": "2016-05-09T12:00:00",
//            "gym": {
//        "name": "禅洲子午瑜伽馆",
//                "id": 123
//    },
//            "end_time": "2016-05-09T12:50:00",
//            "course_id": 3251,
//            "id": 55




}
