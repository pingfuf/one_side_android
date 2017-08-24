package com.oneside.base.rn;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
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

public class RnTempActivity extends BaseActivity implements DefaultHardwareBackBtnHandler{
    @From(R.id.rl_container)
    private RelativeLayout rlContainer;

    private BusinessStateHelper mStateHelper;
    private CardReactActivityDelegate mDelegate;

    @XAnnotation
    RNPageParam mParams;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_rn_root);
        mDelegate = new CardReactActivityDelegate(this, RNConfig.MAIN_COMPONENT_NAME);
        mDelegate.onCreate(bundle);

        addReactRootView();
    }

    private void addReactRootView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        rlContainer.addView(mDelegate.getReactRootView(), params);
        mStateHelper = BusinessStateHelper.build(this, mDelegate.getReactRootView());
        updateJsBundle();

        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        mDelegate.getReactRootView().setEventListener(new ReactRootView.ReactRootViewEventListener() {
            @Override
            public void onAttachedToReactInstance(ReactRootView rootView) {
                mStateHelper.setState(BusinessStateHelper.BusinessState.FINISHED);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDelegate.onResume();
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
        mDelegate.getReactInstanceManager().getDevSupportManager().reloadJSFromServer(RNConfig.getServerUrl());
    }

    /**
     * 下载文件，更新bundle
     */
    private void updateLocalBundle() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        mDelegate.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelegate.onDestroy();
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        mDelegate.onBackPressed();
    }
}
