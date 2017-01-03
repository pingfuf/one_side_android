package com.kuaipao.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guoming on 6/30/16.
 */
public class CardSimpleMerchant implements Serializable {


    private static final long serialVersionUID = -9042903020045444398L;
    @JSONField(name = "id")
    private long merchantID;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "location")
    private String location;

    @JSONField(name = "logo_url")
    private String imageUrl;

    @JSONField(name = "co_type")
    private int partnerType;

    public long getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(long merchantID) {
        this.merchantID = merchantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(int partnerType) {
        this.partnerType = partnerType;
    }
}
