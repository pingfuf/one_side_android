package com.oneside.base.rn;

import android.os.Environment;

import com.oneside.base.CardConfig;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;

import java.io.File;

/**
 * Created by fupingfu on 2017/3/17.
 */

public class RNConfig {
    public static final String BUNDLE_NAME = "index.android.bundle";
    private static final String DEFAULT_URL = "localhost:8081";
    public static final String RN_SERVER = "RN_SERVER";
    public static final String MAIN_COMPONENT_NAME = "RNOneside";

    public static boolean ReactNativeShouldUpdate = false;
    public static boolean ReactNativeServerUsed = false;

    public static String getServerUrl() {
        String url = null;
        if (CardConfig.isDevBuild()) {
            url = IOUtils.getPreferenceValue(RN_SERVER);
        }

        if (LangUtils.isEmpty(url)) {
            url = DEFAULT_URL;
        }

        url = "http://" + url + "/" + BUNDLE_NAME;

        return url;
    }

    /**
     * 读取bundle文件路径
     * 如果路径不存在，表明客户端不能使用react native模块，只能使用native
     *
     * @return bundle文件的路径
     */
    public static String getJSBundleFile() {
        String path = Environment.getExternalStorageDirectory().getPath();
        path = path + "/card";
        path = path + "/index.android.bundle";
        File file = new File(path);

        if (!file.canRead() || RNConfig.ReactNativeShouldUpdate) {
            //如果文件不存在，仍然读取asset中的bundle
            path = "assets://index.android.js";
        }

        return path;
    }

    public static void updateReact() {

    }
}
