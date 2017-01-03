package com.oneside.model.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.Pagination;
import com.oneside.model.beans.XUserPhysicalRecord;
import com.oneside.utils.LangUtils;
import com.oneside.utils.WebUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pingfu on 16-8-31.
 */
public class UserPhysicalRecordResponse extends BaseResult {
    private static long serialVersionUID = 42L;

    public boolean hasMore;

    public List<XUserPhysicalRecord> items;

    public Pagination pagination;

    @Override
    public void arrangeResponseData(JSONObject jsonObject) {
        super.arrangeResponseData(jsonObject);
        hasMore = pagination.hasMore();
    }
}
