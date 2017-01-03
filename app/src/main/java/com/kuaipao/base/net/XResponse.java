package com.kuaipao.base.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.base.net.model.NetworkParam;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.WebUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 网络请求返回结果处理
 * <p/>
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-23
 * Time: 11:12
 * Author: pingfu
 * FIXME
 */
public final class XResponse {
    private static final String CODE_KEY = "code";
    private static final String DATA_KEY = "data";

    public static final int JSON_OBJECT_TYPE = 0;
    public static final int JSON_ARRAY_TYPE = 1;

    /**
     * 将请求结果解析成XResponseResult对象
     *
     * @param request request请求
     * @return XResponseResult对象
     */
    public static BaseResult parseResponse(UrlRequest request) {
        BaseResult result = null;

        if (request != null) {
            NetworkParam param = request.getNetworkParam();
            if (param != null) {
                result = parseJson(param.responseClazz, request.getResponseStr(), 0);
                if (result == null && param.responseClazz != null) {
                    try {
                        result = param.responseClazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (result != null) {
                    result.data = request.getResponseJsonObject();
                    result.arrangeResponseData(result.data);
                }
            }
        }

        return result;
    }

    /**
     * 获取网络请求的返回结果中第一层的某一字段，后端的返回JSON格式不统一，没办法统一解析
     *
     * @param request 网络请求
     * @param key     字段key
     * @return 返回某一字段
     */
    public static String getResponseStr(UrlRequest request, String key) {
        if (request == null || LangUtils.isEmpty(request.getResponseStr()) || LangUtils.isEmpty(key)) {
            return "";
        }

        JSONObject jsonObject = JSON.parseObject(request.getResponseStr());
        return WebUtils.getJsonString(jsonObject, key, "");
    }

    /**
     * 将JSON数据转换为Java对象
     *
     * @param clazz    java对象类型
     * @param json     json数据
     * @param jsonType json数据类型，0为JSONObject， 1为JSONArray
     * @param <T>      泛型
     * @return Java对象
     */
    public static <T> T parseJson(Class clazz, String json, int jsonType) {
        T t;
        try {
            if (jsonType == JSON_OBJECT_TYPE) {
                t = (T) JSON.parseObject(json, clazz);
            } else {
                t = (T) JSON.parseArray(json, clazz);
            }
        } catch (Exception e) {
            t = null;
            e.printStackTrace();
        }

        return t;
    }

    private static Class getListDataItemClass(Class clazz) {
        Class dataClazz = Object.class;
        if (BaseResult.class.isAssignableFrom(clazz)) {
            try {
                Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
                dataClazz = (Class) type;
            } catch (Exception e) {
                e.printStackTrace();
                dataClazz = Object.class;
            }
        }

        return dataClazz;
    }
}
