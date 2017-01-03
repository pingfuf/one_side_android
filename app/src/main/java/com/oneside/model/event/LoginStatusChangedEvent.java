package com.oneside.model.event;

/**
 * 登陆状态改变通知消息
 *
 * Created by ZhanTao on 2/2/16.
 */
public class LoginStatusChangedEvent {
    public boolean isLogin;

    public LoginStatusChangedEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
