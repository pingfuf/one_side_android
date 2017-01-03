package com.oneside.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import static com.oneside.utils.WebUtils.getJsonLong;
import static com.oneside.utils.WebUtils.getJsonString;

/**
 * Created by guoming on 6/2/16.
 */
public class BaseMerchant {

    @JSONField(name = "name")
    private String merchantName;
    @JSONField(name = "id")
    private long merchantId;
    @JSONField(name = "url")
    private String imageUrl;// Background image url

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public static BaseMerchant fromJson(JSONObject j) {
        if (j == null || j.size() == 0) {
            return null;
        }
        BaseMerchant merchant = new BaseMerchant();
        merchant.setMerchantId(getJsonLong(j, "id", 0l));
        merchant.setMerchantName(getJsonString(j, "name", ""));
        merchant.setImageUrl(getJsonString(j, "url", ""));//TODO
        return  merchant;
    }
}