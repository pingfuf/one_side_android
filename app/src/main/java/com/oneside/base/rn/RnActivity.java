package com.oneside.base.rn;


import android.os.Bundle;
import android.view.View;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;

import javax.annotation.Nullable;

public class RnActivity extends ReactActivity {
    private CardReactActivityDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mDelegate.getReactRootView());
    }

    @Nullable
    @Override
    protected String getMainComponentName() {
        return RNConfig.MAIN_COMPONENT_NAME;
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        mDelegate = new CardReactActivityDelegate(this, getMainComponentName());

        return mDelegate;
    }
}
