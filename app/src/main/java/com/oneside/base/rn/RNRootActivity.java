package com.oneside.base.rn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.oneside.base.BaseActivity;
import com.oneside.base.CardConfig;

import javax.annotation.Nullable;

public class RNRootActivity extends ReactActivity {
    private static final String RN_SERVER = "http://localhost:8081/index.android.bundle?platform=android";
    private ReactInstanceManager mReactInstanceManager;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, RNRootActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mReactInstanceManager = getReactInstanceManager();

        //mReactInstanceManager.getDevSupportManager().reloadJSFromServer(RN_SERVER);
    }

    @Nullable
    @Override
    protected String getMainComponentName() {
        return "RNOneside";
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
    protected void onDestroy() {
        super.onDestroy();

        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostDestroy(this);
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
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }
}
