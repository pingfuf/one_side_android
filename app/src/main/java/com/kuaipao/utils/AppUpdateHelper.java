package com.kuaipao.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.RequestDelegate;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.manager.R;
import com.kuaipao.ui.CustomDialog;


import java.io.File;
import java.util.HashMap;

import static com.kuaipao.utils.ViewUtils.getString;
import static com.kuaipao.utils.ViewUtils.showToast;


public class AppUpdateHelper {

    private Activity mActivity;
    //    private UpdateResponse mUpdateResponse;
    // boolean isIgnored;
    private File mDownloadFile;

    /**
     * is auto checkout version
     */
    private volatile boolean mIsAuto = false;

    // private static AppUpdateHelper mAppUpdateHelper = null;

    public AppUpdateHelper(Activity activity) {
        mActivity = activity;
    }

    // public static AppUpdateHelper getInstance() {
    // if (mAppUpdateHelper == null) {
    // mAppUpdateHelper = new AppUpdateHelper();
    // }
    // return mAppUpdateHelper;
    // }


    public void checkUpdate(final boolean isAuto) {
        this.mIsAuto = isAuto;
//        UmengUpdateAgent.forceUpdate(CardManager.getApplicationContext());
        HashMap<String, Object> params = new HashMap<>();
        params.put("platform", "Android");
        UrlRequest r = new UrlRequest("/helper/upgrade", params);

        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String errorString) {
                if (!isAuto) {
                    ViewUtils.showToast(getString(R.string.no_network_warn), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void requestFinished(UrlRequest request) {
                JSONObject json = request.getResponseJsonObject();
                if (json != null && json.size() > 0) {
                    showDialog(json);
                }else{
                    if (!isAuto) {
                        ViewUtils.showToast(getString(R.string.no_network_warn), Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        r.start(true);
    }

    private void showDialog(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        String title = jsonObject.getString("title");
        String versionName = jsonObject.getString("version_name");
        String desc = jsonObject.getString("desc");
        final String url = jsonObject.getString("url");
        final int versionCode = jsonObject.getIntValue("version_code");
        boolean alert = jsonObject.getBooleanValue("alert");
        int currentVersionCode = SysUtils.APP_VERSION_CODE;

//        int ignoreVersionCode = LangUtils.parseInt(IOUtils.getPreferenceValue("upgrade_ignore_version_code"), 0);
        LogUtils.d(" current code  %s", currentVersionCode);
        if (((alert && mIsAuto) || (!mIsAuto)) && versionCode > currentVersionCode) {
            try {
                final CustomDialog.Builder builder = new CustomDialog.Builder(mActivity/*
                                                                 * CardManager.getApplicationContext(
                                                                 * )
                                                                 */);
//                String strTitle =
//                        String.format(getString(R.string.upgrade_vertion_title), mUpdateResponse.version);
                builder.setTitle(title);
                builder.setMessage(desc);
                builder.setMesgGravity(Gravity.CENTER_VERTICAL);
                builder.setCancelable(false);
                builder.setNegativeButton(getString(R.string.upgrade_negative_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mIsAuto){
                            IOUtils.savePreferenceValue("upgrade_ignore_version_code", String.valueOf(versionCode));
                        }
                    }
                });
                builder.setPositiveButton(getString(R.string.upgrade_positive_btn),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                performClickYes(url);
                            }
                        });
                ViewUtils.runInHandlerThread(new Runnable() {
                    @Override
                    public void run() {
                        builder.show();
                    }
                });

            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
        }else if(!mIsAuto && versionCode <= currentVersionCode){
            ViewUtils.showToast(getString(R.string.upgrade_tip_0), Toast.LENGTH_LONG);
        }


    }

    private void performClickYes(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        mActivity.startActivity(intent);
//        mDownloadFile =
//                UmengUpdateAgent.downloadedFile(mActivity/* CardManager.getApplicationContext() */,
//                        mUpdateResponse);
//        if (mDownloadFile == null) {
//            UmengUpdateAgent.startDownload(mActivity/* CardManager.getApplicationContext() */,
//                    mUpdateResponse);
//        } else {
//            UmengUpdateAgent.startInstall(mActivity/* CardManager.getApplicationContext() */,
//                    mDownloadFile);
//        }
    }
}
