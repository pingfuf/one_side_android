package com.oneside.model.event;

import com.oneside.manager.CardSessionManager;

/**
 * Created by ZhanTao on 2/2/16.
 */
public class SessionChangedEvent {
    public CardSessionManager.LoginStatus loginStatus;

    public SessionChangedEvent(CardSessionManager.LoginStatus status) {
        this.loginStatus = status;
    }
}
