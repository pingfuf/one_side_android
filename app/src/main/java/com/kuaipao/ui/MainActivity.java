package com.kuaipao.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kuaipao.base.CardConfig;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.base.net.XService;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.manager.CardManager;
import com.kuaipao.manager.CardSessionManager;
import com.kuaipao.model.beans.Banner;
import com.kuaipao.model.beans.XGym;
import com.kuaipao.model.beans.XRole;
import com.kuaipao.model.beans.XUser;
import com.kuaipao.model.event.CourseDraftChangedEvent;
import com.kuaipao.model.event.LoginStatusChangedEvent;
import com.kuaipao.model.response.BannerListResponse;
import com.kuaipao.model.response.CoachCourseDetailResponse;
import com.kuaipao.model.response.UserInfoResponse;
import com.kuaipao.ui.coach.CoachPersonalCustomerActivity;
import com.kuaipao.ui.course.CoachCourseRecordDetailActivity;
import com.kuaipao.ui.course.CoachPersonalCourseActivity;
import com.kuaipao.ui.coach.CoachReceiveCustomerActivity;
import com.kuaipao.ui.coach.CoachCustomerListActivity;
import com.kuaipao.ui.home.OpenDoorActivity;
import com.kuaipao.ui.home.AdvertisementView;
import com.kuaipao.manager.R;
import com.kuaipao.ui.home.MainGridAdapter;
import com.kuaipao.ui.view.NoScrollGridView;
import com.kuaipao.ui.web.CardWebActivity;
import com.kuaipao.utils.AppUpdateHelper;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.SysUtils;
import com.kuaipao.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 小熊助手首页
 * <p/>
 * Created by Guo Ming on 3/27/15
 */
