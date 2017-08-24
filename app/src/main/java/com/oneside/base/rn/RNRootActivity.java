package com.oneside.base.rn;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.oneside.R;
import com.oneside.base.BaseActivity;
import com.oneside.base.CardConfig;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.rn.lib.RCTSwipeRefreshLayoutPackage;
import com.oneside.base.utils.BusinessStateHelper;
import com.oneside.utils.LangUtils;

import java.io.File;

public class RNRootActivity extends BaseActivity implements DefaultHardwareBackBtnHandler{
    private static final int SET_PERMISSION_PAGE_CODE = 101;
    @From(R.id.rn_root)
    private ReactRootView mReactRootView;

    private BusinessStateHelper mStateHelper;
    private ReactInstanceManager mReactInstanceManager;

    @XAnnotation
    RNPageParam mParams;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_rn_root);
        setTitle("测试RN", true);
        if (Build.VERSION.SDK_INT >= 23) {
            // Get permission to show redbox in dev builds.
            if (!Settings.canDrawOverlays(this)) {
                Intent serviceIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(serviceIntent, SET_PERMISSION_PAGE_CODE);
            }
        }
        mStateHelper = BusinessStateHelper.build(this, mReactRootView);
        //mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        initReactRootView();

        mReactRootView.setEventListener(new ReactRootView.ReactRootViewEventListener() {
            @Override
            public void onAttachedToReactInstance(ReactRootView rootView) {
               mStateHelper.setState(BusinessStateHelper.BusinessState.FINISHED);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostResume(this, this);
        }
    }

    private void initReactRootView() {
        ReactInstanceManagerBuilder builder = ReactInstanceManager.builder()
                .addPackage(new MainReactPackage())
                .addPackage(new NavigatorPackage())
                .addPackage(new RCTSwipeRefreshLayoutPackage())
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .setDefaultHardwareBackBtnHandler(this)
                .setUseDeveloperSupport(CardConfig.isDevBuild())
                .setInitialLifecycleState(LifecycleState.RESUMED);
        if (!RNConfig.ReactNativeShouldUpdate || !RNConfig.ReactNativeServerUsed) {
            builder.setJSBundleFile(getJSBundleFile());
        }
        mReactInstanceManager = builder.build();
        //ReactModule需要和index.android.js中的组件名称一致

        Bundle bundle = initRnParams();
        mReactRootView.startReactApplication(mReactInstanceManager, "RNOneside", bundle);

        updateJsBundle();
    }

    private Bundle initRnParams() {
        Bundle bundle = new Bundle();
        if (mParams != null && !LangUtils.isEmpty(mParams.scheme)) {
            bundle.putString("scheme", mParams.scheme);
        } else {
            bundle.putString("scheme", "rn://android/main");
        }

        if (mParams != null && mParams.params != null) {
            bundle.putString("param", JSON.toJSONString(mParams.params));
        }

        return bundle;
    }

    /**
     * 读取bundle文件路径
     * 如果路径不存在，表明客户端不能使用react native模块，只能使用native
     *
     * @return bundle文件的路径
     */
    private String getJSBundleFile() {
        String path = Environment.getExternalStorageDirectory().getPath();
        path = path + "/card";
        path = path + "/index.android.bundle";
        File file = new File(path);

        if (!file.canRead() || RNConfig.ReactNativeShouldUpdate) {
            //如果文件不存在，仍然读取asset总得bundle
            path = "assets://index.android.js";
        }

        return path;
    }

    private void updateJsBundle() {
        if (RNConfig.ReactNativeShouldUpdate) {
            if (RNConfig.ReactNativeServerUsed) {
                updateServerBundle();
            } else {
                updateLocalBundle();
            }
        }
    }

    /**
     * 从react服务器更新bundle
     */
    private void updateServerBundle() {
        mReactInstanceManager.getDevSupportManager().reloadJSFromServer(RNConfig.getServerUrl());
    }

    /**
     * 下载文件，更新bundle
     */
    private void updateLocalBundle() {

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostPause(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if(mReactInstanceManager != null){
            mReactInstanceManager.onBackPressed();
        } else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostDestroy(this);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }
}
