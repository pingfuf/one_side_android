package com.kuaipao.model.data;

import com.kuaipao.base.net.UrlRequest;

import java.util.HashMap;

/**
 * Created by guoming on 6/29/16.
 */
public class CardXXOrderDataSource extends BaseDataSource{


    private boolean isSpecial = false;

    public CardXXOrderDataSource(int limit, boolean isSpecial){
        super(limit);
        this.isSpecial = isSpecial;

    }

    public UrlRequest getUrlRequest(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("page", (dataList == null ? 0 : dataList.getPage()) + 1);
        params.put("special", isSpecial ? 1 : 0);
        UrlRequest r = new UrlRequest("client/user/orders/all", params);

        return r;
    }


}
