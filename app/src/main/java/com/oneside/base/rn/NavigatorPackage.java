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
 * 导航模块
 *
 * Created by fupingfu on 2017/5/5.
 */
public class NavigatorPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> list = new ArrayList<>();
        list.add(new NavigatorModule(reactContext));
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
        return  Collections.emptyList();
    }

    public static class NavigatorModule extends ReactContextBaseJavaModule {
        public NavigatorModule(ReactApplicationContext reactContext) {
            super(reactContext);
        }

        @Override
        public String getName() {
            return "NavigatorModule";
        }

        /**
         * 从JS页面跳转到原生activity 同时也可以从JS传递相关数据到原生
         *
         * @param name   需要打开的Activity的class
         * @param params
         */
        @ReactMethod
        public void startActivity(String name, String params) {
            try {
                Activity currentActivity = getCurrentActivity();
                if (null != currentActivity) {
                    Class toActivity = Class.forName(name);
                    Intent intent = new Intent(currentActivity, toActivity);
                    intent.putExtra("params", params);
                    currentActivity.startActivity(intent);
                }
            } catch (Exception e) {
                throw new JSApplicationIllegalArgumentException("不能打开Activity : " + e.getMessage());
            }
        }

        @ReactMethod
        public void startActivityForResult(String name, Callback success) {

        }
    }
}