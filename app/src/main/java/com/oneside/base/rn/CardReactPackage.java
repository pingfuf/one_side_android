package com.oneside.base.rn;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.ViewManager;
import com.oneside.base.rn.lib.RCTLazyLoadViewManager;
import com.oneside.base.rn.lib.RCTSwipeRefreshLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义React模块管理
 *
 * Created by fupingfu on 2017/5/5.
 */
public class CardReactPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> list = new ArrayList<>();
        list.add(new RCTNavigatorModule(reactContext));
        list.add(new RCTSwipeRefreshLayoutManager());
        list.add(new RCTLazyLoadViewManager());

        return list;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        List<ViewManager> list = new ArrayList<>();
        list.add(new ReactViewManager());

        return list;
    }
}