package com.kuaipao.model;

import com.kuaipao.model.event.WebBaseEvent;

/**
 * Created by MVEN on 16/3/11.
 */
public class WebRaiseUpMerchant extends WebBaseEvent {

    public long merchantID;

    public String msg;

    public WebRaiseUpMerchant(boolean bResult) {
        super(bResult);
    }

    public WebRaiseUpMerchant() {
        this(false);
    }
}
