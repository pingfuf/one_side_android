package com.oneside.base.net;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.oneside.base.net.model.NetworkParam;
import com.oneside.manager.CardManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.manager.CardSessionManager.NetworkStatus;
import com.oneside.model.event.NetWorkChangedEvent;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.utils.WebUtils;
import com.oneside.R;

import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.util.HashMap;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

import static com.oneside.utils.LogUtils.e;
import static com.oneside.utils.LogUtils.w;

/**
 * @author Bo Hu
 */
public class UrlRequest implements Response.Listener<String>, Response.ErrorListener {
    private static final int MY_SOCKET_TIMEOUT_MS = 10 * 1000;

    /**
     * 网络正确返回code
     */
    public static final int REQUEST_SUCCESS_CODE = 0;

    /**
     * 网络返回错误code
     */
    public static final int REQUEST_ERROR_CODE = -1;

    public static final String CODE = "errcode";

    public static final String MSG = "errmsg";

    private String mUrl;
    protected RequestDelegate delegateRef;
    private HashMap<String, String> mPostParams;
    private byte[] mPostBody;
    private int mMethod = Method.GET;

    private NetworkParam networkParam;

    //返回的JSON数据
    private String mJsonStr;

    //返回的JSON数据
    private JSONObject mJsonObj;
    private Bitmap mImgData;

    private int mMaxWidth = 0;
    private int mMaxHeight = 0;
    private Config mConfig = Config.ARGB_8888;
    private boolean mIsImg = false;

    public UrlRequest(@NonNull String url) {
        autoCompleteUrl(url, null);
    }

    /**
     * To add params of URL
     *
     * @param url
     * @param params
     */
    public UrlRequest(@NonNull String url, HashMap<String, Object> params) {
        autoCompleteUrl(url, params);
    }

    public void setUrl(String url, HashMap<String, Object> params) {
        autoCompleteUrl(url, params);
    }

    public void setDelegate(@CheckForNull RequestDelegate delegate) {
        delegateRef = delegate;
    }

    /**
     * Add post data into request
     *
     * @param key
     * @param value
     */
    public void addPostParam(String key, @CheckForNull Object value) {
        if (mPostParams == null) {
            mPostParams = new HashMap<String, String>();
        }
        mPostParams.put(key, String.valueOf(value));
    }

    public String getUrl() {
        return mUrl;
    }

    public RequestDelegate getDelegate() {
        return delegateRef;
    }

    /**
     * 返回网络请求的数据，JSON字符串类型
     *
     * @return 网络请求数据
     */
    public String getResponseStr() {
        return mJsonStr;
    }

    /**
     * 返回网络请求的数据，JSONObject类型
     *
     * @return 网络请求数据
     */
    public JSONObject getResponseJsonObject() {
        return mJsonObj;
    }

    public NetworkParam getNetworkParam() {
        return networkParam;
    }

    public void setNetworkParam(NetworkParam param) {
        networkParam = param;
    }

    /**
     * Get bitmap data
     *
     * @return
     */
    public Bitmap getImageData() {
        return mImgData;
    }

    /**
     * Config bitmap params
     *
     * @param maxWidth  Max width of the bitmap
     * @param maxHeight Max height of the bitmap
     * @param config    Config param
     */
    public void setImageParams(int maxWidth, int maxHeight, Config config) {
        mMaxWidth = maxWidth;
        mMaxHeight = maxHeight;
        mConfig = config;
        mIsImg = true;
    }

    /**
     * Start request as current type(Default is String, it would be Image type after invoked
     * setImageParams) and normal priority
     */
    public void start() {
        start(mIsImg, false);
    }

    /**
     * Start request as @param img type type and normal priority
     *
     * @param img Whether is bitmap
     */
    public void start(boolean img) {
        start(mIsImg, false);
    }

