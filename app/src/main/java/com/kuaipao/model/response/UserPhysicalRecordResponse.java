package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.Pagination;
import com.kuaipao.model.beans.XUserPhysicalRecord;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

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
