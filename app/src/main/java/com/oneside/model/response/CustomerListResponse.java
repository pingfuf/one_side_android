package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.Pagination;
import com.oneside.model.beans.XCustomer;
import com.oneside.utils.LangUtils;

import java.util.List;

/**
 * Created by MVEN on 16/6/28.
 * <p/>
 * email: magiwen@126.com.
 */

//即将开始的ERP场馆课程列表
public class CustomerListResponse extends BaseResult {
    private static final long serialVersionUID = 2697492781984270368L;

    public List<XCustomer> items;

    @JSONField(name = "pagination")
    public Pagination pagination;

    @Override
    public void arrangeResponseData(JSONObject jsonObject) {
        super.arrangeResponseData(jsonObject);
    }
}
