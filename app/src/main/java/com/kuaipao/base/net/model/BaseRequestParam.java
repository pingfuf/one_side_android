package com.kuaipao.base.net.model;

import com.kuaipao.utils.LangUtils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 网络请求参数基类
 * <p/>
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-05
 * Time: 16:09
 * Author: pingfu
 * FIXME
 */
public class BaseRequestParam implements Serializable {
    public static final String ARRAY_PARAM = "ARRAY_PARAM";

    /**
     * 参数请求数据存放在HashMap中
     */
    private volatile HashMap<String, Object> params;

    /**
     * URL中包含的参数放到urlParams中
     */
    private volatile Object[] urlParams;

    /**
     * 将请求参数对象转化成Map形式，用于网络请求
     */
    protected void addRequestParams(){

    }

    public final HashMap<String, Object> getRequestParam() {
        addRequestParams();

        return params;
    }

    public void clearParams() {
        if(!LangUtils.isEmpty(params)) {
            params.clear();
        }
    }

    /**
     * 添加请求参数
     *
     * @param key    请求参数的key，后端接口定义的参数名称，可能比较怪异
     * @param value  请求参数的值
     */
    public final void addParam(String key, Object value) {
        if(LangUtils.isEmpty(key)) {
            return;
        }

        if(params == null) {
            params = new HashMap<String, Object>();
        }

        params.put(key, value);
    }

    public final void addUrlParams(Object... params) {
        urlParams = params;

        if(!LangUtils.isEmpty(urlParams)) {
           addParam(ARRAY_PARAM, urlParams);
        }
    }
}
