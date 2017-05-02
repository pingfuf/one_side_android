package com.oneside.base.rn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.oneside.R;
import com.oneside.base.BaseFragment;
import com.oneside.base.CardConfig;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.net.UrlRequest;
import com.oneside.utils.LangUtils;

import java.io.File;

/**
 * Created by fupingfu on 2017/4/26.
 */
@XAnnotation(layoutId = R.layout.fragment_rn_root)
public class RNRootFragment extends BaseFragment implements DefaultHardwareBackBtnHandler{
    @From(R.id.rn_root)
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    @XAnnotation
    private RNPageParam mParams;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initReactRootView();
    }

    private void initReactRootView() {
        ReactInstanceManager.Builder builder = ReactInstanceManager.builder()
                .addPackage(new MainReactPackage())
                .addPackage(new NavigatorPackage())
                .setApplication(getActivity().getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .setDefaultHardwareBackBtnHandler(this)
                .setUseDeveloperSupport(CardConfig.isDevBuild())
                .setInitialLifecycleState(LifecycleState.RESUMED);
        if (!RNConfig.ReactNativeShouldUpdate || !RNConfig.ReactNativeServerUsed) {
            builder.setJSBundleFile(getJSBundleFile());
        }
        mReactInstanceManager = builder.build();
        //ReactModule需要和index.android.js中的组件名称一致

        Bundle bundle = initRnParams();
        mReactRootView.startReactApplication(mReactInstanceManager, "RNOneside", bundle);

        updateJsBundle();
    }

    private Bundle initRnParams() {
        Bundle bundle = new Bundle();
        if (mParams != null && !LangUtils.isEmpty(mParams.scheme)) {
            bundle.putString("scheme", mParams.scheme);
        } else {
            bundle.putString("scheme", "rn://android/main");
        }

        if (mParams != null && mParams.params != null) {
            bundle.putString("param", JSON.toJSONString(mParams.params));
        }

        return bundle;
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

        if (!file.canRead() || RNConfig.ReactNativeShouldUpdate) {
            //如果文件不存在，仍然读取asset总得bundle
            path = "assets://index.android.js";
        }

        return path;
    }

    private void updateJsBundle() {
        if (RNConfig.ReactNativeShouldUpdate) {
            if (RNConfig.ReactNativeServerUsed) {
                updateServerBundle();
            } else {
                updateLocalBundle();
            }
        }
    }

    /**
     * 从react服务器更新bundle
     */
    private void updateServerBundle() {
        mReactInstanceManager.getDevSupportManager().reloadJSFromServer(RNConfig.getServerUrl());
    }

    /**
     * 下载文件，更新bundle
     */
    private void updateLocalBundle() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void invokeDefaultOnBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
