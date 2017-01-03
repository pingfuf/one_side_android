package com.oneside.model.request;

import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.utils.LangUtils;

/**
 * Created by pingfu on 16-6-23.
 */
public class UserFavoriteGymsRequestParam extends BaseRequestParam {
    //用户id
    public String uid;

    //偏移量
    public int offset;

    //每页几条数据
    public int limit;

    public String keyword;

    @Override
    protected void addRequestParams() {
        addUrlParams(uid);
        addParam("offset", offset);
        addParam("limit", limit);

        if(!LangUtils.isEmpty(keyword)) {
            addParam("keyword", keyword);
        }
    }
}
