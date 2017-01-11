package com.oneside.hy;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by fupingfu on 2017/1/6.
 */

public class HyChromeClient extends WebChromeClient {
    private WebView mWebView;

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }
}
