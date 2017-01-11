package com.oneside.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.model.beans.ArticleSummary;

import java.util.List;

/**
 * Created by fupingfu on 2017/1/5.
 */

public class NewsListResponse extends BaseShowApiResponse {
    @JSONField(name = "showapi_res_body")
    public NewsListResponseData data;

    public static class NewsListResponseData {
        @JSONField(name = "ret_code")
        public int code;

        @JSONField(name = "pagebean")
        public NewsListResponsePage page;

    }

    public static class NewsListResponsePage {
        public int allPages;
        public int currentPage;
        public int maxResult;

        public List<ArticleSummary> contentList;
    }
}
