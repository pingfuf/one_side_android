package com.oneside.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.base.CardConfig;
import com.oneside.base.inject.From;
import com.oneside.base.BaseActivity;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.manager.CardManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.model.beans.Banner;
import com.oneside.model.beans.XGym;
import com.oneside.model.beans.XRole;
import com.oneside.model.event.CourseDraftChangedEvent;
import com.oneside.model.event.LoginStatusChangedEvent;
import com.oneside.model.request.BaseShowApiRequestParam;
import com.oneside.model.response.BaseShowApiResponse;
import com.oneside.model.response.CoachCourseDetailResponse;
import com.oneside.ui.coach.CoachPersonalCustomerActivity;
import com.oneside.ui.course.CoachCourseRecordDetailActivity;
import com.oneside.ui.course.CoachPersonalCourseActivity;
import com.oneside.ui.coach.CoachReceiveCustomerActivity;
import com.oneside.ui.coach.CoachCustomerListActivity;
import com.oneside.ui.home.MainViewPagerAdapter;
import com.oneside.ui.home.OpenDoorActivity;
import com.oneside.ui.home.AdvertisementView;
import com.oneside.R;
import com.oneside.ui.home.MainGridAdapter;
import com.oneside.hy.CardWebActivity;
import com.oneside.ui.news.NewsFragment;
import com.oneside.ui.view.MainViewPager;
import com.oneside.ui.view.TabItemView;
import com.oneside.ui.view.TabView;
import com.oneside.utils.AppUpdateHelper;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;
import com.show.api.ShowApiRequest;
import com.sina.weibo.sdk.utils.LogUtil;

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

    @From(R.id.tv_items)
    private TabView tvItems;

    @From(R.id.iv_temp)
    private ImageView ivTemp;

    private Fragment newsFragment;

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
        new AppUpdateHelper(this).checkUpdate(true);

        initUI();
        fetchTemp();
    }

    private void fetchTemp() {
        final BaseShowApiRequestParam requestParam = new BaseShowApiRequestParam();
        requestParam.addParam("page", 1);
        requestParam.addParam("id", "/dp/18238.html");
        startRequest(XService.Temp, requestParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String res=new ShowApiRequest("http://route.showapi.com/955-2",
                        requestParam.appId, requestParam.secret)
                        .addTextPara("id", "/dp/18238.html")
                        .post();
                LogUtils.e(res);
            }
        }).start();

    }

    private void initUI() {
        TabItemView newsItemView = new TabItemView(this);
        newsItemView.setIconText(new int[]{
                R.drawable.ic_coupon_color_user, R.drawable.ic_coupon_color}, "新闻");
        tvItems.addView(newsItemView);

        ivTemp.setOnClickListener(this);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
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
     */
    private List<Fragment> getFunctionData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(createNewsFragment());

        return fragments;
    }

    private Fragment createNewsFragment() {
        Fragment fragment = new NewsFragment();

        TabView item = new TabView(this);
        item.addView(null);

        return fragment;
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
    public void onClick(View v) {
        super.onClick(v);
        if(v == ivTemp) {
            fetchTemp();
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
