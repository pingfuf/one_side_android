package com.kuaipao.base.net;

import com.android.volley.Request;
import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.base.net.model.NetworkParam;
import com.kuaipao.utils.LangUtils;

import java.util.HashMap;

/**
 * 发送请求的公共类
 * <p>
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-06
 * Time: 11:00
 * Author: pingfu
 * FIXME
 */
public final class XRequest {
    public enum RequestFeature {
        SHOULD_FORMAT
    }

    /**
     * 发送请求
     *
     * @param delegate 请求结果的callBack
     * @param service  服务端
     * @param params   请求参数
     */
    public static void startRequest(RequestDelegate delegate, IService service, BaseRequestParam params) {
        startRequest(delegate, service, params, 0);
    }

    /**
     * 发送请求
     *
     * @param delegate  请求结果的callBack
     * @param service   服务端
     * @param params    请求参数
     * @param requestNo 请求编号
     */
    public static void startRequest(RequestDelegate delegate, IService service,
                                    BaseRequestParam params, int requestNo) {
        startRequest(delegate, service, params, requestNo, null);
    }

    /**
     * 发送请求
     *
     * @param delegate   请求结果的callBack
     * @param service    服务端
     * @param params     请求参数
     * @param requestNo  请求编号
     * @param requestTag 请求的Tag，用来删除
     */
    public static void startRequest(RequestDelegate delegate, IService service,
                                    BaseRequestParam params, int requestNo, String requestTag) {
        if(service == null || LangUtils.isEmpty(service.getUrl())) {
            return;
        }
        NetworkParam param = createNetworkParam(service, service.getUrl(), params, requestNo, requestTag);
        startRequest(delegate, param, service.getRequestType());
    }

    /**
     * 发送请求
     *
     * @param delegate   请求结果的callBack
     * @param url        服务端
     * @param clazz      返回结果参数
     */
    public static void startRequest(RequestDelegate delegate, String url, Class<? extends BaseResult>  clazz) {
        startRequest(delegate, url, clazz, null);
    }

    /**
     * 发送请求
     *
     * @param delegate  请求结果的callBack
     * @param url       服务端
     * @param clazz      返回结果参数
     * @param params    请求参数
     */
    public static void startRequest(RequestDelegate delegate, String url, Class<? extends BaseResult>  clazz, BaseRequestParam params) {
        startRequest(delegate, url, clazz, params, Request.Method.GET);

    }

    /**
     * 发送请求
     *
     * @param delegate    请求结果的callBack
     * @param url         服务端
     * @param clazz       返回结果参数
     * @param params      请求参数
     * @param requestType 请求类型
     */
    public static void startRequest(RequestDelegate delegate, String url, Class<? extends BaseResult>  clazz,
                                    BaseRequestParam params, int requestType) {
        startRequest(delegate, url, clazz, params, requestType, 0);
    }

    /**
     * 发送请求
     *
     * @param delegate    请求结果的callBack
     * @param url         服务端
     * @param clazz       返回结果参数
     * @param params      请求参数
     * @param requestType 请求类型
     * @param requestNo   请求编号
     */
    public static void startRequest(RequestDelegate delegate, String url, Class<? extends BaseResult>  clazz,
                                    BaseRequestParam params, int requestType, int requestNo) {
        startRequest(delegate, url, clazz, params, requestType, requestNo, null);
    }

    /**
     * 发送请求
     *
     * @param delegate    请求结果的callBack
     * @param url         服务端
     * @param clazz       返回结果参数
     * @param params      请求参数
     * @param requestType 请求类型
     * @param requestNo   请求编号
     */
    public static void startRequest(RequestDelegate delegate, String url, Class<? extends BaseResult> clazz,
                                    BaseRequestParam params, int requestType, int requestNo, String requestTag) {
        startRequest(delegate, createNetworkParam(url, params, requestNo, requestTag, clazz), requestType);
    }

    /**
     * 发送请求k
     *
     * @param delegate    请求结果的callBack
     * @param param       请求参数
     * @param requestType 请求类型
     */
    private static void startRequest(RequestDelegate delegate, NetworkParam param, int requestType) {
        if (delegate == null || param == null || LangUtils.isEmpty(param.serverUri)) {
            return;
        }

        HashMap<String, Object> requestParams = null;
        String url = param.serverUri;
        if (param.requestParam != null && !LangUtils.isEmpty(param.requestParam.getRequestParam())) {
            requestParams = param.requestParam.getRequestParam();
            if (requestParams.containsKey(BaseRequestParam.ARRAY_PARAM)) {
                Object[] arrayParam = (Object[]) requestParams.get(BaseRequestParam.ARRAY_PARAM);
                try {
                    url = String.format(url, arrayParam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //请求参数中不需要ARRAY_PARAM
                requestParams.remove(BaseRequestParam.ARRAY_PARAM);
            }
        }

        UrlRequest request = UrlRequestFactory.build(url, requestParams, requestType);
        request.setDelegate(delegate);
        request.setNetworkParam(param);
        request.start();

        //为了避免影响下一次网络请求，请求结束后清空本次网络参数
        if(param.requestParam != null) {
            param.requestParam.clearParams();
        }
    }

    /**
     * 生成网络请求参数
     *
     * @param service    后端服务
     * @param url        网络请求url
     * @param param      请求参数
     * @param requestNo  请求序列号
     * @param requestTag 请求tag
     * @return networkParam
     */
    private static NetworkParam createNetworkParam(IService service, String url,
                                                   BaseRequestParam param, int requestNo,
                                                   String requestTag) {
        NetworkParam networkParam = new NetworkParam();
        networkParam.service = service;
        networkParam.serverUri = url;
        networkParam.requestParam = param;
        networkParam.requestNo = requestNo;
        networkParam.requestTag = requestTag;
        networkParam.responseClazz = service.getResultClazz();

        return networkParam;
    }

    /**
     * 生成网络请求参数
     *
     * @param url        网络请求url
     * @param param      请求参数
     * @param requestNo  请求序列号
     * @param requestTag 请求tag
     * @return networkParam
     */
    private static NetworkParam createNetworkParam(String url, BaseRequestParam param, int requestNo,
                                                   String requestTag, Class<? extends BaseResult> clazz) {
        NetworkParam networkParam = new NetworkParam();
        networkParam.serverUri = url;
        networkParam.requestParam = param;
        networkParam.requestNo = requestNo;
        networkParam.requestTag = requestTag;
        networkParam.responseClazz = clazz;

        return networkParam;
    }
}