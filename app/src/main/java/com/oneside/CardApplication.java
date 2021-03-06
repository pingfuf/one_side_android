package com.oneside;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.facebook.react.*;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.oneside.base.CardConfig;
import com.oneside.base.rn.NavigatorPackage;
import com.oneside.base.rn.RNConfig;
import com.oneside.base.rn.lib.RCTSwipeRefreshLayoutPackage;
import com.oneside.manager.CardManager;
import com.oneside.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

/**
 * Created by Guo Ming on 3/31/15.
 */
public class CardApplication extends Application implements ReactApplication {
    public static CardApplication application;
    public static boolean isInit = false;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //在使用SDK各组间之前初始化context 信息，传入 ApplicationContext
        application = this;

        String processName = getProcessName(this, android.os.Process.myPid());
        String packageName = getApplicationContext().getPackageName();

        long startTime = System.currentTimeMillis();

        if (packageName != null && packageName.equals(processName)) {
            LogUtils.d("CardApplication ... initialize manager");
            //app有多个进程运行，只有在主线程所在的进程初始化Application的时候初始化SDK
            CardManager.initInMainThread(this);

            initReactJsBundleFile();
        }
        isInit = true;
        LogUtils.d("CardApplication start time = %s", System.currentTimeMillis() - startTime);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static CardApplication getApplication() {
        return application;
    }

    /**
     * 得到进程名称，一些特殊的进程可能得不到进程名称，可能返回null
     *
     * @param cxt 进程的context
     * @param pid 进程id
     * @return 进程名称
     */
    private String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningApps) {
                if (processInfo.pid == pid) {
                    return processInfo.processName;
                }
            }
        }

        return null;
    }

    private void initReactJsBundleFile() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/card";
        File file = new File(path);
        if (!file.exists()) {
            boolean flag = file.mkdir();
            if (!flag) {
                try {
                    throw new Throwable("no sdcard");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return new ReactNativeHost(this) {
            @Override
            public boolean getUseDeveloperSupport() {
                return true;
            }

            @Override
            public List<ReactPackage> getPackages() {
                List<ReactPackage> packages = new ArrayList<>();
                packages.add(new MainReactPackage());
                packages.add(new NavigatorPackage());
                packages.add(new RCTSwipeRefreshLayoutPackage());

                return packages;
            }

            @Nullable
            @Override
            public String getJSBundleFile() {
                return RNConfig.getJSBundleFile();
            }
        };
    }
}
