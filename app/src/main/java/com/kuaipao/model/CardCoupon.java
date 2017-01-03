package com.kuaipao.model;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

import java.util.Date;

public class CardCoupon {

    public enum CouponStatus {
        UNUSED, USED, USEING, EXPIRED
    }

    private long id;
    private Date expiredTime;
    private int price, enoughAmount;
    private CouponStatus status = CouponStatus.UNUSED;
    private String name;
    private boolean isSelected = false;
    private boolean canUse = true;
    private boolean onlyNewUserCanUse = false;
    private int restrict;
    private String restrictDesc;

    // "id": 1,
    // "name": "优惠券",
    // "status": 0, // 0: 未使用 1.已使用 2.购买中, 还未购买完成
    // "amount": 20, // 面值
    // "expired_date": "2015-06-21", // 过期日期
    // "min_charge": 0
    //"restrict": 0为通用，1为三次通卡，2为有氧空间

    public CardCoupon(JSONObject json) {
        id = WebUtils.getJsonLong(json, "id", 0l);
        price = WebUtils.getJsonInt(json, "face_value", 0);
        enoughAmount = WebUtils.getJsonInt(json, "min_charge", 0);
        name = WebUtils.getJsonString(json, "name", "");
        String time = WebUtils.getJsonString(json, "expired_date");
        expiredTime = LangUtils.formatDate(time, "yyyy-MM-dd");
        onlyNewUserCanUse = WebUtils.getJsonBoolean(json, "only_new_user", false);
        restrict = WebUtils.getJsonInt(json, "restrict", 0);
        restrictDesc = WebUtils.getJsonString(json, "restrict_desc", "");
        int statusIndex = WebUtils.getJsonInt(json, "status", 0);
        status = LangUtils.getEnumObj(CardCoupon.CouponStatus.values(), statusIndex);
        if (status == CouponStatus.UNUSED) {
            int days = LangUtils.daysBetweenDate(new Date(), expiredTime);
            if (days > 0) {
                status = CouponStatus.EXPIRED;
            }
        }
    }

    /**
     * add by shi for test
     ***/
    public CardCoupon() {

    }

    public JSONObject jsonDict() {

        try {
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("name", name);
            json.put("status", status.ordinal());
            json.put("face_value", price);
            json.put("min_charge", enoughAmount);
            json.put("expired_date", LangUtils.formatAlldayTime(expiredTime));
            json.put("only_new_user", onlyNewUserCanUse);
            return json;
        } catch (JSONException e) {

        }
        return null;

    }

    public static CardCoupon getCounponByJson(JSONObject json) {
        if (json == null || json.size() <= 0) {
            return null;
        }
        return new CardCoupon(json);
    }

    public int getRestrict() {
        return restrict;
    }

    public String getRestrictDesc() {
        return restrictDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getEnoughAmount() {
        return enoughAmount;
    }

    public void setEnoughAmount(int enoughAmount) {
        this.enoughAmount = enoughAmount;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setCanUse(boolean canuse) {
        this.canUse = canuse;
    }

    public boolean getCanUse() {
        return this.canUse;
    }

    public boolean getIsOnlyNewUserCanUse() {
        return this.onlyNewUserCanUse;
    }
}
