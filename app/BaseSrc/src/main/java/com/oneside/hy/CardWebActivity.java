package com.oneside.hy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.BaseActivity;
import com.oneside.manager.CardActivityManager;
import com.oneside.utils.LogUtils;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;

import static com.oneside.utils.LangUtils.isNotEmpty;

public class CardWebActivity extends BaseActivity {
    WebView mWebView;

    @XAnnotation
    WebPageParam mPageParam;

    private HyJavascriptClient mJavascriptClient;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mWebView = new WebView(this);
        setContentView(mWebView);
        initWebView();

        CookieManager cookieManager = CookieManager.getInstance();
    }

    @Override
    protected void onPause() {
        dismissLoadingDialog();
        super.onPause();
    }

    @Override
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
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebChromeClient(new HyChromeClient());

        initJavaScriptClient();
        mWebView.addJavascriptInterface(mJavascriptClient, "hyScript");

        if (isNotEmpty(mPageParam.url)) {
            String action = "action=" + mPageParam.action;
            byte[] data = null;
            try {
                data = action.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(data != null) {
                LogUtils.e(action);
                mWebView.postUrl(mPageParam.url, data);
            } else {
                mWebView.loadUrl(mPageParam.url);
            }
        }
    }

    private void initJavaScriptClient() {
        if(mJavascriptClient == null) {
            mJavascriptClient = new HyJavascriptClient(this);
        }
    }

    private boolean syncCookie(String url, String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);//如果没有特殊需求
        String newCookie = cookieManager.getCookie(url);

        return !TextUtils.isEmpty(newCookie);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isPageFinished = false;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals("oneside")) {

                return true;
            }

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(!isPageFinished) {
                showLoadingDialog();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            if(isPageFinished) {
//                return;
//            }
            dismissLoadingDialog();
            setTitle(mWebView.getTitle(), true);
            isPageFinished = true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            dismissLoadingDialog();
        }
    }

    /**
     * Web界面需要的参数
     */
    public static class WebPageParam extends BasePageParam {
        public String url;
        public String action;
        public boolean hasBack;
    }
}
