package com.kuaipao.model.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by pingfu on 16-10-27.
 */
public class Pagination {
    @JSONField(name = "current_page")
    public int currentPage;

    @JSONField(name = "page_size")
    public int pageSize;

    @JSONField(name = "total_count")
    public int totalCount;

    @JSONField(name = "total_page")
    public int totalPage;

    @JSONField(name = "next_day")
    public Date nextDay;

    public boolean hasMore() {
        return totalPage > currentPage;
    }
}
