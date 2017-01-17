package com.oneside.base.net;

/**
 * Created by pingfu on 16-10-25.
 */
public interface RequestDelegate {
    void requestFailed(UrlRequest request, int statusCode, String errorString);

    void requestFinished(UrlRequest request);
}
