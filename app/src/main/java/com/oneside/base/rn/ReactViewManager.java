package com.oneside.base.rn;

import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.image.ReactImageView;
import com.oneside.base.rn.views.RnImageView;

/**
 * Created by fupingfu on 2017/11/29.
 */

public class ReactViewManager extends SimpleViewManager<View> {
    @Override
    public String getName() {
        return null;
    }

    @Override
    protected View createViewInstance(ThemedReactContext reactContext) {
        return new RnImageView(reactContext);
    }
}
