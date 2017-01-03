package com.oneside.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.manager.CardSessionManager;
import com.oneside.model.event.LoginStatusChangedEvent;
import com.oneside.model.response.MembershipCardsResponse;
import com.oneside.utils.LogUtils;
import com.oneside.base.net.UrlRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class JumpCenterActivity extends BaseActivity {
    public static final int ACTIVE_CARD_TYPE = 1;

    @XAnnotation
    JumpPageParam mPageParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            finish();
        }

        View view = new View(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.TRANSPARENT);
//        setContentView(view);

        EventBus.getDefault().register(this);
        showLoadingDialog();
        jump();
    }

    private void jump() {
        if(mPageParam == null) {
            finish();
        }

        switch (mPageParam.jumpType) {
            case ACTIVE_CARD_TYPE:
                checkUserValid();
                checkUserActive();
                break;
            default:
                finish();
                break;
        }
    }

    /**
     * 用户从登陆状态变为非登陆状态，关闭页面
     *
     * @param event 用户登陆状态转变事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userInfoUpdateEvent(LoginStatusChangedEvent event) {
        if (!CardSessionManager.getInstance().isLogin()) {
            finish();
        }
    }

    /**
     * 判断用户是否有激活卡片历史
     * 通过发送请求，判断返回结果
     */
    private void checkUserActive() {
        BaseRequestParam requestParam = new BaseRequestParam();
        requestParam.addUrlParams(mPageParam.gymId);
        LogUtils.e("getUserInfo from gymId = %s", mPageParam.gymId);
//        startRequest(XService.DoesGymContainUser, requestParam);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
//        super.onNetError(request, statusCode);
//        if(isSameUrl(XService.DoesGymContainUser, request)) {
//            finish();
//        } else if(isSameUrl(XService.LatestGymUserInfo, request)) {
//            gotoUserActiveCardActivity(false, null, 0, null, null);
//        } else {
//            finish();
//        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        dismissLoadingDialog();
//        if(isSameUrl(XService.DoesGymContainUser, request)) {
//            gotoUserActiveCardActivity(false, null, 0, null, null);
//        } else if(isSameUrl(XService.LatestGymUserInfo, request)) {
//            gotoUserActiveCardActivity(false, null, 0, null, null);
//        } else {
//            finish();
//        }
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
//        if (isSameUrl(XService.DoesGymContainUser, request)) {
//            LogUtils.e("check userActive");
//            if (isUserActive((CoachPersonalCardsResponse)data)) {
//                LogUtils.e("check userActive true");
//                gotoUserActiveSuccessActivity();
//            } else {
//                //场馆没有用户信息，查看用户信息是否存在
//                LogUtils.e("check userActive false");
//                fetchLatestUserInfo();
//            }
//        } else if (isSameUrl(XService.LatestGymUserInfo, request)) {
//            //获取用户最新信息
//            CoachPersonalCardsResponse response = (CoachPersonalCardsResponse) data;
//            if(response != null && response.data != null) {
//                XUser user = response.data;
//                gotoUserActiveCardActivity(true, user.name, user.sex, user.birthday, user.avatar);
//            } else {
//                gotoUserActiveCardActivity(false, null, 0, null, null);
//            }
//        }
    }

    /**
     * 场馆是否有用户信息
     *
     * @param result 场馆的用户信息接口返回结果
     * @return 场馆是否有用户信息
     */
    private boolean isUserActive(MembershipCardsResponse result) {
//        return result != null && result.data != null && !LangUtils.isEmpty(result.data.avatar);
        return false;
    }

    /**
     * 跳转到开卡成功界面
     */
    private void gotoUserActiveSuccessActivity() {
        dismissLoadingDialog();
//        UserActiveSuccessActivity.UserActiveSuccessPageParam param
//                = new UserActiveSuccessActivity.UserActiveSuccessPageParam();
//        param.gymId = mPageParam.gymId;
//        xStartActivity(UserActiveSuccessActivity.class, param);
        finish();
    }

    /**
     * 跳转到开卡界面
     *
     * @param isActive  用户是否曾经激活过
     * @param name      用户名
     * @param sex       用户性别
     * @param birthday  用户生日
     * @param faceImg   用户脸部图片
     */
    private void gotoUserActiveCardActivity(boolean isActive, String name, int sex, String birthday, String faceImg) {
        dismissLoadingDialog();
//        UserActiveCardActivity.ActivePageParam pageParam = new UserActiveCardActivity.ActivePageParam();
//        pageParam.gymId = mPageParam.gymId;
//        pageParam.isActive = isActive;
//        pageParam.name = name;
//        pageParam.sex = sex;
//        pageParam.birthday = birthday;
//        pageParam.faceImg = faceImg;
//        xStartActivity(UserActiveCardActivity.class, pageParam);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == BaseActivity.PHONE_CONFIRM_PAGE) {
            //用户登陆返回，结束页面
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        LogUtils.e(TAG + " is finished");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 跳转页面参数
     */
    public static class JumpPageParam extends BasePageParam {
        /**
         * 跳转类型，用来区分跳转到什么界面
         */
        public int jumpType;

        /**
         * 场馆id
         */
        public long gymId;
    }
}
