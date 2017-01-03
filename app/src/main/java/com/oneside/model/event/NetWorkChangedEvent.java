package com.oneside.model.event;

import com.oneside.manager.CardSessionManager;

/**
 * Created by ZhanTao on 2/2/16.
 */
public class NetWorkChangedEvent {
    public CardSessionManager.NetworkStatus networkStatus;

    public NetWorkChangedEvent(CardSessionManager.NetworkStatus mode) {
        this.networkStatus = mode;
    }
}
