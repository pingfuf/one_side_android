package com.kuaipao.manager;

import com.alibaba.fastjson.JSON;
import com.kuaipao.base.net.RequestQueueManager;
import com.kuaipao.base.net.XResponse;
import com.kuaipao.model.beans.XUser;
import com.kuaipao.model.event.SessionChangedEvent;

import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.SysUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 用户session管理工具类
 *
 * @author Guo Ming Class in charge of login, logout and keep session
 */
public class CardSessionManager {
    /** 本地存储的用户信息key */
    public static final String USER_KEY = "xx_user";

    /** 本地存储的token信息key */
    public static final String TOKEN_KEY = "remember_token";

    private static CardSessionManager mManager;
    private NetworkStatus networkStatus = NetworkStatus.OnLine;
    private LoginStatus loginStatus = LoginStatus.Logout;
    private XUser mUser;
    private String mToken;
    private boolean userCareGymCountChanged = false;

    public final ArrayList<Long> closedActiveCard = new ArrayList<>();

    private CardSessionManager() {
        //检查登陆状态
        loginStatus = checkLogin() ? LoginStatus.Login : LoginStatus.Logout;

        //初始化User信息
        initUserInfo(isLogin());

        //初始化token
        mToken = IOUtils.getPreferenceValue(TOKEN_KEY);
    }

    public synchronized static CardSessionManager getInstance() {
        CardSessionManager manager = mManager;
        if(manager == null) {
            synchronized (CardSessionManager.class) {
                manager = mManager;
                if(manager == null) {
                    manager = new CardSessionManager();
                    mManager = manager;
                }
            }
        }

        return manager;
    }

    public NetworkStatus getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(NetworkStatus networkStatus) {
        this.networkStatus = networkStatus;
    }

    public XUser getUser() {
        return mUser;
    }

    public void updateFromServer() {
        if (isOnLine()) {
            if (isLogin()) {
                CardCommentPostManager.getInstance().autoPostComment();
            }
        }
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public String getToken() {
        return mToken;
    }

    public boolean isLogin() {
        return this.loginStatus == LoginStatus.Login;
    }

    public boolean isOnLine() {
        return this.networkStatus == NetworkStatus.OnLine;
    }

    /**
     * 判断当前用户是否是管理员用户
     *
     * @return 是否是管理员
     */
    public boolean isAdmin() {
        boolean flag = false;
        if(mUser != null) {
            flag = mUser.isAdmin;
        }

        return flag;
    }

    /**
     * 存储用户信息
     *
     * @param user 用户信息
     */
    public void restoreSession(XUser user) {
        restoreSession(user, null);
    }

    /**
     * 保存缓存信息
     *
     * @param user  用户信息
     * @param token 用户token
     */
    public void restoreSession(XUser user, String token) {
        boolean isLogin = user != null || LangUtils.isNotEmpty(token);
        updateLoginStatus(isLogin ? LoginStatus.Login : LoginStatus.Logout);

        if (user != null) {
            restoreLocalUserInfo(user);
        }

        if (!LangUtils.isEmpty(token)) {
            mToken = token;
            IOUtils.savePreferenceValue(TOKEN_KEY, token);
        }
    }

    /**
     * 清空本地缓存
     */
    public void clearSession() {
        mUser = new XUser();
        mToken = "";
        clearPreference();
        updateLoginStatus(LoginStatus.Logout);
        RequestQueueManager.clearToken();
    }

    private void clearPreference() {
        ArrayList<String> keyMaps = new ArrayList<>();
        keyMaps.add(TOKEN_KEY);
        keyMaps.add(USER_KEY);

        //清空草稿箱
        keyMaps.add("failed_course_detail");
        IOUtils.clearPreference(keyMaps);
    }

    public boolean isUserCareGymCountChanged() {
        return userCareGymCountChanged;
    }

    public void setUserCareGymCountChanged(boolean userCareGymCountChanged) {
        this.userCareGymCountChanged = userCareGymCountChanged;
    }

    public void addClosedActiveID(long id) {
        closedActiveCard.add(id);
    }

    private void initUserInfo(boolean isLogin) {
        if (isLogin) {
            String localString = IOUtils.getPreferenceValue(USER_KEY);
            if (LangUtils.isNotEmpty(localString)) {
                mUser = XResponse.parseJson(XUser.class, localString, 0);
            }
        }

        if (mUser == null) {
            mUser = new XUser();
        }

        mUser.phone = SysUtils.PHONE_NUMBER;
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    private void restoreLocalUserInfo(XUser user) {
        if (user == null) {
            return;
        }

        if(mUser == null) {
            initUserInfo(isLogin());
        }

        mUser.name = user.name;
        mUser.isAdmin = user.isAdmin;
        if (!LangUtils.isEmpty(user.phone)) {
            mUser.phone = user.phone;
        }

        String json = JSON.toJSONString(mUser);
        IOUtils.savePreferenceValue(USER_KEY, json);
    }

    private boolean checkLogin() {
        String token = IOUtils.getPreferenceValue(TOKEN_KEY);
        return LangUtils.isNotEmpty(token);
    }

    /**
     * 更新用户的登陆状态
     *
     * @param logMode 登陆状态
     */
    private void updateLoginStatus(LoginStatus logMode) {
        if (this.loginStatus != logMode) {
            this.loginStatus = logMode;

            if (isLogin()) {
                updateFromServer();
            }

            EventBus.getDefault().post(new SessionChangedEvent(logMode));
        }
    }

    public enum NetworkStatus {
        OnLine, OffLine
    }

    public enum LoginStatus {
        Login, Logout
    }
}
