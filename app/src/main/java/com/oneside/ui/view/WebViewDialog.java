package com.oneside.ui.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;


public class WebViewDialog extends Dialog {

    private WebView mWebView;
    //	private ImageView cancelView;
    private double proportion = -1;

    public WebViewDialog(Context context) {
        super(context, R.style.MyWebDialog);
        initViews(context);

    }

    public void setLenWidthRatio(double proportion) {
        this.proportion = proportion;
    }

    public void setAdData(String url) {
        mWebView.loadUrl(url);
    }

    public void setLoadData(String data) {
        mWebView.getSettings().setDefaultTextEncodingName("UTF -8");
        mWebView.loadData(data, "text/html; charset=UTF-8", null);
    }

    public WebView getWebView() {
        return mWebView;
    }

    /***************
     * Private method
     ******************/
    private void initViews(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        RelativeLayout layout = new RelativeLayout(context);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.setBackgroundResource(R.drawable.shape_dialog_bg);
        setContentView(layout);

        mWebView = new WebView(context);
        params.width = ViewUtils.rp(280);
        if (proportion != -1) {
            params.height = (int) (params.width / proportion);
        } else
            params.height = (int) (SysUtils.HEIGHT * 0.7);
        mWebView.setLayoutParams(params);
        layout.addView(mWebView);
        mWebView.setVerticalScrollBarEnabled(false);
        layout.setBackgroundResource(R.drawable.shape_dialog_bg);
        layout.setVerticalScrollBarEnabled(false);
        initWebView();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    private ProgressDialog mProgressDialog;// show loading dialog

    public void showProgressDialog() {

//        mWebView.loadData();//加载网页
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(getContext(), "", "Loading...", true, true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        } else if (!mProgressDialog.isShowing()) {
            try {
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

		/*private class MyWebViewClient extends WebViewClient {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
				setTitle(url);
				return false;
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				showProgressDialog();
			}

			public void onPageFinished(WebView view, String url) {
				dismissProgressDialog();
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			}
		}*/

    private class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
        }
    }


}
