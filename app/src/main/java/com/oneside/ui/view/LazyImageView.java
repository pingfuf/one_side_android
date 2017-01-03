package com.oneside.ui.view;


import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.oneside.utils.LogUtils;
import com.oneside.base.net.RequestQueueManager;

public class LazyImageView extends NetworkImageView {

    // private static final String PIC_URL = "/img/";

    public LazyImageView(Context context) {
        this(context, null);
    }

    public LazyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LazyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param key Key is pic key in PapayaCard server
     */
    public void setImageKey(String key) {
        // URL uri = WebUtils.createURL(PIC_URL + key);
        // if (uri != null) {
        // key = uri.toString();
        // }
        setImageUrl(key);
    }

//  public void setImageKey(String key, int defaultResID, int errorResID) {
//    // URL uri = WebUtils.createURL(PIC_URL + key);
//    // if (uri != null) {
//    // key = uri.toString();
//    // }
//    setImageUrl(key, defaultResID, errorResID);
//  }

    public void setImageUrl(String url) {
        LogUtils.d("setImageurl = %s", url);
        super.setImageUrl(url, RequestQueueManager.getImageLoader());
//    GlideUtils.loadImage(url, this);
    }

//  public void setImageUrl(String url, int defaultResID, int errorResID) {
//    LogUtils.d("setImageurl = %s", url);
//    super.setImageUrl(url, RequestQueueManager.getImageLoader());
////    GlideUtils.loadImage(url, this, defaultResID, errorResID);
//  }


}
