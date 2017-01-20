package com.oneside;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.oneside.manager.CardManager;
import com.oneside.utils.LogUtils;

import java.util.List;

/**
 * Created by Guo Ming on 3/31/15.
 */
public class CardApplication extends Application {
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
}
