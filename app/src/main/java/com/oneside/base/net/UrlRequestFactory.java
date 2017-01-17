package com.oneside.base.net;

import com.android.volley.Request;
import com.oneside.utils.LangUtils;

import java.util.HashMap;

/**
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-06
 * Time: 10:47
 * Author: pingfu
 * FIXME
 */
public class UrlRequestFactory {
    /**
     * 生成UrlRequest
     *
     * @param url 请求路径
     * @return urlRequest
     */
    public static UrlRequest build(String url) {
        return build(url, null);
    }

    /**
     * 生成UrlRequest
     *
     * @param url    请求路径
     * @param params 请求参数
     * @return urlRequest
     */
    public static UrlRequest build(String url, HashMap<String, Object> params) {
        return build(url, params, Request.Method.GET);
    }

    /**
     * 生成UrlRequest
     *
     * @param url    请求路径
     * @param params 请求参数
     * @param method 请求类型
     * @return
     */
    public static UrlRequest build(String url, HashMap<String, Object> params, int method) {
        UrlRequest request;
        if(LangUtils.isEmpty(params)) {
            request = new UrlRequest(url);
        } else {
            switch (method) {
                case Request.Method.GET:
                    request = new UrlRequest(url, params);
                    break;
                case Request.Method.POST:
                case Request.Method.PUT:
                    request = new UrlRequest(url);
                    for(String key: params.keySet()) {
                        request.addPostParam(key, params.get(key));
                    }
                    break;
                default:
                    request = new UrlRequest(url, params);
                    break;
            }
        }

        return request;
    }
}
