package com.oneside.base.rn;

import com.facebook.react.shell.MainPackageConfig;
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

    public static boolean shouldUpdate = false;

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
}
