package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResponseData;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.MembershipCard;

import java.io.Serializable;
import java.util.List;

/**
 * 请求场馆的卡片列表的返回结果
 * <p>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-11
 * Time: 13:51
 * Author: pingfu
 * FIXME
 */
public class StoryDetailResponse extends BaseShowApiResponse {
    private static long serialVersionUID = 42L;

    @JSONField(name = "showapi_res_body")
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data extends BaseShowApiResponse.BaseShowApiData {
        private static final long serialVersionUID = 1l;

        @JSONField(name = "ret_code")
        public int retCode;

        public String text;

        public String content;

        public String answer;
    }

    @Override
    public void arrangeResponseData(JSONObject dataStr) {
        super.arrangeResponseData(dataStr);
    }
}
