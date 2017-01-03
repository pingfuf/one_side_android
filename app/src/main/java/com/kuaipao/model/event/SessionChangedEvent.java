package com.kuaipao.model.event;

import com.kuaipao.manager.CardSessionManager;

/**
 * Created by ZhanTao on 2/2/16.
 */
public class SessionChangedEvent {
    public CardSessionManager.LoginStatus loginStatus;

    public SessionChangedEvent(CardSessionManager.LoginStatus status) {
        this.loginStatus = status;
    }
}
