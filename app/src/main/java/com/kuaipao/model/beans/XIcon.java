package com.kuaipao.model.beans;

import java.io.Serializable;

/**
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-11
 * Time: 13:46
 * Author: pingfu
 * FIXME
 */
public class XIcon implements Serializable {
    private static long serialVersionUID = 42L;

    /**
     * icon类型：0 -> 本地图片  5 -> 网络图片
     */
    public int type;

    /**
     * 图片URL
     */
    public String url;

    /**
     * 图片的resource id
     */
    public int resId;
}
