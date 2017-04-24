package com.oneside.base.rn;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import com.facebook.react.MemoryPressureRouter;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.DevSupportManager;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.react.uimanager.ViewManager;
import com.oneside.CardApplication;
import com.oneside.base.CardConfig;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by fupingfu on 2017/3/16.
 */

public class TReactInstanceManager {
    private static TReactInstanceManager instance;
    private ReactInstanceManager mReactInstanceManager;

    private TReactInstanceManager(DefaultHardwareBackBtnHandler handler) {
        mReactInstanceManager = ReactInstanceManager.builder()
                .addPackage(new MainReactPackage())
                .setApplication(CardApplication.getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .setDefaultHardwareBackBtnHandler(handler)
                .setJSBundleFile(getJSBundleFile())
                .setUseDeveloperSupport(CardConfig.isDevBuild())
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
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
}
