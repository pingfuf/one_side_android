package com.kuaipao.model;

import com.kuaipao.model.event.WebBaseEvent;

/**
 * Created by MVEN on 16/3/11.
 */
public class WebBindMerchant extends WebBaseEvent {

    public long merchantID;

    public String msg;

    public WebBindMerchant(boolean bResult) {
        super(bResult);
    }

    public WebBindMerchant() {
        this(false);
    }
}
