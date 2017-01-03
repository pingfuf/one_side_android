package com.oneside.model.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.model.CardOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoming on 6/29/16.
 */
public class CardXXOrderList extends BaseDataList {

    @JSONField(name = "orders")
    private List<CardOrder> list;


    public List getList() {
        return list;
    }

    @JSONField(name = "has_more")
    public void setHasMore(boolean hasMore) {
        super.setHasMore(hasMore);
    }

    @JSONField(name = "current_page")
    public void setPage(int page) {
        super.setPage(page);
    }

    @Override
    public void initList() {
        list = new ArrayList<CardOrder>();
    }

    public void setList(List<CardOrder> list) {
        if (list != null) {
            this.list = new ArrayList<>(list.size());
            for (CardOrder order : list) {
                if (order.getOrderStatus() != CardOrder.OrderStatus.OrderStatusCancelled) {
                    this.list.add(order);
                }
            }
        } else {
            this.list = null;
        }

    }

}
