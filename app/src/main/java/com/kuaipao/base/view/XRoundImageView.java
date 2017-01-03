package com.kuaipao.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.kuaipao.utils.LogUtils;

/**
 * 圆形ImageView，可以通过网络URL设置背景
 * <p/>
 * Created by pingfu on 16-7-1.
 */
public class XRoundImageView extends XImageView {
    private boolean isGray;
    private Paint mBorderPaint;

    public XRoundImageView(Context context) {
        super(context);
        initBorderPaint();
    }

    public XRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBorderPaint();
    }

    public XRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBorderPaint();
    }

    private void initBorderPaint() {
        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if (null == b) {
            return;
        }

        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        int radius = getWidth() > getHeight() ? getHeight() : getWidth();
        int bitmapRadius = radius - 2 * mBorderSize;

        Bitmap roundBitmap = getCroppedBitmap(isGray ? toGrayScale(bitmap) : bitmap, bitmapRadius);
        canvas.drawCircle(getWidth() / 2,  getHeight() / 2, radius / 2, mBorderPaint);
        canvas.drawBitmap(roundBitmap, mBorderSize, mBorderSize, null);
    }

    public void setGray(boolean gray) {
        isGray = gray;
    }

    public void setBorder(int size, int color) {
        mBorderSize = size;
        mBorderColor = color;
        initBorderPaint();
    }

    public static Bitmap toGrayScale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayScale;
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
            output = Bitmap.createBitmap(outSize, outSize, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            try {
                output = Bitmap.createBitmap(outSize, outSize, Bitmap.Config.ARGB_4444);
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
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        boolean isWidthLarger = w > h;
        Rect srcRect = new Rect(isWidthLarger ? (oldW - size) / 2 : 0, isWidthLarger ? 0 : (oldH - size) / 2,
                isWidthLarger ? (oldW + size) / 2 : size, isWidthLarger ? size : (oldH + size) / 2);
        canvas.drawBitmap(bmp, srcRect, rect, paint);

        return output;
    }
}
