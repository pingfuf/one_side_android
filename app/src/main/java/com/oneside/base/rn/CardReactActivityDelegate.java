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
import com.oneside.CardApplication;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

/**
 * Created by fupingfu on 2017/8/17.
 */
public class CardReactActivityDelegate extends ReactActivityDelegate {
    private WeakReference<Activity> mActivityRef;
    private RNPageParam mParams;
    private ReactRootView mReactRootView;

    public CardReactActivityDelegate(Activity activity, @Nullable String mainComponentName) {
        super(activity, mainComponentName);
        mActivityRef = new WeakReference<Activity>(activity);
    }

    public CardReactActivityDelegate(FragmentActivity fragmentActivity, @Nullable String mainComponentName) {
        super(fragmentActivity, mainComponentName);
        mActivityRef = new WeakReference<Activity>(fragmentActivity);
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
        mReactRootView = new ReactRootView(mActivityRef.get());

        return mReactRootView;
    }

    public ReactRootView getReactRootView() {
        return mReactRootView;
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return CardApplication.getApplication().getReactNativeHost();
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
        if (mParams != null) {
            LogUtils.i("react scheme = %s", mParams.scheme);
            LogUtils.i("react params = %s", mParams.buildScheme());
            bundle.putString("scheme", mParams.buildScheme());
        } else {
            bundle.putString("scheme", "rn://android/main");
        }

        return bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onBackPressed() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onBackPressed();
            return true;
        }
        return false;
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
