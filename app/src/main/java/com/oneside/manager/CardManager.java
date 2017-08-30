package com.oneside.manager;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.LongSparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.CardApplication;
import com.oneside.model.event.CourseDraftChangedEvent;
import com.oneside.model.response.CoachCourseDetailResponse;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.base.net.RequestQueueManager;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guo Ming on 3/31/15.
 */
public class CardManager {

//    public static CardDBManager cardDBManager;

    /***
     * Init all tools invoked in Main Thread
     *
     * @param context
     */
    public static void initInMainThread(Context context) {
        SysUtils.initialize(context);
        ViewUtils.initialize();
        initManager(context);
//        clearPreRes();
    }


    /**
     * can not initialize BaiduPush in oncreate of application !! you need to set your API KEY
     */
    private static void initManager(Context context) {

        RequestQueueManager.initialize(context);
    }


    /**
     * @return Application Context
     */
    public static Context getApplicationContext() {
        return CardApplication.getApplication();
    }

    /***
     * Invoked while app exited
     */
    public static void exit() {
        // RequestQueueManager.destroy();
        CardSessionManager.getInstance().setNetworkStatus(CardSessionManager.NetworkStatus.OnLine);
//        CardActivityManager.finishAllActivities();
    }

    public static void logout() {
        destroySession();
        // JumpCenter.Jump2Activity(a, PhoneConfirmActivity.class, -1, null);
    }

    /**
     * Invoked while logout
     */
    public static void destroySession() {
        clearPreference();
        RequestQueueManager.clearToken();
        // CardPushManager.getInstance().clearUserId(); // Clear Preference would clear push
        CardSessionManager.getInstance().clearSession();
//        if (cardDBManager != null) {
//            cardDBManager.clear();
//        }
//        cardDBManager = null;
    }

    public static void clearPreference() {
        ArrayList<String> keepKeys = new ArrayList<String>();
        IOUtils.clearPreference(keepKeys);
    }

    /**
     * Send event by umeng
     *
     * @param event
     */
    public static void logUmengEvent(String event) {
        if (LangUtils.isEmpty(event)) {
            return;
        }
        MobclickAgent.onEvent(getApplicationContext(), event);
    }


    public static void logUmengEvent(String event, String nValue) {
        if (LangUtils.isEmpty(event)) {
            return;
        }
        MobclickAgent.onEvent(getApplicationContext(), event, "" + nValue);
    }

    public static boolean isRunningApp() {
        boolean isRunning = false;
        ActivityManager am =
                (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)
                    && info.baseActivity.getPackageName().equals(packageName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static String getOfficialRootPath() {
        String rootPathName = "XiaoXiongoneside";

        File root = null;
        if (SysUtils.EXTERNAL_STORAGE_WRITABLE) {
            root = new File(Environment.getExternalStorageDirectory(), rootPathName);
            if (!root.exists())
                root.mkdirs();
        }
        if (root == null || !root.exists())
            return null;
        else
            return root.getPath();
    }

    public static File getOfficialRootFile() {
        String rootPathName = "XiaoXiongoneside";

        File root = null;
        if (SysUtils.EXTERNAL_STORAGE_WRITABLE) {
            root = new File(Environment.getExternalStorageDirectory(), rootPathName);
            if (!root.exists())
                root.mkdirs();
        }
        if (root == null || !root.exists())
            return null;
        else
            return root;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void clearPreRes() {
        Resources resources = getApplicationContext().getResources();
        try {
            Field field = Resources.class.getDeclaredField("sPreloadedDrawables");
            field.setAccessible(true);
            LongSparseArray<Drawable.ConstantState>[] sPreLoadedDrawables = (LongSparseArray<Drawable.ConstantState>[]) field.get(resources);
            LogUtils.e("wwwwwwww %s", sPreLoadedDrawables.length);
            for (LongSparseArray<Drawable.ConstantState> preLoaded : sPreLoadedDrawables) {

                preLoaded.clear();
            }
        } catch (NoSuchFieldException e) {
            LogUtils.e("清理系统资源  %s", e);
        } catch (IllegalAccessException e) {
            LogUtils.e("清理系统资源sPreLoadedDrawables  %s", e);
        }
    }

    public static void saveFailedDetail(CoachCourseDetailResponse model) {
        JSONObject json = (JSONObject) JSON.toJSON(model);
        LogUtils.d("xxxxxxx saveFailed %s", json);
        if (json != null) {
            IOUtils.savePreferenceValue("failed_course_detail", json.toString());
        }
        EventBus.getDefault().post(new CourseDraftChangedEvent());
    }

    public static void clearFailedDetail(){
        IOUtils.removePreferenceValue("failed_course_detail");
        EventBus.getDefault().post(new CourseDraftChangedEvent());
    }

    public static CoachCourseDetailResponse getFailedCourseDetail() {
        String text = IOUtils.getPreferenceValue("failed_course_detail");
        if (LangUtils.isNotEmpty(text)) {
            try {
                CoachCourseDetailResponse response = JSON.parseObject(text, CoachCourseDetailResponse.class);
                return response;
            }catch (JSONException e){

            }
        }
        return null;
    }
}
