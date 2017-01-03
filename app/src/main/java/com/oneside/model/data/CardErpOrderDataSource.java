package com.oneside.model.data;

import com.oneside.base.net.UrlRequest;



import java.util.HashMap;

/**
 * Created by guoming on 6/3/16.
 */
public class CardErpOrderDataSource extends BaseDataSource{
    public CardErpOrderDataSource(int limit){
        super(limit);

    }

    public UrlRequest getUrlRequest(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("offset", dataList == null ? 0 : dataList.getPage());
        UrlRequest r = new UrlRequest("/client/erp/order", params);

        return r;
    }
}
