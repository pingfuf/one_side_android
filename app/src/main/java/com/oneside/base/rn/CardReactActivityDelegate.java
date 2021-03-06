package com.oneside.base.rn;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.oneside.utils.LangUtils;

import javax.annotation.Nullable;

/**
 * Created by fupingfu on 2017/8/17.
 */
public class CardReactActivityDelegate extends ReactActivityDelegate {
    private Activity mActivity;
    private RNPageParam mParams;
    private ReactRootView mReactRootView;

    public CardReactActivityDelegate(Activity activity, @Nullable String mainComponentName) {
        super(activity, mainComponentName);
        mActivity = activity;
    }

    public CardReactActivityDelegate(FragmentActivity fragmentActivity, @Nullable String mainComponentName) {
        super(fragmentActivity, mainComponentName);
        mActivity = fragmentActivity;
    }

    public void setPageParam(RNPageParam pageParam) {
        mParams = pageParam;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public ReactRootView createRootView() {
        mReactRootView = new ReactRootView(mActivity);

        return mReactRootView;
    }

    public ReactRootView getReactRootView() {
        return mReactRootView;
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (mActivity == null) {
            return null;
        }

        return ((ReactApplication) mActivity.getApplication()).getReactNativeHost();
    }

    @Override
    public ReactInstanceManager getReactInstanceManager() {
        return super.getReactInstanceManager();
    }

    @Override
    public void loadApp(String appKey) {
        if (mReactRootView != null) {
            throw new IllegalStateException("Cannot loadApp while app is already running.");
        }
        mReactRootView = createRootView();
        mReactRootView.startReactApplication(
                getReactNativeHost().getReactInstanceManager(),
                appKey,
                getLaunchOptions());
    }

    @Nullable
    @Override
    protected Bundle getLaunchOptions() {
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
