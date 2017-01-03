package com.kuaipao.ui.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.manager.CardActivityManager;
import com.kuaipao.utils.LogUtils;

import static com.kuaipao.utils.LangUtils.isNotEmpty;

public class CardWebActivity extends BaseActivity {
    WebView mWebView;

    @XAnnotation
    WebPageParam mPageParam;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mWebView = new WebView(this);
        setContentView(mWebView);
        initWebView();
    }

    @Override
    protected void onPause() {
        dismissLoadingDialog();
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.addJavascriptInterface(new XXKuaipaoJavaScript(), "xxkuaipaoclient");

        if (isNotEmpty(mPageParam.url)) {
            mWebView.loadUrl(mPageParam.url);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Activity a = CardActivityManager.getActiveActivity();
            if (a == null) {
                return true;
            }
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals("xxkuaipao")) {

                return true;
            }

            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showLoadingDialog();
        }

        public void onPageFinished(WebView view, String url) {
            dismissLoadingDialog();
            setTitle(mWebView.getTitle(), true);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
        }
    }

    private class XXKuaipaoJavaScript {
        @JavascriptInterface
        public void test() {
            LogUtils.e("wwww");
        }
    }

    /**
     * Web界面需要的参数
     */
    public static class WebPageParam extends BasePageParam {
        public String url;
        public boolean hasBack;
    }

}
