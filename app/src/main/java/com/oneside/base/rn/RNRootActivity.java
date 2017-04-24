package com.oneside.base.rn;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.cxxbridge.CatalystInstanceImpl;
import com.facebook.react.cxxbridge.JSBundleLoader;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.oneside.CardApplication;
import com.oneside.R;
import com.oneside.base.BaseActivity;
import com.oneside.base.CardConfig;
import com.oneside.base.inject.From;
import com.oneside.base.utils.BusinessStateHelper;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

public class RNRootActivity extends BaseActivity implements DefaultHardwareBackBtnHandler{
    private static final String RN_SERVER = "http://localhost:8081/index.android.bundle?platform=android";
    @From(R.id.rn_root)
    private ReactRootView mReactRootView;

    private BusinessStateHelper mStateHelper;
    private ReactInstanceManager mReactInstanceManager;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, RNRootActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_rn_root);

        if (Build.VERSION.SDK_INT >= 23) {
            // Get permission to show redbox in dev builds.
            if (!Settings.canDrawOverlays(this)) {
                Intent serviceIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(serviceIntent, 0);
            }
        }
        mStateHelper = BusinessStateHelper.build(this, mReactRootView);
        //mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);

        mReactInstanceManager = ReactInstanceManager.builder()
                .addPackage(new MainReactPackage())
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .setDefaultHardwareBackBtnHandler(this)
                .setJSBundleFile(getJSBundleFile())
                .setUseDeveloperSupport(CardConfig.isDevBuild())
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        //ReactModule需要和index.android.js中的组件名称一致
        mReactRootView.startReactApplication(mReactInstanceManager, "RNOneside", null);
        mReactRootView.setEventListener(new ReactRootView.ReactRootViewEventListener() {
            @Override
            public void onAttachedToReactInstance(ReactRootView rootView) {
               mStateHelper.setState(BusinessStateHelper.BusinessState.FINISHED);
            }
        });
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

        if (!file.canRead()) {
            //如果文件不存在，仍然读取asset总得bundle
            path = null;
        }

        return path;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostResume(this, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostPause(this);
        }
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
