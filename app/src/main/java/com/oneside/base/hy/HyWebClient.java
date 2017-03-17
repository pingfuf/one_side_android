package com.oneside.base.hy;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oneside.base.BaseActivity;

/**
 * Created by fupingfu on 2017/1/12.
 */

public class HyWebClient extends WebViewClient {
    private BaseActivity mActivity;
    private String mAction;
    private boolean isFinished;

    public HyWebClient(BaseActivity activity, String action) {
        mActivity = activity;
        mAction = action;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        LogUtils.e("shouldOverrideurlLoading");
//        Uri uri = Uri.parse(url);
//        if (uri.getScheme().equals("oneside")) {
//
//            return true;
//        }

        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mActivity.showLoadingDialog();
        isFinished = false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if(!isFinished) {
            view.loadUrl("javascript:initPage('" + mAction + "')");
            isFinished = true;
        }
        mActivity.setTitle(view.getTitle(), true);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        mActivity.dismissLoadingDialog();
    }
}
