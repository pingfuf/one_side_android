package com.oneside.base.net;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView.ScaleType;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

public class ImageRequestPriority extends ImageRequest {

    private Priority mPriority = Priority.HIGH;// IMMEDIATE;

    public ImageRequestPriority(String url, Response.Listener<Bitmap> listener, int maxWidth,
                                int maxHeight, ScaleType scaleType, Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

}
