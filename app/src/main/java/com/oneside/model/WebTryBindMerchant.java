package com.oneside.model;

import com.oneside.model.event.WebBaseEvent;

/**
 * Created by MVEN on 16/3/11.
 */
public class WebTryBindMerchant extends WebBaseEvent {

    public boolean isInBindingMerchantActivity = false;

    public long merchantID;

    //    失败：code=1 后续操作：返回到卡片列表
//        卡片数已经达到上限
//        已经绑定了卡片, 请先解绑
//    需要更多信息：code=2 后续操作：进行卡片绑定
//          初次绑定卡片
//          需要录入卡片有效期（适用于解绑之后再绑定的情况）
    public int code;

    public String msg;

    public WebTryBindMerchant(boolean bResult) {
        super(bResult);
    }

    public WebTryBindMerchant() {
        this(false);
    }
}
