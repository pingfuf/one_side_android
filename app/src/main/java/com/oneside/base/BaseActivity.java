package com.oneside.base;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.android.volley.Request;
import com.oneside.base.net.RequestDelegate;
import com.oneside.base.net.XService;
import com.oneside.model.event.LoginStatusChangedEvent;
import com.oneside.ui.LoginActivity;
import com.oneside.base.inject.InjectUtils;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.IService;
import com.oneside.base.net.XRequest;
import com.oneside.base.net.XResponse;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.view.XTitleBar;
import com.oneside.manager.CardActivityManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.R;
import com.oneside.ui.MainActivity;
import com.oneside.ui.web.CardWebActivity;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.StatusBarCompat;
import com.oneside.utils.SystemBarTintManager;
import com.oneside.utils.ViewUtils;
import com.oneside.ui.view.CustomProgressDialog;
import com.oneside.base.net.UrlRequest;
import com.oneside.utils.WebUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 所有的Activity都要继承BaseActivity
 *
 * @author pingfu
 */
public abstract class BaseActivity extends FragmentActivity implements RequestDelegate, View.OnClickListener {
    protected static final int PHONE_CONFIRM_PAGE = 801;
    //登陆失效code
    protected static final int LOGIN_TOKEN_EXPIRED_CODE = 1001;
    //Activity的RootView
    protected RelativeLayout mRootView;
    protected View mContentView;

    //页面的title
    protected XTitleBar mTitleBar;
    private Field mPageParamField;

