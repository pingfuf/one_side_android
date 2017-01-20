package com.oneside.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResult;

import java.io.Serializable;

/**
 * Created by fupingfu on 2017/1/4.
 */

public class BaseShowApiResponse extends BaseResult {
    @JSONField(name = "showapi_res_code")
    public int code;

    @JSONField(name = "showapi_res_error")
    public String msg;

    public static class BaseShowApiData implements Serializable {
        public int allPages;
        public int currentPage;
        public int allNum;

        public boolean hasMore() {
            return currentPage < allPages;
        }
    }
}
