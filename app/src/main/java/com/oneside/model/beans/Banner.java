package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by MVEN on 16/7/18.
 * <p/>
 * email: magiwen@126.com.
 */


public class Banner implements Serializable {
    private static final long serialVersionUID = 4117251956057845067L;

    public long id;

    public String title;

    public String url;

    @JSONField(name = "is_show")
    public boolean isShow;

    @JSONField(name = "start_time")
    public String startTime;

    @JSONField(name = "end_time")
    public String endTime;

    @JSONField(name = "img_url")
    public String imgUrl;
}