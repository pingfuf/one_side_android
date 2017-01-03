package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.CoachPersonalCard;
import com.kuaipao.model.beans.MembershipCard;
import com.kuaipao.model.beans.Pagination;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pingfu on 16-8-11.
 */
public class CoachPersonalCardsResponse extends BaseResult {
    public boolean hasMore;

    public Pagination pagination;

    public List<CoachPersonalCard> items;

    @Override
    public void arrangeResponseData(JSONObject object) {
        super.arrangeResponseData(object);
        if(object == null) {
            return;
        }

        if(pagination != null) {
            hasMore = pagination.hasMore();
        }

        JSONArray jsonArray = WebUtils.getJsonArray(object, "items");
        if(jsonArray != null && items != null && jsonArray.size() == items.size()) {
            for(int i = 0; i < jsonArray.size(); i++) {
                CoachPersonalCard card = items.get(i);
                JSONObject itemObj = WebUtils.getJsonObject(jsonArray, i);
                card.price = WebUtils.getJsonInt(itemObj, "pay_price");
                JSONObject courseObj = WebUtils.getJsonObject(itemObj, "individual_course");
                card.name = WebUtils.getJsonString(courseObj, "name", "");

                Date date = LangUtils.formatAllDayDate(card.beginTime);
                String dateStr = LangUtils.formatDate(date, "yyyy年MM月");
                card.date = dateStr;
            }
        }
    }
}