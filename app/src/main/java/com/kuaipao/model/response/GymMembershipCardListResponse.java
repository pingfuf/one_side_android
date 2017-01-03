package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResponseData;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.MembershipCard;

import java.util.List;

/**
 * 请求场馆的卡片列表的返回结果
 * <p>
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-11
 * Time: 13:51
 * Author: pingfu
 * FIXME
 */
public class GymMembershipCardListResponse extends BaseResult {
    private static long serialVersionUID = 42L;

    public GymMembershipCardListData data;

    public static class GymMembershipCardListData extends BaseResponseData {
        @JSONField(name = "is_erp")
        public boolean isErp;

        //卡片列表
        @JSONField(name = "service")
        public List<MembershipCard> cardList;
    }

    @Override
    public void arrangeResponseData(JSONObject dataStr) {
        super.arrangeResponseData(dataStr);
    }
}