    //页面加载弹框动画
    protected CustomProgressDialog progressDialog = null;
    protected String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        CardActivityManager.onCreated(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRootView = new RelativeLayout(this);
        super.setContentView(mRootView);
        initTitleBar();

        //初始化参数
        if (bundle != null) {
            initPageParam(bundle);
        } else {
            if (getIntent() != null) {
                initPageParam(getIntent().getExtras());
            }
        }

        initStatusBar();
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, null);
        mRootView.setBackgroundResource(R.color.layout_background);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        mContentView = view;
        LayoutParams paramsActivityContent = new LayoutParams(-1, -1);
        getWindow().setBackgroundDrawable(null);
        mRootView.addView(view, paramsActivityContent);
        InjectUtils.autoInject(this);
    }

    /**
     * 初始化View
     */
    private void initStatusBar() {
        if (!isStatusBarOverlay()) {
            initSystemBar();
        } else {
            StatusBarCompat.translucentStatusBar(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        CardActivityManager.onResumed(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        CardActivityManager.onPaused(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && getPageParam() != null) {
            outState.putSerializable(BasePageParam.PARAM_KEY, getPageParam());
        }
    }

    /**
     * 初始化页面参数
     *
     * @param bundle bundle
     */
    protected void initPageParam(Bundle bundle) {
        Field[] paramFields = getClass().getDeclaredFields();
        for (Field field : paramFields) {
            if (field.isAnnotationPresent(XAnnotation.class)) {
                field.setAccessible(true);//允许范围私有变量
                if (BasePageParam.class.isAssignableFrom(field.getType())) {
                    try {
                        mPageParamField = field;

                        Serializable data = getSerializableParamFromBundle(bundle);
                        if (data != null && data instanceof BasePageParam) {
                            field.set(this, field.getType().cast(data));
                        } else {
                            field.set(this, field.getType().newInstance());
                        }

                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        LogUtils.e("init page param failed");
                    }
                }
            }
        }
    }

    private Serializable getSerializableParamFromBundle(Bundle bundle) {
        Serializable pageParamSerializable = null;
        if (bundle != null) {
            pageParamSerializable = bundle.getSerializable(BasePageParam.PARAM_KEY);
        }

        return pageParamSerializable;
    }

    /**
     * 得到页面参数
     *
     * @return 页面参数
     */
    private BasePageParam getPageParam() {
        BasePageParam pageParam = null;
        if (mPageParamField == null) {
            Field[] paramFields = getClass().getDeclaredFields();
            for (Field field : paramFields) {
                if (field.isAnnotationPresent(XAnnotation.class)) {
                    mPageParamField = field;
                    break;
                }
            }
        }

        if (mPageParamField != null) {
            mPageParamField.setAccessible(true);
            try {
                Object obj = mPageParamField.get(this);
                if (obj != null && obj instanceof BasePageParam) {
                    pageParam = (BasePageParam) obj;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return pageParam;
    }

    /**
     * 发送HTTP请求，返回结果在requestFinished、requestFailed中处理
     *
     * @param service 后端服务
     */
    protected void startRequest(IService service) {
        startRequest(service, null);
    }

    /**
     * 发送HTTP请求，返回结果在requestFinished、requestFailed中处理
     *
     * @param service 后端服务
     * @param param   HTTP请求参数
     */
    protected void startRequest(IService service, BaseRequestParam param) {
        startRequest(service, param, 0);
    }

    /**
     * 发送HTTP请求，返回结果在requestFinished、requestFailed中处理
     *
     * @param service     后端服务
     * @param param       HTTP请求参数
     * @param requestCode 该请求的编号
     */
    protected void startRequest(IService service, BaseRequestParam param, int requestCode) {
        String tag = null;
        if (getClass() != null) {
            tag = getClass().getSimpleName();
        }
        startRequest(service, param, requestCode, tag);
    }

    /**
     * 发送HTTP请求，返回结果在requestFinished、requestFailed中处理
     *
     * @param service     后端服务
     * @param param       HTTP请求参数
     * @param requestCode 该请求的编号
     * @param requestTag  该请求的tag
     */
    protected void startRequest(IService service, BaseRequestParam param, int requestCode, String requestTag) {
        if (service == null || isFinishing()) {
            return;
        }

        if(service != XService.LOGIN && !CardSessionManager.getInstance().isLogin()) {
            ViewUtils.showToast("请登录", Toast.LENGTH_LONG);
            xStartActivity(MainActivity.class);
        } else {
            XRequest.startRequest(this, service, param, requestCode, requestTag);
        }
    }

    /**
     * 发送网络请求
     *
     * @param url 后端接口URL
     */
    protected void startRequest(String url) {
        startRequest(url, (BaseRequestParam) null);
    }

    /**
     * 发送网络请求
     *
     * @param url   后端接口URL
     * @param param 请求参数
     */
    protected void startRequest(String url, BaseRequestParam param) {
        startRequest(url, param, BaseResult.class);
    }

    /**
     * 发送网络请求
     *
     * @param url   后端接口URL
     * @param clazz 后端结构返回数据的数据结构
     */
    protected void startRequest(String url, Class<? extends BaseResult> clazz) {
        startRequest(url, null, clazz);
    }

    /**
     * 发送网络请求
     *
     * @param url   后端接口URL
     * @param param 请求参数
     * @param clazz 后端结构返回数据的数据结构
     */
    protected void startRequest(String url, BaseRequestParam param, Class<? extends BaseResult> clazz) {
        startRequest(url, param, clazz, Request.Method.GET);
    }

    /**
     * 发送网络请求
     *
     * @param url         后端接口URL
     * @param param       请求参数
     * @param clazz       后端结构返回数据的数据结构
     * @param requestType 请求类型，默认为get
     */
    protected void startRequest(String url, BaseRequestParam param,
                                Class<? extends BaseResult> clazz, int requestType) {
        startRequest(url, param, clazz, requestType, 0);
    }

    /**
     * 发送网络请求
     *
     * @param url         后端接口URL
     * @param param       请求参数
     * @param clazz       后端结构返回数据的数据结构
     * @param requestType 请求类型，默认为get
     * @param requestCode 请求序列号
     */
    protected void startRequest(String url, BaseRequestParam param, Class<? extends BaseResult> clazz,
                                int requestType, int requestCode) {
        startRequest(url, param, clazz, requestType, requestCode, TAG);
    }

    /**
     * 发送网络请求
     *
     * @param url         后端接口URL
     * @param param       请求参数
     * @param clazz       后端结构返回数据的数据结构
     * @param requestType 请求类型，默认为get
     * @param requestCode 请求序列号
     * @param requestTag  请求Tag
     */
    protected void startRequest(String url, BaseRequestParam param, Class<? extends BaseResult> clazz,
                                int requestType, int requestCode, String requestTag) {
        XRequest.startRequest(this, url, clazz, param, requestType, requestCode, requestTag);
    }

    /**
     * 页面跳转的时候弹框动画
     */
    public void showLoadingDialog() {
        showLoadingDialog(getString(R.string.loading));
    }

    /**
     * 页面跳转的时候动画效果
     *
     * @param loadingTip loading提示
     */
    protected void showLoadingDialog(String loadingTip) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage(loadingTip);
            progressDialog.setCanceledOnTouchOutside(false);
            android.view.WindowManager.LayoutParams lay = progressDialog.getWindow().getAttributes();
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            Rect rect = new Rect();
            View view = getWindow().getDecorView();
            view.getWindowVisibleDisplayFrame(rect);
            lay.height = dm.heightPixels - rect.top;
            lay.width = dm.widthPixels;
        }

        if (!isFinishing() && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 页面跳转结束弹窗
     */
    public void dismissLoadingDialog() {
        dismissLoadingDialog(0);
    }

    public void dismissLoadingDialog(long time) {
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }, time);
    }

    /**
     * titleBar的类型，如果返回true，titleBar是在最上层，否则，titleBar和contentView在同一个图层
     *
     * @return titleBar的类型
     */
    protected boolean isTitleBarOverlay() {
        return false;
    }

    /**
     * 是否设置沉浸式栏
     *
     * @return 是否设置沉浸式栏
     */
    protected boolean isStatusBarOverlay() {
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRootView.setFitsSystemWindows(true);
            mRootView.setClipToPadding(false);
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.black);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void setTitle(CharSequence title) {
        addTitleBar();

        mTitleBar.setTitle(title != null ? title.toString() : "");
    }

    /**
     * 设置标题
     *
     * @param title      标题名称
     * @param hasBackImg 是否有返回图标
     */
    public void setTitle(CharSequence title, boolean hasBackImg) {
        addTitleBar();
        mTitleBar.setTitle(title != null ? title.toString() : "", hasBackImg);
    }

    /**
     * 设置标题
     *
     * @param title      标题名称
     * @param hasBackImg 是否有返回图标
     */
    public void setTitle(CharSequence title, boolean hasBackImg, View... rightParams) {
        addTitleBar();
        mTitleBar.setTitle(title != null ? title.toString() : "", hasBackImg, rightParams);
    }

    /**
     * 添加titleBar
     */
    private void addTitleBar() {
        if (mTitleBar == null) {
            initTitleBar();
        }

        if (mTitleBar.getParent() == mRootView) {
            return;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
        if (isTitleBarOverlay()) {
            mRootView.addView(mTitleBar, params);
            mTitleBar.bringToFront();
            mTitleBar.setBackgroundColor(Color.TRANSPARENT);
        } else {
            if (mContentView != null && mContentView.getParent() == mRootView) {
                mRootView.removeView(mContentView);
            }
            mRootView.addView(mTitleBar, params);
            RelativeLayout.LayoutParams cParams = new RelativeLayout.LayoutParams(-1, -1);
            cParams.addRule(RelativeLayout.BELOW, mTitleBar.getId());
            if (mContentView != null) {
                mRootView.addView(mContentView, cParams);
            }
        }
    }

    /**
     * 初始化titleBar
     */
    private void initTitleBar() {
        mTitleBar = new XTitleBar(this);
        mTitleBar.setId(R.id.base_activity_title_bar);
        mTitleBar.getLeftContent().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 网络请求正常返回的回调
     *
     * @param request 请求内容
     */
    @Override
    public void requestFinished(final UrlRequest request) {
        //判断当前请求结果是否需要处理：Activity必须还存在，必须是有效的返回结果
        if (isFinishing() || !illegalResponse(request)) {
            return;
        }
        long time = System.currentTimeMillis();
        final BaseResult result = XResponse.parseResponse(request);
        if (result == null) {
            dismissLoadingDialog();
            return;
        }

        LogUtils.e("parseJsonTime = %s", System.currentTimeMillis() - time);
        //网络请求返回并不在主线程中，需要特殊处理
        ViewUtils.runInHandlerThread(new Runnable() {
            @Override
            public void run() {
                if (result.code == 0) {
                    onResponseSuccess(request, result);
                } else {
                    onResponseError(request, result.code, result.msg);
                }
            }
        });
    }

    /**
     * 网络链接失败的回调，可能是网络异常
     *
     * @param request    请求内容
     * @param statusCode 请求结果状态
     */
    @Override
    public void requestFailed(final UrlRequest request, final int statusCode, final String message) {
        //判断当前请求结果是否需要处理：Activity必须没有回收，必须是有效的返回结果
        if (isFinishing()) {
            return;
        }

        //网络请求并不在主线程中，需要特殊处理
        ViewUtils.runInHandlerThread(new Runnable() {
            @Override
            public void run() {
                if(statusCode == LOGIN_TOKEN_EXPIRED_CODE) {
                    ViewUtils.showToast(message, Toast.LENGTH_LONG);
                    CardSessionManager.getInstance().clearSession();
                    EventBus.getDefault().post(new LoginStatusChangedEvent(false));
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    xStartActivity(LoginActivity.class, LoginActivity.LOGIN_PAGE_CODE);
                    return;
                }

                if (message == null) {
                    onNetError(request, statusCode);
                } else {
                    onResponseError(request, statusCode, message);
                }
            }
        });
    }

    /**
     * 处理网络请求成功的结果，如果某些接口中没有data，则data为null，使用时需要特殊处理
     *
     * @param data 返回结果中data部分的数据，也就是页面真正需要的数据
     */
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        //处理网络请求成功结果
    }

    /**
     * 网络请求成功，但是返回结果不符合要求
     *
     * @param request 网络请求
     * @param code    错误信息
     */
    protected void onResponseError(UrlRequest request, int code, String message) {
        //处理返回结果失败的情况
    }

    /**
     * 网络请求失败的情况
     *
     * @param request    网络请求
     * @param statusCode http code
     */
    protected void onNetError(UrlRequest request, int statusCode) {
        //处理网络请求失败的情况
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz 目标页面
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz) {
        xStartActivity(clazz, (BasePageParam) null);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz  目标页面
     * @param bundle bundle数据
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, Bundle bundle) {
        xStartActivity(clazz, bundle, -1);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz       目标页面
     * @param bundle      bundle数据
     * @param requestCode requestCode
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, Bundle bundle, int requestCode) {
        if (isFinishing()) {
            return;
        }
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz     目标页面
     * @param pageParam 目标页面参数
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, BasePageParam pageParam) {
        xStartActivity(clazz, pageParam, -1);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz       目标页面
     * @param requestCode 目标页面参数
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, int requestCode) {
        xStartActivity(clazz, (BasePageParam) null, requestCode);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz       目标页面
     * @param pageParam   目标页面参数
     * @param requestCode requestCode
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, BasePageParam pageParam, int requestCode) {
        if (isFinishing()) {
            return;
        }
        Intent intent = new Intent(this, clazz);
        if (pageParam != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BasePageParam.PARAM_KEY, pageParam);
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到WebActivity
     *
     * @param url H5页面的URL
     */
    public void startWebActivity(String url) {
        startWebActivity(url, false);
    }

    /**
     * 跳转到WebActivity
     *
     * @param url     H5页面的URL
     * @param hasBack WebActivity是否有返回键
     */
    public void startWebActivity(String url, boolean hasBack) {
        CardWebActivity.WebPageParam pageParam = new CardWebActivity.WebPageParam();
        pageParam.url = url;
        pageParam.hasBack = hasBack;

        xStartActivity(CardWebActivity.class, pageParam);
    }

    protected void checkUserValid() {
        //判断是否登录，未登录跳转到登录界面
        if (!CardSessionManager.getInstance().isLogin()) {
            xStartActivity(LoginActivity.class, PHONE_CONFIRM_PAGE);
        }
    }

    /**
     * 判断请求结果是否合法
     *
     * @param request 请求结果
     * @return 如果合法，返回true，否则返回false
     */
    private boolean illegalResponse(UrlRequest request) {
        return (request != null);
    }

    /**
     * 判断两个URL是否为同一个请求，用来区分不同的网络请求
     *
     * @param service 后端服务
     * @param request 网络请求的urlRequest
     * @return 两次后端接口是否一致
     */
    protected boolean isSameUrl(IService service, UrlRequest request) {
        if (request == null || request.getNetworkParam() == null) {
            return false;
        }

        return request.getNetworkParam().service == service;
    }

    /**
     * 判断两个URL是否为同一个请求，用来区分不同的网络请求
     *
     * @param url     后端服务接口
     * @param request 网络请求的urlRequest
     * @return 两个URL是否一致
     */
    protected boolean isSameUrl(String url, UrlRequest request) {
        if (request == null || request.getNetworkParam() == null || LangUtils.isEmpty(url)) {
            return false;
        }

        return url.equals(request.getNetworkParam().serverUri);
    }

    /**
     * 清空该fragment发出的网络请求
     */
    protected void removeAllRequest() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        boolean flag = false;
        if (!LangUtils.isEmpty(fragments)) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    if (baseFragment.isAdded() && baseFragment.isVisible() && baseFragment.onBackPressed()) {
                        flag = true;
                    }
                }
            }
        }

        if (!flag) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CardActivityManager.onDestroyed(this);
        dismissLoadingDialog();
        removeAllRequest();
    }
}
