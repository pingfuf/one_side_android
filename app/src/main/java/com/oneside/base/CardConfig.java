package com.oneside.base;

import android.util.Log;

import com.oneside.BuildConfig;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Guo Ming on 3/27/15.
 */
public class CardConfig {
    public static final String IS_MOCK = "IS_MOCK";
    public static final String SERVER_URL = "SERVER_URL";

    public static final boolean DEV_BUILD = BuildConfig.LOG_DEBUG;
    public static final int MIN_LOG_LEVEL = DEV_BUILD ? Log.DEBUG : Log.INFO;
    public static final boolean LOG_LINE_NUMBER = false;
    public static final String KEY = "8f00d7d21c6645719b4d4f47713b4030";

    public static final String DEFAULT_URL = BuildConfig.serverUrl;

    static {
        try {
            DEFAULT_WEB_URL = new URL(DEFAULT_URL);
        } catch (MalformedURLException e) {
            DEFAULT_WEB_URL = null;
            LogUtils.e("parse Default URL error %s", e);
        }
    }

    public static URL DEFAULT_WEB_URL;

    public static boolean isDevBuild() {
        return "debug".equals(BuildConfig.BUILD_TYPE) || "beta".equals(BuildConfig.BUILD_TYPE);
    }

    public static boolean isMock() {
        return "true".equals(IOUtils.getPreferenceValue(IS_MOCK));
    }

    public static String getMockServerUrl() {
        return IOUtils.getPreferenceValue(SERVER_URL);
    }

    /**
     * 获取WebURL
     *
     * @return
     */
    public static URL getDefaultWebUrl() {
        URL url = null;
        if(isDevBuild()) {
            boolean isMock = "true".equals(IOUtils.getPreferenceValue(IS_MOCK));
            String serverUrl = IOUtils.getPreferenceValue(SERVER_URL);
            if(isMock && !LangUtils.isEmpty(serverUrl)) {
                try {
                    url = new URL(serverUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                url = DEFAULT_WEB_URL;
            }
        } else {
            url = DEFAULT_WEB_URL;
        }

        return url;
    }
}