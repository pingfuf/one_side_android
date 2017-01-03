package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.XReceiveRecord;
import com.oneside.model.beans.Pagination;

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
