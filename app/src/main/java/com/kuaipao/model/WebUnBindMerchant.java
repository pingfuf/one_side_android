package com.kuaipao.model;

import com.kuaipao.model.event.WebBaseEvent;

/**
 * Created by MVEN on 16/3/11.
 */
public class WebUnBindMerchant extends WebBaseEvent {

    public long[] merchantIDs;

    public String msg;

    public WebUnBindMerchant(boolean bResult) {
        super(bResult);
    }

    public WebUnBindMerchant() {
        this(false);
    }
}
