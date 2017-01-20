package com.oneside.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.Article;

import java.util.List;

/**
 * Created by pingfu on 16-10-8.
 */
public class JokeListResponse extends BaseShowApiResponse {
    @JSONField(name = "showapi_res_body")
    public Data data;

    public static class Data extends BaseShowApiResponse.BaseShowApiData {
        public List<JokeArticle> contentlist;
    }

    public static class JokeArticle {
        public String title;
        public String text;
    }
}
