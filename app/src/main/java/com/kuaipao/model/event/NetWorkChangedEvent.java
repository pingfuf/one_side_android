package com.kuaipao.model.event;

import com.kuaipao.manager.CardSessionManager;

/**
 * Created by ZhanTao on 2/2/16.
 */
public class NetWorkChangedEvent {
    public CardSessionManager.NetworkStatus networkStatus;

    public NetWorkChangedEvent(CardSessionManager.NetworkStatus mode) {
        this.networkStatus = mode;
    }
}
