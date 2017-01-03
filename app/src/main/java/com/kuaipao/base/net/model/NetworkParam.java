package com.kuaipao.base.net.model;

import com.kuaipao.base.net.IService;

import java.io.Serializable;

/**
 * 网络请求参数
 *
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-05
 * Time: 16:07
 * Author: pingfu
 * FIXME
 */
public class NetworkParam implements Serializable {
    /** 后端服务 */
    public IService service;

    /** 请求地址（后端服务地址和真正的请求地址不一定一样） */
    public String serverUri;

    /** 请求参数 */
    public BaseRequestParam requestParam;

    /** 请求序列号 */
    public int requestNo;

    /** 请求tag */
    public String requestTag;

    public Class<? extends BaseResult> responseClazz;
}
