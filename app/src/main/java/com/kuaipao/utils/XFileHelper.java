package com.kuaipao.utils;

import android.widget.ImageView;

import com.kuaipao.base.utils.XGlideUtils;
import com.kuaipao.utils.UploadPicFileUtils.OnUploadProcessListener;


import java.io.File;
import java.util.Map;

/**
 * Created by MVEN on 16/5/30.
 */
public class XFileHelper {
    private static int mCurrentCallTag = 433;

    public static void downLoadFileFormUrl(String cardImg, OnUploadProcessListener listener) {

    }

    public static void uploadFileFromDisk(File file, String fileKey, String url,
                                          Map<String, String> param, OnUploadProcessListener listener) {
        UploadPicFileUtils.uploadImage(file, fileKey, url, param, listener);
    }

    public static void uploadImageView() {

    }
}
