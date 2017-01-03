package com.kuaipao.ui.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.manager.R;
import com.kuaipao.ui.CustomDialog;
import com.kuaipao.ui.photopicker.ImageCaptureManager;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.UploadPicFileUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.utils.WebUtils;

import java.io.File;
import java.io.IOException;

public class AddPhysicalRecordMenuActivity extends BaseActivity {
    private static final int TAKE_PHOTO_PAGE = 902;
    @From(R.id.ll_photo)
    private LinearLayout llPhoto;

    @From(R.id.tv_photo)
    private TextView tvPhoto;

    @From(R.id.ll_detail)
    private LinearLayout llDetail;

    @From(R.id.tv_detail)
    private TextView tvDetail;

    @XAnnotation
    private CustomerPageParam mPageParam;

    private ImageCaptureManager captureManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_physical_record);
        setTitle("添加体测数据", true);

        initUI();
    }

    private void initUI() {
        captureManager = new ImageCaptureManager(this);
        tvPhoto.setText(isPhysicalPhotoExist() ? "已完成" : "未完成");
        tvDetail.setText(mPageParam.record == null ? "未完成" : "已完成");

        llPhoto.setOnClickListener(this);
        llDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == llPhoto) {
            if (isPhysicalPhotoExist()) {
                showChangePhotoDialog();
            } else {
                takePhoto();
            }

        } else if (v == llDetail) {
            xStartActivity(AddPhysicalRecordDetailActivity.class, mPageParam);
        }
    }

    /**
     * 更换体侧照片提示
     */
    private void showChangePhotoDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("图片已经上传，是否重新拍摄？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto();
            }
        });
        builder.create().show();
    }

    private boolean isPhysicalPhotoExist() {
        return mPageParam.record != null && LangUtils.isNotEmpty(mPageParam.record.reportUrl);
    }

    /**
     * 打开摄像头
     */
    private void takePhoto() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (captureManager == null || LangUtils.isEmpty(captureManager.getCurrentPhotoPath())) {
                ViewUtils.showToast(getResources().getString(R.string.take_photo_failed), Toast.LENGTH_SHORT);
                return;
            }

            final File file = new File(captureManager.getCurrentPhotoPath());
            LogUtils.d(">>>> onActivityResult() !file.exists() %s  length %s", file.exists(), file.length());
            if (!file.exists()) {
                return;
            }

            final UrlRequest request = new UrlRequest("upload/physical-exam");
            showLoadingDialog();
            new Thread() {
                @Override
                public void run() {
                    UploadPicFileUtils.uploadImageBase64(file, request.getUrl(), new UploadPicFileUtils.OnUploadProcessListener() {
                        @Override
                        public void onUploadDone(final int responseCode, final String message) {
                            dismissLoadingDialog();
                            LogUtils.e("message = %s", message);
                            if(responseCode == UploadPicFileUtils.UPLOAD_SUCCESS_CODE) {
                                ViewUtils.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        JSONObject object = WebUtils.parseJsonObject(message);
                                        String url = "";
                                        if(object != null) {
                                            JSONObject urls = WebUtils.getJsonObject(object, "urls");
                                            url = WebUtils.getJsonString(urls, "url");
                                            mPageParam.photoUrl = url;
                                            mPageParam.photoThumbUrl = WebUtils.getJsonString(urls, "thumb_url");
                                        }
                                        ViewUtils.showToast("上传成功", Toast.LENGTH_LONG);
                                        tvPhoto.setText("已完成");
                                        xStartActivity(AddPhysicalRecordDetailActivity.class, mPageParam);
                                    }
                                });
                            } else {
                                ViewUtils.showToast("上传失败", Toast.LENGTH_LONG);
                            }

                        }

                        @Override
                        public void onUploadProcess(int uploadSize) {

                        }

                        @Override
                        public void initUpload(int fileSize) {

                        }
                    });
                }
            }.start();
        }
    }
}
