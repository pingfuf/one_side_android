package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.Pagination;
import com.kuaipao.model.beans.XCustomer;
import com.kuaipao.utils.LangUtils;

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
