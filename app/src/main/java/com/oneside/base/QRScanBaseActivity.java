package com.oneside.base;

import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.oneside.zxing.view.ViewfinderView;

/**
 * Created by MVEN on 16/3/18.
 */
public abstract class QRScanBaseActivity extends BaseActivity {

    public ViewfinderView getViewfinderView() {
        return null;
    }

    public Handler getHandler() {
        return null;
    }

    public void drawViewfinder() {
    }

    public void handleDecode(Result result, Bitmap barcode) {
    }
}
