package com.kuaipao.base.net.model;

import java.io.Serializable;

/**
 * 网络请求返回结果
 * 在既不能区分data是JSONObject还是JSONArray, 有不能使用GSON等开源软件的情况下，该怎么办呢？怎么办呢？
 * 使用泛型吧。只能说，垃圾的后端设计，当然，客户端这样设计似乎设计也有点2……
 * <p>
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-05
 * Time: 16:09
 * Author: pingfu
 * FIXME
 */
public abstract class BaseResponseData implements Serializable {
}
