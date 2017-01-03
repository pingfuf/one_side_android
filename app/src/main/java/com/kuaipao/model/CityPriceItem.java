package com.kuaipao.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static com.kuaipao.utils.LangUtils.daysBetweenDate;
import static com.kuaipao.utils.WebUtils.getJsonArray;
import static com.kuaipao.utils.WebUtils.getJsonBoolean;
import static com.kuaipao.utils.WebUtils.getJsonInt;
import static com.kuaipao.utils.WebUtils.getJsonLong;
import static com.kuaipao.utils.WebUtils.getJsonObject;
import static com.kuaipao.utils.WebUtils.getJsonString;
import static com.kuaipao.utils.WebUtils.jsonToArrayString;

import java.util.ArrayList;


public class CityPriceItem {


    private static final String KEY_LEVEL = "level";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "desc";
    private static final String KEY_CITIES = "cities";
    private static final String KEY_PRICEITEMS = "items";

    private int level;
    private String title;
    private String desc;
    private ArrayList<String> cities;
    private ArrayList<BuyCardPackage> priceItems;


    public CityPriceItem(JSONObject json) {

        level = getJsonInt(json, KEY_LEVEL, -1);
        title = getJsonString(json, KEY_TITLE, "");
        desc = getJsonString(json, KEY_DESC, "");

        JSONArray citiesArray = getJsonArray(json, KEY_CITIES);
        cities = (ArrayList<String>) jsonToArrayString(citiesArray);

        JSONArray priceItemsJson = getJsonArray(json, KEY_PRICEITEMS);
        if (priceItemsJson != null && priceItemsJson.size() > 0) {
            priceItems = new ArrayList<BuyCardPackage>();
            for (int i = 0; i < priceItemsJson.size(); i++) {
                JSONObject piJson = getJsonObject(priceItemsJson, i);
                priceItems.add(BuyCardPackage.fromJson(piJson));
            }
        }
    }

    public static CityPriceItem fromJson(JSONObject json) {
        if (json == null || json.size() <= 0)
            return null;
        return new CityPriceItem(json);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }

    public ArrayList<BuyCardPackage> getPriceItems() {
        return priceItems;
    }

    public void setPriceItems(ArrayList<BuyCardPackage> priceItems) {
        this.priceItems = priceItems;
    }


}
