package com.oneside.base.rn;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.MemoryPressureRouter;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.DevSupportManager;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.uimanager.ViewManager;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by fupingfu on 2017/3/16.
 */

public class RNInstanceManager extends ReactInstanceManager {
    @Override
    public DevSupportManager getDevSupportManager() {
        return null;
    }

    @Override
    public MemoryPressureRouter getMemoryPressureRouter() {
        return null;
    }

    @Override
    public void createReactContextInBackground() {

    }

    @Override
    public boolean hasStartedCreatingInitialContext() {
        return false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostPause(Activity activity) {

    }

    @Override
    public void onHostResume(Activity activity, DefaultHardwareBackBtnHandler defaultBackButtonImpl) {

    }

    @Override
    public void onHostDestroy() {

    }

    @Override
    public void onHostDestroy(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void showDevOptionsDialog() {

    }

    @Override
    public String getSourceUrl() {
        return null;
    }

    @Nullable
    @Override
    public String getJSBundleFile() {
        return null;
    }

    @Override
    public void attachMeasuredRootView(ReactRootView rootView) {

    }

    @Override
    public void detachRootView(ReactRootView rootView) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public List<ViewManager> createAllViewManagers(ReactApplicationContext catalystApplicationContext) {
        return null;
    }

    @Override
    public void addReactInstanceEventListener(ReactInstanceEventListener listener) {

    }

    @Override
    public void removeReactInstanceEventListener(ReactInstanceEventListener listener) {

    }

    @Nullable
    @Override
    public ReactContext getCurrentReactContext() {
        return null;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return null;
    }
}
