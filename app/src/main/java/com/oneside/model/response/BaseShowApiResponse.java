package com.oneside.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResult;

/**
 * Created by fupingfu on 2017/1/4.
 */

public class BaseShowApiResponse extends BaseResult {
    @JSONField(name = "showapi_res_code")
    public int code;

    @JSONField(name = "showapi_res_error")
    public String msg;
}
