package com.oneside.hy;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.oneside.base.BaseActivity;
import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;

/**
 * H5和native交互的接口
 *
 * Created by fupingfu on 2017/1/6.
 */
public class HyJavascriptClient {
    private BaseActivity mActivity;

    public HyJavascriptClient() {

    }

    public HyJavascriptClient(BaseActivity context) {
        mActivity = context;
    }

    @JavascriptInterface
    public void test() {
        ViewUtils.showToast("temp", Toast.LENGTH_LONG);
    }

    @JavascriptInterface
    public void setTitle(String title) {
        LogUtils.e("js set title->" + title);
        if(mActivity != null) {
            mActivity.setTitle(title, true);
        }
    }

    @JavascriptInterface
    public void showLoadingDialog() {
        if(mActivity != null) {
            mActivity.showLoadingDialog();
        }
    }

    @JavascriptInterface
    public void dissmissLoadingDialog() {
        if(mActivity != null) {
            mActivity.dismissLoadingDialog();
        }
    }
}
