package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.model.beans.ArticleSummary;

import java.util.List;

/**
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-24
 * Time: 15:13
 * Author: pingfu
 * FIXME
 */
public class ConnotativePicsResponse extends BaseShowApiResponse {
    @JSONField(name = "showapi_res_body")
    public ConnotativePicData data;

    public static class ConnotativePicData {
        public int currentPage;
        public boolean hasMore;
        public Pagebean pagebean;
    }

    public static class Pagebean extends BaseShowApiData {
        public List<ArticleSummary> contentlist;
    }

    @Override
    public void arrangeResponseData(JSONObject jsonObject) {
        super.arrangeResponseData(jsonObject);
        if(data != null && data.pagebean != null) {
            data.hasMore = data.currentPage < data.pagebean.allPages;
        }
    }
}
