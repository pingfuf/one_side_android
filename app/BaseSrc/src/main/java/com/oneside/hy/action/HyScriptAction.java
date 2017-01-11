package com.oneside.hy.action;

import com.oneside.base.CardConfig;

import java.io.Serializable;

/**
 * Created by fupingfu on 2017/1/9.
 */
public class HyScriptAction implements Serializable {
    private static final long serialVersionUID = 1l;

    public String token;

    public String url;

    public int type;

    public String secret = "8f00d7d21c6645719b4d4f47713b4030";

    public String appId = "30094";
}
