package com.kuaipao.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kuaipao.base.inject.InjectUtils;
import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.base.net.IService;
import com.kuaipao.base.net.RequestDelegate;
import com.kuaipao.base.net.XRequest;
import com.kuaipao.base.net.XResponse;
import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.manager.CardSessionManager;
import com.kuaipao.ui.LoginActivity;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.base.net.UrlRequest;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 所有Fragment的基类，处理Fragment的公共方法
 *
 * Created by ZhanTao on 2/2/16.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener, RequestDelegate {
    protected static final int RESULT_OK = Activity.RESULT_OK;
    protected static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    //登陆失效code
    protected static final int LOGIN_TOKEN_EXPIRED_CODE = 1001;

    //Fragment的根View
    protected ViewGroup mRootView;

    protected Field mPageParamField;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化Fragment的View
        XAnnotation annotation = getClass().getAnnotation(XAnnotation.class);
        int layoutId = annotation.layoutId();
        try {
            mRootView = (ViewGroup) inflater.inflate(layoutId, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化Fragment的参数
        if(savedInstanceState != null) {
            initPageParam(savedInstanceState);
        } else {
            initPageParam(getArguments());
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //解决OverDraw
        getActivity().getWindow().setBackgroundDrawable(null);

        //添加注解
        InjectUtils.autoInject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null && getPageParam() != null) {
            outState.putSerializable(BasePageParam.PARAM_KEY, getPageParam());
        }
    }

    /**
     * 为Fragment添加参数
     *
     * @param param Fragment需要的参数
     */
    public void setFragmentParam(BasePageParam param) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BasePageParam.PARAM_KEY, param);
        setArguments(bundle);
    }

    /**
     * 初始化页面参数
     *
     * @param bundle bundle
     */
    protected void initPageParam(Bundle bundle) {
        Field[] paramFields = getClass().getDeclaredFields();
        for(Field field : paramFields) {
            if(field.isAnnotationPresent(XAnnotation.class)) {
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
                    } catch (java.lang.InstantiationException e) {
                        LogUtils.e("init page param failed");
                    }
                }
            }
        }
    }

    private Serializable getSerializableParamFromBundle(Bundle bundle) {
        Serializable pageParamSerializable = null;
        if(bundle != null) {
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
        if(mPageParamField == null) {
            Field[] paramFields = getClass().getDeclaredFields();
            for(Field field : paramFields) {
                if(field.isAnnotationPresent(XAnnotation.class)) {
                    mPageParamField = field;
                    break;
                }
            }
        }

        if(mPageParamField != null) {
            mPageParamField.setAccessible(true);
            try {
                Object obj = mPageParamField.get(this);
                if(obj != null && obj instanceof BasePageParam) {
                    pageParam = (BasePageParam) obj;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return pageParam;
    }

    /**
     * 判断当前的Activity是否有效
     *
     * @return 如果当前的Activity已经被回收。返回false，否则返回true
     */
    protected boolean isValidFragment() {
        //判断当前的Fragment是否已经移除
        if(isRemoving()) {
            return false;
        }

        //判断Fragment所在的Activity是否被回收
        Activity act = getActivity();
        return act != null && !act.isFinishing();
    }

    /**
     * 发送HTTP请求，返回结果在requestFinished、requestFailed中处理
     *
     * @param service    后端服务
     */
    protected void startRequest(IService service) {
        startRequest(service, null);
    }

    /**
     * 发送HTTP请求，返回结果在requestFinished、requestFailed中处理
     *
     * @param service    后端服务
     * @param param      HTTP请求参数
     */
    protected void startRequest(IService service, BaseRequestParam param) {
        startRequest(service, param, 0);
    }

    /**
     * 发送请求
     *
     * @param service   后端服务
     * @param param     HTTP请求参数
     * @param requestNo http请求序号
     */
    protected void startRequest(IService service, BaseRequestParam param, int requestNo){
        startRequest(service, param, requestNo, getClass().getSimpleName());
    }

    /**
     * 发送请求
     *
     * @param service     后端服务
     * @param param       HTTP请求参数
     * @param requestNo   http请求序号
     * @param requestTag  http请求tag标识
     */
    protected void startRequest(IService service, BaseRequestParam param, int requestNo, String requestTag){
        if(service == null || !isValidFragment()) {
            return;
        }

        XRequest.startRequest(this, service, param, requestNo, requestTag);
    }

    @Override
    public void onClick(View v) {
        //点击事件相应的时候，应该判断当前页面是否有效
        if(!isValidFragment() || v == null) {
            return;
        }
    }

    /**
     * 网络请求正常返回的回调
     *
     * @param request 请求内容
     */
    @Override
    public void requestFinished(final UrlRequest request) {
        //判断当前请求结果是否需要处理：fragment必须还存在，必须是有效的返回结果
        if(!isValidFragment() || !illegalResponse(request)) {
            return;
        }

        long time = System.currentTimeMillis();
        final BaseResult result = XResponse.parseResponse(request);
        if(result == null) {
            return;
        }

        LogUtils.e("parseJsonTime = %s", System.currentTimeMillis() - time);
        //网络请求返回并不在主线程中，需要特殊处理
        ViewUtils.runInHandlerThread(new Runnable() {
            @Override
            public void run() {
                onResponseSuccess(request, result);
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
        //判断当前请求结果是否需要处理：fragment必须还存在，必须是有效的返回结果
        if(!isValidFragment()) {
            return;
        }

        //网络请求并不在主线程中，需要特殊处理
        ViewUtils.runInHandlerThread(new Runnable() {
            @Override
            public void run() {
                if(statusCode == LOGIN_TOKEN_EXPIRED_CODE) {
                    ViewUtils.showToast(message, Toast.LENGTH_LONG);
                    CardSessionManager.getInstance().clearSession();
                    xStartActivity(LoginActivity.class, LoginActivity.LOGIN_PAGE_CODE);
                    return;
                }
                if(message == null) {
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
     * @param result 返回结果数据
     */
    protected void onResponseSuccess(UrlRequest request, BaseResult result) {
        //处理网络请求成功结果
    }

    /**
     * 网络请求成功，但是返回结果不符合要求
     *
     * @param request   网络请求
     * @param code      错误信息
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
     * @param clazz        目标页面
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
        xStartActivity(clazz, bundle, 0);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz       目标页面
     * @param bundle      bundle数据
     * @param requestCode requestCode
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, Bundle bundle, int requestCode) {
        if (!isValidFragment()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz        目标页面
     * @param pageParam    目标页面参数
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, BasePageParam pageParam) {
        xStartActivity(clazz, pageParam, 0);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz        目标页面
     * @param requestCode    目标页面参数
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, int requestCode) {
        xStartActivity(clazz, (BasePageParam) null, requestCode);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz        目标页面
     * @param pageParam    目标页面参数
     * @param requestCode  requestCode
     */
    public void xStartActivity(Class<? extends BaseActivity> clazz, BasePageParam pageParam, int requestCode) {
        if(!isValidFragment()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clazz);
        if(pageParam != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BasePageParam.PARAM_KEY, pageParam);
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
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
     * @param service    后端服务
     * @param request    网络请求的urlRequest
     *
     * @return 两个URL是否一致
     */
    protected boolean isSameUrl(IService service, UrlRequest request) {
        if(request == null || request.getNetworkParam() == null) {
            return false;
        }

        return request.getNetworkParam().service == service;
    }

    /**
     * 判断两个URL是否为同一个请求，用来区分不同的网络请求
     *
     * @param url      后端服务接口
     * @param request  网络请求的urlRequest
     *
     * @return 两个URL是否一致
     */
    protected boolean isSameUrl(String url, UrlRequest request) {
        if(request == null || request.getNetworkParam() == null || LangUtils.isEmpty(url)) {
            return false;
        }

        return url.equals(request.getNetworkParam().serverUri);
    }

    /**
     * 清空该fragment发出的网络请求
     */
    protected void removeAllRequest() {

    }

    /**
     * 返回事件，当Fragment需要处理返回事件的时候，在这里处理。
     * 返回true：则activity不会响应返回事件，由该fragment处理返回事件
     * 返回false：fragment会执行onBackPressed方法，但是不会阻拦activity的返回事件
     *
     * @return 是否拦截activity的返回事件
     */
    protected boolean onBackPressed () {
        LogUtils.e(getClass().getSimpleName() + " onBackPressed");
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeAllRequest();
    }
}
