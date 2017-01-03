package com.kuaipao.model.response;

import com.kuaipao.base.net.model.BaseResult;

import java.io.Serializable;
import java.util.List;

/**
 * 卡片类型筛选条件接口返回结果
 *
 * Created by pingfu on 16-6-6.
 */
public class CardTypeFilterResponse extends BaseResult {
    private static long serialVersionUID = 42L;

    public List<CardTypeFilterItem> data;

    public static class CardTypeFilterItem implements Serializable {
        public int id;
        public String label;
    }
}
