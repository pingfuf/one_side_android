package com.oneside.model;

import java.util.ArrayList;

import android.content.Context;

import com.android.volley.Request;
import com.oneside.base.net.IService;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.response.BaseShowApiResponse;
import com.oneside.model.response.ConnotativePicsResponse;
import com.oneside.model.response.JokeListResponse;
import com.oneside.model.response.NewsListResponse;
import com.oneside.model.response.StoryDetailResponse;

public enum ShowApiService implements IService{
    /**
     * 故事汇系列
     */
    //鬼故事列表
    GhostStoryList("http://route.showapi.com/955-1", NewsListResponse.class),

    //鬼故事详情
    GhostStoryDetail("http://route.showapi.com/955-2", StoryDetailResponse.class),

    //笑话列表
    JokesList("http://route.showapi.com/341-1", JokeListResponse.class),

    /**
     * 漫画系列
     */
    //内涵漫画
    ConnotativePics("http://route.showapi.com/978-2", ConnotativePicsResponse.class)
    ;

    private final String mUrl;
    private final int mMethod;
    private final Class mClazz;

    ShowApiService(String url, Class<? extends BaseShowApiResponse> clazz) {
        this(url, Request.Method.GET, clazz);
    }

    ShowApiService(String url, int method, Class<? extends BaseShowApiResponse> clazz) {
        mUrl = url;
        mMethod = method;
        mClazz = clazz;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public int getRequestType() {
        return mMethod;
    }

    @Override
    public Class<? extends BaseResult> getResultClazz() {
        return mClazz;
    }
}
