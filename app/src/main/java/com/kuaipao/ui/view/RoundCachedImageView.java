package com.kuaipao.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.base.net.RequestQueueManager;

import java.io.File;

public class RoundCachedImageView extends NetworkImageView {

    public RoundCachedImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RoundCachedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundCachedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String url) {
        LogUtils.d("setImageurl = %s", url);
        tryLoadFromCacheFile(url);
        super.setImageUrl(url, RequestQueueManager.getImageLoader());
    }


    public void tryLoadFromCacheFile(String url) {
        try {
            if (LangUtils.isEmpty(url))
                return;

            String fileName = url.replace("/", "_").replace(":", "_")/*.substring(30) + ".jpg"*/;
            if (fileName.length() > 100) {
                fileName = fileName.substring(0, 100);
            }
            File fileRootPath = Environment.getExternalStorageDirectory()/*
                                                                    * CacheManager.getManager().
                                                                    * getCacheDir()
                                                                    */;
            File tmpRoot = new File(fileRootPath, "ppy_card_temp");
            if (!tmpRoot.exists()) {
                return;
            }

            File picFile = new File(tmpRoot, fileName);

            if (picFile.exists()) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(picFile.getAbsolutePath());
                    if (bitmap != null) {
                        setImageBitmap(bitmap);
                    }
                } catch (OutOfMemoryError e1) {
                }
            } else {
                LogUtils.d("#### !picFile.exists()");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @Override
//    public boolean doTryLoadingFromCacheFile() {
//        return true;
//    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();

        if (null == b) {
            return;
        }

        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        if (null == bitmap)
            return;

        int w = getWidth(), h = getHeight();


        Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
        if (roundBitmap == null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
            return;
        }

        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    /**
     * Could crop center square to circle
     * @param bmp
     * @param diameter
     * @return
     */
    public static Bitmap getCroppedBitmap(Bitmap bmp, int diameter) {
        if (bmp == null){
            return null;
        }
        int oldW = bmp.getWidth();
        int oldH = bmp.getHeight();
        int size = oldW > oldH ? oldH : oldW;
        float rate = diameter / (float)size;
        int w = (int) (rate * oldW);
        int h = (int) (rate * oldH);
        int outSize = diameter;
        Bitmap output = null;

        try {
            output = Bitmap.createBitmap(outSize, outSize, Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            try {
                output = Bitmap.createBitmap(outSize, outSize, Config.ARGB_4444);
            } catch (OutOfMemoryError e1) {
            }
        }
        if (output == null)
            return null;

        Canvas canvas = new Canvas(output);

        // final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, outSize, outSize);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawCircle(outSize / 2, outSize / 2,
                outSize / 2 - 1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        boolean isWidthLarger = w > h;
        Rect srcRect = new Rect(isWidthLarger ? (oldW - size) / 2 : 0, isWidthLarger ? 0 : (oldH - size) / 2,
                isWidthLarger ? (oldW + size) / 2 : size, isWidthLarger ? size : (oldH + size) / 2);
        canvas.drawBitmap(bmp, srcRect, rect, paint);

        return output;
    }

}
