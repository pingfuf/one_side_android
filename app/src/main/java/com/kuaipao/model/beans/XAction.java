package com.kuaipao.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pingfu on 16-10-11.
 */
public class XAction implements Serializable {
    public String name;

    @JSONField(name = "data")
    public List<XActionItem> items;
}
