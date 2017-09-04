package com.oneside.base.rn;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.oneside.manager.CardManager;
import com.oneside.model.event.ReactModuleEvent;
import com.oneside.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 导航模块
 *
 * Created by fupingfu on 2017/8/25.
 */
public class RCTNavigatorModule extends ReactContextBaseJavaModule {
    private ReactContext mContext;

    public RCTNavigatorModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return "NavigatorModule";
    }

    /**
     * 从JS页面跳转到原生activity 同时也可以从JS传递相关数据到原生
     *
     * @param name   需要打开的Activity的class
     * @param params 参数
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

    /**
     * react模块结束时间
     */
    @ReactMethod
    public void onReactModuleFinished() {
        ReactModuleEvent event = new ReactModuleEvent();
        event.state = 1;
        EventBus.getDefault().post(event);
    }

    /**
     * 记录点击事件
     *
     * @param key    事件标识
     * @param params 事件参数
     */
    @ReactMethod
    public void onReactModuleClicked(String key, String... params) {
        ViewUtils.showToast(key, Toast.LENGTH_LONG);

        CardManager.logUmengEvent(key, params[0]);
    }
}