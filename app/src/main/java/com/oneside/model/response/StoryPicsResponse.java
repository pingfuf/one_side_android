package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResponseData;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.ArticleSummary;
import com.oneside.model.beans.XCoach;

import java.io.Serializable;
import java.util.List;

/**
 * ERP课程列表返回结果
 *
 * Created by pingfu on 16-5-27.
 */
public class StoryPicsResponse extends BaseShowApiResponse {
    @JSONField(name = "showapi_res_body")
    public ConnotativePicData data;

    public static class ConnotativePicData {
        public PageBean pagebean;
        public boolean hasMore;
    }

    public static class PageBean extends BaseShowApiData {
        public List<ArticleSummary> contentlist;
    }

    @Override
    public void arrangeResponseData(JSONObject jsonObject) {
        super.arrangeResponseData(jsonObject);
        if(data != null && data.pagebean != null) {
            data.hasMore = data.pagebean.hasMore();
        }
    }
}
