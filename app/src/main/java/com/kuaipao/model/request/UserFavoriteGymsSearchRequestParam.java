package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;

/**
 * Created by pingfu on 16-7-4.
 */
public class UserFavoriteGymsSearchRequestParam extends BaseRequestParam {
    //用户id
    public String uid;

    //偏移量
    public int offset;

    //每页几条数据
    public int limit;

    //搜索关键词
    public String keyword;

    @Override
    protected void addRequestParams() {
        addParam("uId", uid);
        addParam("offset", offset);
        addParam("limit", limit);
        addParam("keyword", keyword);
    }
}
