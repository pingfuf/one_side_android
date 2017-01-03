package com.oneside.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oneside.model.event.WebBaseEvent;
import com.oneside.utils.WebUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/3/11.
 */
public class MerchantCardYear extends WebBaseEvent {

    private List<CardYear> cardYearList;

    public MerchantCardYear(boolean bResult) {
        super(bResult);
        cardYearList = new ArrayList<>();
    }

    public static MerchantCardYear fromJson(JSONObject json) {
        JSONArray array = WebUtils.getJsonArray(json, "service");
        List<CardYear> cardYearList = new ArrayList<>();
        if (array != null) {
            int l = array.size();
            for (int i = 0; i < l; i++) {
                JSONObject jo = WebUtils.getJsonObject(array, i);
                cardYearList.add(CardYear.fromJson(jo));
            }
        }
        MerchantCardYear merchantCardYear = new MerchantCardYear(true);
        merchantCardYear.setCardYearList(cardYearList);
        return merchantCardYear;
    }

    private void setCardYearList(List<CardYear> cardYearList) {
        this.cardYearList = cardYearList;
    }

    public List<CardYear> getCardYearList() {
        return cardYearList;
    }
}
