package com.kuaipao.base.model;

import java.io.Serializable;

/**
 * 页面需要参数的基类，每个页面的入口参数统一，方便理解
 *
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-09
 * Time: 13:15
 * Author: pingfu
 * FIXME
 */
public class BasePageParam implements Serializable {
    public static final String PARAM_KEY = "PageParam";

    /**
     * 用户id
     */
    public long userId;

    /**
     * 场馆id
     */
    public long gymId;

    /**
     * 教练id
     */
    public long coachId;

    /**
     * 课程id
     */
    public long courseId;

    /**
     * 卡片id
     */
    public long cardId;

    /**
     * 订单id
     */
    public long orderId;

    /**
     * 会员id
     */
    public long memberId;
}