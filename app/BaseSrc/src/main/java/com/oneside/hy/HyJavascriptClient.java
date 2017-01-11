package com.oneside.hy;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;

/**
 * Created by fupingfu on 2017/1/6.
 */

public class HyJavascriptClient {
    private Activity mContext;

    public HyJavascriptClient() {

    }

    public HyJavascriptClient(Activity context) {
        mContext = context;
    }

    @JavascriptInterface
    public void test() {
        ViewUtils.showToast("temp", Toast.LENGTH_LONG);
    }
}