    /**
     * Start requestas as @param img type type and @param immediate priority
     *
     * @param img       true : Image type, false : String type
     * @param immediate true : High priority false : Normal priority
     */
    public void start(boolean img, boolean immediate) {
        Request<?> r;
        if (img) {
            r = new ImageRequestPriority(mUrl, new Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    mImgData = response;
                    fireDelegate(true, REQUEST_SUCCESS_CODE, null);
                }
            }, mMaxWidth, mMaxHeight, ScaleType.CENTER, mConfig, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    int code = error.networkResponse != null ? error.networkResponse.statusCode : -1;
                    fireDelegate(false, code, null);
                }
            });
            ((ImageRequestPriority) r).setPriority(immediate ? Priority.IMMEDIATE : Priority.NORMAL);
        } else {
            if (mPostParams != null && mPostParams.size() > 0) {
                mMethod = Method.POST;
            }
            if(networkParam != null && networkParam.service != null) {
                mMethod = networkParam.service.getRequestType();
            }

            StringRequestPriority sr = new StringRequestPriority(mMethod, mUrl, this, this);
            if (mMethod == Method.POST || mMethod == Method.PUT) {
                if (mPostBody != null) {
                    sr.setPostBody(mPostBody);
                }
                if (mPostParams != null && mPostParams.size() > 0) {
                    sr.setPostParams(mPostParams);
                }
            }
            sr.setShouldCache(false);
            sr.setPriority(immediate ? Priority.IMMEDIATE : Priority.NORMAL);
            r = sr;
        }
        r.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueManager.addRequest(r);
    }

    /**
     * Start request as current type(Default is String, it would be Image type after invoked
     * setImageParams) and High priority
     */
    public void startImmediate() {
        startImmediate(mIsImg);
    }

    /**
     * Start request as @param img type type and High priority
     *
     * @param img Whether is bitmap
     */
    public void startImmediate(boolean img) {
        start(img, true);
    }

    /**
     * 处理正常网络返回
     *
     * @param response 网络返回
     */
    @Override
    public void onResponse(String response) {
        mJsonStr = response;
        String errorString = CardManager.getApplicationContext().getString(R.string.no_network_warn);
        try {
            mJsonObj = JSON.parseObject(response);
            if (mJsonObj != null) {
                int code = WebUtils.getJsonInt(mJsonObj, CODE, REQUEST_SUCCESS_CODE);
                String errorMsg = WebUtils.getJsonString(mJsonObj, MSG, "");
                if (LangUtils.isNotEmpty(errorMsg)) {
                    errorString = errorMsg;
                }

                if (code == REQUEST_SUCCESS_CODE) {
                    fireDelegate(true, REQUEST_SUCCESS_CODE, null);
                } else {
                    fireDelegate(false, code, errorString);
                }
            } else {
                fireDelegate(false, REQUEST_ERROR_CODE, errorString);
            }
        } catch (Exception e) {
            if (LangUtils.isNotEmpty(response)) {
                fireDelegate(true, REQUEST_SUCCESS_CODE, null);
            } else {
                fireDelegate(false, REQUEST_ERROR_CODE, errorString);
            }
        }
    }

    /**
     * 处理异常网络返回
     *
     * @param error 网络返回结果
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        int code = error.networkResponse != null ? error.networkResponse.statusCode : -1;
        w("onErrorResponse %s", error);
        if (error instanceof NoConnectionError) {
            CardSessionManager.getInstance().setNetworkStatus(NetworkStatus.OffLine);
            EventBus.getDefault().post(new NetWorkChangedEvent(NetworkStatus.OffLine));
            CardSessionManager.getInstance().updateFromServer();
        }
        fireDelegate(false, code, null);
    }

    /**
     * Auto insert host into @param url if need and auto parse @param params to add into @param url
     *
     * @param url
     * @param params
     */
    private void autoCompleteUrl(String url, HashMap<String, Object> params) {
        URL u = WebUtils.createURL(params == null ? url : WebUtils.compositeUrl(url, params));
        if (u != null) {
            mUrl = u.toString();
        }
    }

    /**
     * 处理返回结果，返回结果有效，仅仅表示网络请求正常，不表示返回结果正确
     * 正确的返回结果的条件是：网络返回正常，切code = 0
     *
     * @param isValidResponse 是否是有效的返回
     * @param code            返回结果的code
     * @param errorString     返回结果的错误信息
     */
    private void fireDelegate(boolean isValidResponse, int code, String errorString) {
        RequestDelegate d = getDelegate();
        if (d != null) {
            if (isValidResponse) {
                d.requestFinished(this);
            } else {
                d.requestFailed(this, code, errorString);
            }
        }
    }
}