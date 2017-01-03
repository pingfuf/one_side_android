package com.oneside.utils;

import com.oneside.base.BaseActivity;
import com.oneside.ui.web.CardWebActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 页面跳转的工具类
 */
public class JumpCenter {
    /**
     * 页面跳转
     *
     * @param activity 当前页面
     * @param clazz    要跳转的页面
     */
    public static void Jump2Activity(Activity activity, Class<? extends BaseActivity> clazz) {
        Jump2Activity(activity, clazz, 0, null);
    }

    /**
     * 页面跳转
     *
     * @param activity    当前页面
     * @param clazz       要跳转的页面
     * @param requestCode onActivityResult中的requestCode
     */
    public static void Jump2Activity(Activity activity, Class<? extends BaseActivity> clazz, int requestCode) {
        Jump2Activity(activity, clazz, requestCode, null);
    }

    /**
     * 页面跳转
     *
     * @param activity    当前页面
     * @param clazz       要跳转的页面
     * @param requestCode onActivityResult中的requestCode
     * @param bundle      跳转页面时传递的数据
     */
    public static void Jump2Activity(Activity activity, Class<? extends BaseActivity> clazz,
                                     int requestCode, Bundle bundle) {
        Intent intent = new Intent();

        intent.setClass(activity, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        if (requestCode == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void JumpWebActivity(Context c, String url) {
        if (c == null || LangUtils.isEmpty(url)) {
            LogUtils.w("JumpWebActivity invalid params c = %s url = %s", c, url);
            return;
        }
        Intent intent = new Intent(c, CardWebActivity.class);
        intent.putExtra("url", url);
        if (!(c instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        c.startActivity(intent);
    }
}
