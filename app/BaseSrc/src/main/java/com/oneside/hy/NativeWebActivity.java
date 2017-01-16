package com.oneside.hy;

import com.oneside.base.BaseActivity;
import com.oneside.R;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.utils.LogUtils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;

import static com.oneside.utils.LangUtils.isNotEmpty;

public class NativeWebActivity extends BaseActivity {
    WebView mWebView;

    @XAnnotation
    private WebPageParam mPageParam;

    private HyJavascriptClient mJavascriptClient;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        }
        mWebView.setWebViewClient(new HyWebClient(NativeWebActivity.this, mPageParam.action));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
