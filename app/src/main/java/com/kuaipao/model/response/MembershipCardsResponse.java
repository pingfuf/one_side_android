package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.CoachPersonalCard;
import com.kuaipao.model.beans.MembershipCard;
import com.kuaipao.model.beans.Pagination;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by pingfu on 16-8-11.
 */
public class MembershipCardsResponse extends BaseResult {
    private static long serialVersionUID = 42L;

    public boolean hasMore;

    public List<MembershipCard> items;

    public Pagination pagination;

    @Override
    public void arrangeResponseData(JSONObject jsonObject) {
        super.arrangeResponseData(jsonObject);
        if (pagination != null) {
            hasMore = pagination.hasMore();
        }

        if (jsonObject != null) {
            JSONArray jsonArray = WebUtils.getJsonArray(jsonObject, "items");

            if (jsonArray != null && items != null && jsonArray.size() == items.size()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    MembershipCard card = items.get(i);
                    if (card == null) {
                        continue;
                    }

                    JSONObject object = WebUtils.getJsonObject(jsonArray, i);
                    JSONObject template = WebUtils.getJsonObject(object, "template");
//                    card.beginTime = WebUtils.getJsonString(template, "insert_time", "");
                    card.name = WebUtils.getJsonString(template, "name", "");

                    Date date = LangUtils.formatAllDayDate(card.beginTime);
                    String dateStr = LangUtils.formatDate(date, "yyyy年MM月");
                    card.date = dateStr;
                }
            }
        }
    }
}
