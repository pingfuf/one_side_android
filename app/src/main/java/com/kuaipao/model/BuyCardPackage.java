package com.kuaipao.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.utils.LogUtils;

import static com.kuaipao.utils.WebUtils.getJsonArray;
import static com.kuaipao.utils.WebUtils.getJsonInt;
import static com.kuaipao.utils.WebUtils.getJsonString;
import static com.kuaipao.utils.WebUtils.jsonToArrayString;

import java.util.ArrayList;


public class BuyCardPackage {

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "desc";
    private static final String KEY_PRICE = "price";
    private static final String KEY_NUM = "num";

    private boolean isSelected = false;

    private String title;
    private ArrayList<String> desc;
    private int price;//
    private int numOfMonth;
//  private int totalPrice;

    public BuyCardPackage(JSONObject json) {

        title = getJsonString(json, KEY_TITLE, "");

        JSONArray descArray = getJsonArray(json, KEY_DESC);
        desc = (ArrayList<String>) jsonToArrayString(descArray);

        price = getJsonInt(json, KEY_PRICE, -1);
        numOfMonth = getJsonInt(json, KEY_NUM, -1);
//    totalPrice = numOfMonth * price;
    }

    public static BuyCardPackage fromJson(JSONObject json) {
        if (json == null || json.size() <= 0)
            return null;
        return new BuyCardPackage(json);
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getDesc() {
        return desc;
    }

    public void setDesc(ArrayList<String> desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumOfMonth() {
        return numOfMonth;
    }

    public void setNumOfMonth(int numOfMonth) {
        this.numOfMonth = numOfMonth;
    }

    public int getToalPrice() {
        return numOfMonth * price;
    }

}
