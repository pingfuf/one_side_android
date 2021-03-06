package com.oneside.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.oneside.R;
import com.oneside.utils.ViewUtils;
import com.oneside.zxing.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 */
public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;
    private int ScreenRate;
    private static final int CORNER_WIDTH = 10;
    private static final int MIDDLE_LINE_WIDTH = ViewUtils.rp(6);
    private static final int MIDDLE_LINE_PADDING = 5;
    private static final int SPEEN_DISTANCE = 5;
    private static float density;
    private static final int TEXT_SIZE = 16;
    private static final int TEXT_PADDING_TOP = 30;
    private Paint paint;
    private int slideTop;
    @SuppressWarnings("unused")
    private int slideBottom;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;

    private final int resultPointColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    boolean isFirst;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        density = context.getResources().getDisplayMetrics().density;

        ScreenRate = (int) (20 * density);

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);

        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }

        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top;
            slideBottom = frame.bottom;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setColor(resultBitmap != null ? resultColor : maskColor);

        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);


        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            paint.setColor(Color.parseColor("#ffff510d"));
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate, frame.top + CORNER_WIDTH,
                    paint);
            canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top + ScreenRate,
                    paint);
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right, frame.top + CORNER_WIDTH,
                    paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top + ScreenRate,
                    paint);
            canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left + ScreenRate,
                    frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - ScreenRate, frame.left + CORNER_WIDTH,
                    frame.bottom, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH, frame.right,
                    frame.bottom, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate, frame.right,
                    frame.bottom, paint);

            slideTop += SPEEN_DISTANCE;
            if (slideTop >= frame.bottom) {
                slideTop = frame.top;
            }

            paint.setAntiAlias(true);
//      paint.setAlpha(0x70);
//      LinearGradient lg =
//          new LinearGradient(frame.centerX(), 0, frame.centerX(),
//              MIDDLE_LINE_WIDTH / 2, Color.WHITE, Color.parseColor("#ffff510d"), Shader.TileMode.MIRROR);
//      paint.setShader(lg);
            // canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH / 2,
            // frame.right - MIDDLE_LINE_PADDING, slideTop + MIDDLE_LINE_WIDTH / 2, paint);
            canvas.drawOval(new RectF(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH / 2,
                    frame.right - MIDDLE_LINE_PADDING, slideTop + MIDDLE_LINE_WIDTH / 2), paint);

//      paint.setAlpha(0x10);
//      paint.setColor(Color.WHITE);
//      canvas.drawOval(new RectF(frame.left + MIDDLE_LINE_PADDING * 16, slideTop - MIDDLE_LINE_WIDTH / 12,
//          frame.right - MIDDLE_LINE_PADDING * 16, slideTop + MIDDLE_LINE_WIDTH / 12), paint);

            // Rect lineRect = new Rect();
            // lineRect.left = frame.left;
            // lineRect.right = frame.right;
            // lineRect.top = slideTop;
            // lineRect.bottom = slideTop + ViewUtils.rp(18);
            // canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.drawable.qrcode_scan_line)))
            // .getBitmap(), null, lineRect, paint);

            paint.setShader(null);
            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE * density);
            paint.setAlpha(0x90);
            paint.setTypeface(Typeface.create("System", Typeface.BOLD));

            String scanText = getResources().getString(R.string.scan_text);


            Paint.FontMetrics metrics = paint.getFontMetrics();
            int top = (int) Math.ceil(metrics.descent - metrics.top) + 2;

//            canvas.drawRect(frame.centerX() - paint.measureText(scanText) / 2 - ViewUtils.rp(15), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density) - top,
//                    frame.centerX() - paint.measureText(scanText) / 2 + paint.measureText(scanText) + ViewUtils.rp(15), (float) (frame.bottom + (float) TEXT_PADDING_TOP * density) + ViewUtils.rp(10),
//                    new Paint(Color.BLACK));

            RectF rectF = new RectF();
            rectF.left = frame.centerX() - paint.measureText(scanText) / 2 - ViewUtils.rp(15);
            rectF.top = (float) (frame.bottom + (float) TEXT_PADDING_TOP * density) - top;
            rectF.right = frame.centerX() - paint.measureText(scanText) / 2 + paint.measureText(scanText) + ViewUtils.rp(15);
            rectF.bottom = (float) (frame.bottom + (float) TEXT_PADDING_TOP * density) + ViewUtils.rp(10);
            float corner = (top + ViewUtils.rp(10)) / 2;
            Paint textPaint = new Paint(getResources().getColor(R.color.alpha_80_percent_black));
            textPaint.setAntiAlias(true);
            canvas.drawRoundRect(rectF, corner, corner, textPaint);
            canvas.drawText(scanText, frame.centerX() - paint.measureText(scanText) / 2,
                    (float) (frame.bottom + (float) TEXT_PADDING_TOP * density), paint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
                }
            }


            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);

        }
    }

    private void drawTextBackGround() {

    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
