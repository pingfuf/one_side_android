package com.oneside.model.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.model.CardErpOrder;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoming on 6/3/16.
 */
public class CardErpOrderList extends  BaseDataList{

    @JSONField(name = "orders")
    private List<CardErpOrder> list;


    public List getList() {
        return list;
    }

    @JSONField(name="has_more")
    public void setHasMore(boolean hasMore) {
        super.setHasMore(hasMore);
    }

    @Override
    public void initList() {
        list = new ArrayList<>();
    }

    public void setList(List<CardErpOrder> list) {
        this.list = list;
    }




}
