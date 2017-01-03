package com.kuaipao.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.CoachMember;
import com.kuaipao.model.beans.Pagination;
import com.kuaipao.model.beans.XCustomer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoming on 31/10/16.
 */
public class CoachMemberListResponse extends BaseResult{

    private List<CoachMember> items;

    @JSONField(name = "pagination")
    private Pagination pagination;

    public List<CoachMember> getItems() {
        return items;
    }

    public void setItems(List<CoachMember> items) {
        this.items = items;
    }

    public void addOtherResponse(CoachMemberListResponse response){
        if (response == null){
            return;
        }
        addItems(response.getItems());
        this.pagination = response.getPagination();
    }

    private void addItems(List<CoachMember> items){
        if (this.items == null){
            this.items = new ArrayList<>();
        }
        this.items.addAll(items);
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
