package com.oneside.base.hy;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.oneside.base.BaseActivity;
import com.oneside.utils.ViewUtils;

/**
 * H5和native交互的接口
 *
 * Created by fupingfu on 2017/1/6.
 */
public class HyJavascriptClient {
    private BaseActivity mActivity;

    public HyJavascriptClient(BaseActivity context) {
        mActivity = context;
    }

    @JavascriptInterface
    public void test() {
        ViewUtils.showToast("temp", Toast.LENGTH_LONG);
    }

    @JavascriptInterface
    public void setTitle(final String title) {
        if(!validActivity()) {
            return;
        }

        ViewUtils.post(new Runnable() {
            @Override
            public void run() {
                mActivity.setTitle(title, true);
            }
        });
    }

    @JavascriptInterface
    public void showLoadingDialog() {
        if(!validActivity()) {
            return;
        }
        mActivity.showLoadingDialog();
    }

    @JavascriptInterface
    public void dismissLoadingDialog() {
        if(!validActivity()) {
            return;
        }
        mActivity.dismissLoadingDialog();
    }

    private boolean validActivity() {
        return mActivity != null && !mActivity.isFinishing();
    }
}
