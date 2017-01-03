package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.XReceiveRecord;
import com.kuaipao.model.beans.Pagination;

import java.util.List;

/**
 * Created by pingfu on 16-9-13.
 */
public class CoachReceiveRecordsResponse extends BaseResult {
    public boolean hasMore;

    public List<XReceiveRecord> items;

    public Pagination pagination;

    @Override
    public void arrangeResponseData(JSONObject jsonObject) {
        super.arrangeResponseData(jsonObject);
        hasMore = pagination.hasMore();
    }
}