public class MainActivity extends BaseActivity
        implements AdvertisementView.IAdvertisementChosenHandler, MainGridAdapter.OnCoachCourseUploadFailedHandler {
    private static final long DELAY_TO_EXIT = 1500;

    private boolean canExit;
    private MainGridAdapter mAdapter;
    private List<Banner> mBanners;
    private List<XGym> mGyms;
    private boolean isBannerRequestFinished;
    private boolean isUserInfoRequestFinished;
    private CoachCourseDetailResponse mDetailResponse;

    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        //展示加载动画
        //必须当banner列表和用户信息都返回的时候，才能结束加载动画
        showLoadingDialog();
        new AppUpdateHelper(this).checkUpdate(true);
    }

    private void initUI() {

    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        if (isSameUrl(XService.BannerList, request)) {
            isBannerRequestFinished = true;
        } else if (isSameUrl(XService.UserInfo, request)) {
            isUserInfoRequestFinished = true;
            freshGridView(CardSessionManager.getInstance().isAdmin(), null);
        }

        if (isBannerRequestFinished && isUserInfoRequestFinished) {
            dismissLoadingDialog(500);
        }
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        if (isSameUrl(XService.BannerList, request)) {
            isBannerRequestFinished = true;
        } else if (isSameUrl(XService.UserInfo, request)) {
            isUserInfoRequestFinished = true;
            freshGridView(CardSessionManager.getInstance().isAdmin(), null);
        }

        if (isBannerRequestFinished && isUserInfoRequestFinished) {
            dismissLoadingDialog(500);
        }
    }

    /**
     * 刷新功能模块
     * 如果是管理员，显示全部功能模块
     * 如果不是管理员，是教练，显示全部功能模块
     * 如果不是管理员，也不是教练，显示前三个功能模块
     *
     * @param isAdmin 是否是管理员
     * @param roles   角色列表
     */
    private void freshGridView(boolean isAdmin, List<XRole> roles) {
    }

    /**
     * 判断用户是否是管理员
     *
     * @param roles 用户的权限列表
     * @return 是否有管理员权限
     */
    private boolean isAdminRole(List<XRole> roles) {
        boolean flag = false;
        if (!LangUtils.isEmpty(roles)) {
            for (XRole role : roles) {
                if (role != null && ("教练".equals(role.name) || "管理员".equals(role.name))) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private void gotoItemPage(MainItem item) {
        if (item == null || item.clazz == null) {
            return;
        }

        BasePageParam pageParam = null;
        if (item.clazz == OpenDoorActivity.class) {
            OpenDoorActivity.OpenDoorPageParam param = new OpenDoorActivity.OpenDoorPageParam();
            param.mGyms = mGyms;
            pageParam = param;
        }

        xStartActivity(item.clazz, pageParam);
    }

    /**
     * 加载首页模块，如果是管理员，显示全部模块；否则，显示前三个模块
     *
     * @param isAdmin 是否是管理员
     * @return 首页模块列表
     */
    private List<MainItem> getFunctionData(boolean isAdmin) {
        List<MainItem> items = new ArrayList<>();
        MainItem item1 = new MainItem();
        item1.resId = R.drawable.ic_open_door_icon;
        item1.name = "扫码开门";
        item1.clazz = OpenDoorActivity.class;
        items.add(item1);

        MainItem item2 = new MainItem();
        item2.resId = R.drawable.ic_customer_reception;
        item2.name = "顾客接待";
        item2.clazz = CoachReceiveCustomerActivity.class;
        items.add(item2);

        MainItem item3 = new MainItem();
        item3.resId = R.drawable.ic_coach_customer;
        item3.name = "我的顾客";
        item3.clazz = CoachCustomerListActivity.class;
        items.add(item3);

        if (isAdmin) {
//            MainItem item4 = new MainItem();
//            item4.resId = R.drawable.ic_physical_exam;
//            item4.name = "会员体检";
////            item4.clazz = TempActivity.class;
//            if (CardConfig.isDevBuild()) {
//                //dev环境下测试使用
//                items.add(item4);
//            }

            MainItem item5 = new MainItem();
            item5.resId = R.drawable.ic_coach_personal_course;
            item5.name = "私教课";
            item5.clazz = CoachPersonalCourseActivity.class;

            mDetailResponse = getLocalResponse();
            if(mDetailResponse != null) {
                item5.isFailed = true;
            }
            items.add(item5);

            MainItem item6 = new MainItem();
            item6.resId = R.drawable.ic_membership;
            item6.name = "私教会员";
            item6.clazz = CoachPersonalCustomerActivity.class;
            items.add(item6);
        }

        if(items.size() % 2 == 1) {
            items.add(new MainItem());
        }

        return items;
    }

    @Override
    public void onAdvertiseItemChosen(Banner banner) {
        if (banner == null) {
            return;
        }

        CardWebActivity.WebPageParam pageParam = new CardWebActivity.WebPageParam();
        pageParam.url = banner.url;
        xStartActivity(CardWebActivity.class, pageParam);
    }

    @Override
    public void onBackPressed() {
        if (canExit) {
            moveTaskToBack(true);
            CardManager.exit();
        } else {
            ViewUtils.showToast(getResources().getString(R.string.exit_warn), Toast.LENGTH_SHORT);
        }

        canExit = true;
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                canExit = false;
            }
        }, DELAY_TO_EXIT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCourseDraftChangedEvent(CourseDraftChangedEvent event) {
        LogUtils.d("courseDraftChangeEvent %s",event);
        mDetailResponse = getLocalResponse();
        for(MainItem item : mAdapter.getData()) {
            if(item != null && item.clazz == CoachPersonalCourseActivity.class) {
                item.isFailed = mDetailResponse != null;
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LoginActivity.LOGIN_PAGE_CODE) {
            if(mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            if(resultCode != RESULT_OK) {
                finish();
            }
        }
    }

    private void showExistDialog() {
        mDialog = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.shareDiaLogWindowAnim);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.show();

        mDialog.setContentView(R.layout.ui_exist_item);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tvTitle = (TextView) mDialog.findViewById(R.id.tv_title);
        tvTitle.setText(CardSessionManager.getInstance().getUser().name);
        TextView tvExist  = (TextView) mDialog.findViewById(R.id.tv_exist);
        tvExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoExistDialog();
            }
        });

        if(CardSessionManager.getInstance().isLogin()) {
            tvExist.setText("退出登录");
        } else {
            tvExist.setText("请登录");
        }

        mDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    private void showDoExistDialog() {
        CustomDialog dialog = new CustomDialog(this);

        dialog.setMessage("确认退出当前账号吗？");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CardSessionManager.getInstance().clearSession();
                EventBus.getDefault().post(new LoginStatusChangedEvent(false));
                xStartActivity(LoginActivity.class, LoginActivity.LOGIN_PAGE_CODE);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void uploadCoachCourseDetail() {
        if(mDetailResponse == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putLong("arrangement_id", mDetailResponse.id);
        bundle.putLong("user_id", mDetailResponse.member.id);
        bundle.putSerializable("course_detail", mDetailResponse);
        bundle.putBoolean("from_draft", true);
        xStartActivity(CoachCourseRecordDetailActivity.class, bundle, 1);
    }

    /**
     * 功能模块显示的数据结构
     */
    public static class MainItem {
        public int resId;

        public String name;

        public boolean isFailed;

        public Class<? extends BaseActivity> clazz;
    }

    private CoachCourseDetailResponse getLocalResponse() {
        return CardManager.getFailedCourseDetail();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
