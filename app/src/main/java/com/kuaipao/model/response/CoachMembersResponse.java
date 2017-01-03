package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.XMember;

import java.util.List;

/**
 * Created by pingfu on 16-9-18.
 */
public class CoachMembersResponse extends BaseResult {
    public boolean hasMore;
    public String moreUrl;
    public List<XMember> members;

    @Override
    public void arrangeResponseData(JSONObject dataStr) {
        super.arrangeResponseData(dataStr);

    }
}
