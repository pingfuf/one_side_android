package com.oneside.base.rn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.oneside.R;
import com.oneside.base.BaseFragment;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.net.UrlRequest;
import com.oneside.model.event.ReactModuleEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 默认的react fragment
 *
 * Created by fupingfu on 2017/8/24.
 */
@XAnnotation(layoutId = R.layout.fragment_rn_temp)
public class CardReactFragment extends BaseFragment {
    @From(R.id.rl_container)
    private RelativeLayout rlContainer;

    private CardReactActivityDelegate mDelegate;
    private boolean mReactModuleIsFinished = false;

    @XAnnotation
    private RNPageParam mParams;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate = new CardReactActivityDelegate(getActivity(), RNConfig.MAIN_COMPONENT_NAME);
        mDelegate.setPageParam(mParams);
        mDelegate.onCreate(savedInstanceState);

        initReactRootView();
    }

    private void initReactRootView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        rlContainer.addView(mDelegate.getReactRootView(), params);

        updateJsBundle();
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
        mDelegate.getReactInstanceManager()
                .getDevSupportManager()
                .reloadJSFromServer(RNConfig.getServerUrl());
    }

    /**
     * 下载文件，更新bundle
     */
    private void updateLocalBundle() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReactModuleFinished(ReactModuleEvent event) {
        mReactModuleIsFinished = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
        mReactModuleIsFinished = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
    }

    @Override
    protected boolean onBackPressed() {
        return !mReactModuleIsFinished && mDelegate.onBackPressed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
