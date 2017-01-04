package com.oneside.base.net;

import com.android.volley.Request;
import com.oneside.base.net.model.BaseResult;

import java.io.Serializable;

/**
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-20
 * Time: 18:28
 * Author: pingfu
 * FIXME
 */
public interface IService extends Serializable {
    public static final int POST = Request.Method.POST;

    public static final int GET = Request.Method.GET;

    String getUrl();

    int getRequestType();

    Class<? extends BaseResult> getResultClazz();
}
