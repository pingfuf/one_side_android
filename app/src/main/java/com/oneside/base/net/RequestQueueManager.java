package com.oneside.base.net;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.oneside.CardApplication;
import com.oneside.manager.CardCacheManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;

import java.util.Map;

/**
 * 网络请求队列管理工具类
 *
 */
public class RequestQueueManager {
    private static RequestQueue mQueue;
    private static ImageLoader mImageLoader;
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static String token;
    // private static final String SESSION_COOKIE = "session";

    private RequestQueueManager() {
        // no instances
    }

    public static void initialize(Context context) {
        if (mQueue == null) {
            HttpStack stack = new HurlStack();
            Network network = new BasicNetwork(stack);
            mQueue = new RequestQueue(new DiskBasedCache(CardCacheManager.getManager().getCacheDir()), network);
            mQueue.start();
        }

        if (mImageLoader == null) {
            int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
            // Use 1/8th of the available memory for this memory cache.
            int cacheSize = 1024 * 1024 * memClass / 16;
            mImageLoader = new ImageLoader(mQueue, new BitmapLruCache(cacheSize));
        }
    }

    public static RequestQueue getRequestQueue() {
        if (mQueue != null) {
            return mQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache} which effectively means
     * that no memory caching is used. This is useful for images that you know that will be show only
     * once.
     *
     * @return
     */
    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public synchronized static <T> void addRequest(Request<T> r) {
        addRequest(r, CardApplication.getApplication());
    }

    public synchronized static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        if (mQueue != null)
            mQueue.add(request);
    }

    public static void cancelAll() {
        cancelAll(CardApplication.getApplication());
    }

    public static void destroy() {
        cancelAll();
        token = null;
        mImageLoader = null;
    }

    public static void clearToken() {
        token = null;
    }

    public static void cancelAll(Object tag) {
        if (mQueue != null) {
            mQueue.cancelAll(tag);
            mQueue.stop();
            mQueue = null;
        }
    }

    /**
     * Checks the response headers for session cookie and saves it if it finds it.
     *
     * @param headers Response Headers.
     */
    public static void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY) && headers.get(SET_COOKIE_KEY).startsWith(CardSessionManager.TOKEN_KEY)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                token = cookie;
                LogUtils.d("checkSessionCookie, toke n= %s", token);
                CardSessionManager.getInstance().restoreSession(null, token);
            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     *
     * @param headers
     */
    public static void addSessionCookie(Map<String, String> headers) {
        if(LangUtils.isEmpty(token)) {
            token = CardSessionManager.getInstance().getToken();
        }

        LogUtils.i("addSessionCookie, token = %s", token);
        if (LangUtils.isNotEmpty(token)) {
            StringBuilder builder = new StringBuilder();
            builder.append(CardSessionManager.TOKEN_KEY);
            builder.append("=");
            builder.append(token);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }
}
