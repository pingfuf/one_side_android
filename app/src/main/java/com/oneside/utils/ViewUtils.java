package com.oneside.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.CardApplication;
import com.oneside.base.CardConfig;
import com.oneside.manager.CardManager;
import com.oneside.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

import static com.oneside.utils.LogUtils.D;
import static com.oneside.utils.LogUtils.d;
import static com.oneside.utils.LogUtils.e;
import static com.oneside.utils.LogUtils.i;
import static com.oneside.utils.LogUtils.w;

/**
 * Class to help to handle Views' logic.
 *
 * @author Bo Hu
 */
public class ViewUtils {
    public static long time = 0l;

    public static float DENSITY = 1.0f;
    public static final int VIEW_ID_OFFSET = 0x558886;

    @CheckForNull
    private static EnhancedHandler handler;

    private static WeakReference<Thread> thread_ref;
    private static boolean initialized = false;

    @CheckForNull
    private static File TMP_PHOTO = null;
    @CheckForNull
    public static Uri TMP_PHOTO_URI = null;

    public static final String IMAGE_UNSPECIFIED = "image/*";

    private static Typeface lightTypeface, mediumTypeface;

    /**
     * Initialize class.
     */
    public static void initialize() {
        if (!initialized) {
            handler = new EnhancedHandler();
            thread_ref = new WeakReference<Thread>(Thread.currentThread());
            lightTypeface = Typeface.DEFAULT;// Typeface.createFromAsset
            // (CardManager.getApplicationContext().getAssets()
            // , "Roboto-Light.ttf");
            mediumTypeface = Typeface.DEFAULT_BOLD;// Typeface.createFromAsset
            // (CardManager.getApplicationContext().getAssets()
            // , "Roboto-Medium.ttf");
            initialized = true;
        }
    }

    /**
     * Get roboto-light typeface
     */
    public static Typeface getLightTypeface() {
        return lightTypeface;
    }

    /**
     * Get roboto-medium typeface
     */
    public static Typeface getMediumTypeface() {
        return mediumTypeface;
    }

    /**
     * Clear class fields when this is called.
     */
    public static void destroy() {
        handler = null;
        initialized = false;
    }

    /**
     * Run in handler thread.
     *
     * @param runnable
     */
    public static void runInHandlerThread(Runnable runnable) {
        if (handler != null)
            handler.runInHandlerThread(runnable);
    }

    /**
     * Call a method or command in handler thread.
     *
     * @param <T>
     * @param callable
     * @param defaultValue
     * @return T
     */
    public static <T> T callInHandlerThread(Callable<T> callable, T defaultValue) {
        if (handler != null)
            return handler.callInHandlerThread(callable, defaultValue);
        return defaultValue;
    }

    /**
     * Post methods or commands in handler thread.
     *
     * @param <T>
     * @param callable
     * @return Future<T>
     */
    @CheckForNull
    public static <T> Future<T> postCallable(Callable<T> callable) {
        if (handler != null)
            return handler.postCallable(callable);
        return null;
    }

    /**
     * Post a command in handler thread with specific timespan.
     *
     * @param r
     * @param delayMillis
     */
    public static void postDelayed(Runnable r, long delayMillis) {
        if (handler != null)
            handler.postDelayed(r, delayMillis);
    }

    /**
     * @param r
     */
    public static void removeRunnable(Runnable r) {
        if (handler != null)
            handler.removeCallbacks(r);
    }

    /**
     * Post a command in handler thread.
     *
     * @param r
     */
    public static void post(Runnable r) {
        if (handler != null)
            handler.post(r);
    }

    /**
     * Check whether it's main thread.
     *
     * @return
     */
    public static boolean isMainThread() {
        if (!initialized)
            return true; // always return true if the app is not initialized
        // properly
        if (thread_ref != null)
            return thread_ref.get() == Thread.currentThread();
        return false;
    }

    /**
     * Assert that this is main thread. When it's not, throw a Exception.
     */
    public static void assertMainThread() {
        if (!isMainThread()) {
            e(new Exception(), "UI thread assertion failed");
            if (CardConfig.DEV_BUILD)
                throw new RuntimeException("UI thread assertion failed!");
        }
    }

    /**
     * Remove a view from viewgroup.
     *
     * @param o can be null
     */
    public static void removeFromSuperView(@CheckForNull Object o) {
        if (o == null)
            return;

        if (o instanceof View) {
            View view = (View) o;
            final ViewParent parent = view.getParent();
            if (parent == null)
                return;
            if (parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            } else
                w("the parent of view %s is not a viewgroup: %s", view, parent);
        } else if (o instanceof Dialog) {
            Dialog dialog = (Dialog) o;
            dialog.hide();
        }
    }

    /**
     * Add a view to viewgroup
     *
     * @param parent
     * @param view
     */
    public static void addView(ViewGroup parent, View view) {
        addView(parent, view, false);
    }

    /**
     * Add a view to viewgroup.
     *
     * @param parent
     * @param view
     * @param force
     */
    public static void addView(@CheckForNull ViewGroup parent, @CheckForNull View view, boolean force) {
        if (parent == null || view == null)
            return;

        try {
            if (view.getParent() == null)
                parent.addView(view);
            else if (force) {
                if (view.getParent() != parent) {
                    removeFromSuperView(view);
                    parent.addView(view);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e, "Failed to addView, parent %s, view %s, force %b, layoutParams %s", parent,
                    view, force, view.getLayoutParams());
        }
    }

    /**
     * Convert dip to pixels.
     *
     * @param dip
     * @return int
     */
    public static int rp(int dip) {
        return rp(CardManager.getApplicationContext(), dip);
    }

    /**
     * Convert Dip to pixels.
     *
     * @param c
     * @param dip
     * @return int
     */
    public static int rp(@CheckForNull Context c, int dip) {
        if (c == null) {
            e("context is null for rp");
            return dip;
        }
        return dip > 0 ? (int) (c.getResources().getDisplayMetrics().density * dip + 0.5f) : dip;
    }

    public static float pr(float pix) {
        return pr(CardApplication.getApplication(), pix);
    }

    public static float pr(@CheckForNull Context c, float pix) {
        if (c == null) {
            e("context is null for rp");
            return pix;
        }

        return pix > 0 ? (pix / c.getResources().getDisplayMetrics().density + 0.5f) : pix;
    }

    /**
     * Get orientation of a view.
     *
     * @param context
     * @return int
     */
    public static int getOrientation(@CheckForNull Context context) {
    /*
     * Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE
     * )).getDefaultDisplay(); int orientation = display.getOrientation(); if (orientation ==
     * ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) return Configuration.ORIENTATION_LANDSCAPE; else
     * if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) return
     * Configuration.ORIENTATION_PORTRAIT; else if (orientation ==
     * ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) return Configuration.ORIENTATION_UNDEFINED;
     * return orientation;
     */
        if (context == null)
            context = CardManager.getApplicationContext();
        return context.getResources().getConfiguration().orientation;

    }

    /**
     * Get color.
     *
     * @param webColor
     * @return int
     */
    public static int color(int webColor) {
        return Color.rgb(webColor >> 16, webColor >> 8, webColor & 0x0000FF);
    }

    /**
     * 从SD卡中读取图片
     *
     * @param imgPath 图片位置
     * @return
     */
    public static Bitmap getBitmapFromDisk(String imgPath, int maxSize) {

        File file = new File(imgPath);

        if (!file.exists())
            return null;

        Bitmap ret = null;
        if (maxSize > 0) {
            int width = 0, height = 0;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgPath, options);
                width = options.outWidth;
                height = options.outHeight;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (width != 0 && height != 0) {
                if (width > maxSize || height > maxSize) {

                    int sample = 1;
                    int largerLength = width > height ? width : height;
                    while ((maxSize * sample) < largerLength) {
                        sample *= 2;
                    }
//                    LogUtils.d("#### sample=%s width %s height %s maxSize %s largerLength = %s", sample, width, height, maxSize, largerLength);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    try {
                        options.inSampleSize = sample;
                        ret = BitmapFactory.decodeFile(imgPath, options);
                    } catch (OutOfMemoryError e) {
                        options.inSampleSize = sample * 2;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        try {
                            ret = BitmapFactory.decodeFile(imgPath, options);
                        } catch (OutOfMemoryError e1) {
                        }
                    }
                }
            }
        }

        if (ret == null)
            ret = BitmapFactory.decodeFile(imgPath);


        return ret;
    }

    public static Bitmap getBitmapFromInputStream(InputStream is, BitmapFactory.Options op) {
        Bitmap raw = null;
        try {
            int length = is.available();
            byte[] bytes = new byte[length];
            is.read(bytes, 0, length);
            int offset = 0;
            if (bytes[0] == 'P' && bytes[1] == 'P' && bytes[2] == 'Y')
                offset = 3;
            raw = BitmapFactory.decodeByteArray(bytes, offset, length - offset, op);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return raw;
    }

    /**
     * Create a ScaledBitmap.
     *
     * @param bm
     * @param maxWidth
     * @param maxHeight
     * @return Bitmap
     */
    public static Bitmap createScaledBitmap(@NonNull Bitmap bm, int maxWidth, int maxHeight) {
        if (bm.getWidth() <= maxWidth && bm.getHeight() <= maxHeight)
            return bm;
        int width = bm.getWidth();
        int height = bm.getHeight();

        if (bm.getWidth() > maxWidth) {
            width = maxWidth;
            height = (int) (bm.getHeight() * (maxWidth + 0.0) / bm.getWidth());
        }

        if (height > maxHeight) {
            width = (int) (width * (maxHeight + 0.0) / height);
            height = maxHeight;
        }

        return Bitmap.createScaledBitmap(bm, width, height, true);
    }

    /**
     * Create a ScaledBitmap by providing content model and uri.
     *
     * @param cr
     * @param uri
     * @param maxWidth
     * @param maxHeight
     * @param respectOrientation
     * @return Bitmap
     */
    @CheckForNull
    public static Bitmap createScaledBitmap(@NonNull ContentResolver cr, Uri uri, int maxWidth,
                                            int maxHeight, boolean respectOrientation) {
        InputStream input = null;
        Bitmap ret = null;
        double width = 0, height = 0;
        try {
            input = cr.openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            width = options.outWidth;
            height = options.outHeight;

        } catch (Exception e) {
            LogUtils.e(e, "Failed to createScaledBitmap, get outWidth/Height %s, %d, %d", uri, maxWidth,
                    maxHeight);
        } finally {
            IOUtils.close(input);
        }

        if (width != 0 && height != 0) {
            int sample = 1;
            if (respectOrientation) {
                if ((width - height) * (maxWidth - maxHeight) < 0) {
                    double tmp = width;
                    width = height;
                    height = tmp;
                }
            }

            if (width > maxWidth) {
                sample = (int) Math.ceil(width / maxWidth);
                height = height / sample;
            }

            if (height > (maxHeight * 1.1))
                sample += (int) Math.ceil(height / maxHeight);

            // d("%dx%d, max %dx%d, sample %d", (int) width, (int) height,
            // maxWidth, maxHeight, sample);
            try {
                input = cr.openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = sample;
                ret = BitmapFactory.decodeStream(input, null, options);
            } catch (Exception e) {
                LogUtils.e(e, "Failed to createScaledBitmap, %s, %d, %d", uri, maxWidth, maxHeight);
            } finally {
                IOUtils.close(input);
            }
        }

        if (ret.getWidth() > maxWidth || ret.getHeight() > maxHeight) {
            ret = createScaledBitmap(ret, maxWidth, maxHeight);
        }

        return ret;
    }

    public static Bitmap createScaledBitmapFillWidth(Bitmap bitmap, int outWidth, int outHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) outWidth) / width;
        float scaleHeight = scaleWidth;
        if (outHeight > 0) {
            scaleHeight = ((float) outHeight) / height;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * Set visibility of a specific object.
     *
     * @param o
     * @param visibility
     */
    public static void setVisibility(@CheckForNull Object o, int visibility) {
        if (o == null)
            return;
        if (o instanceof View)
            ((View) o).setVisibility(visibility);
    }

    /**
     * Compress a bitmap to byte[].
     *
     * @param b
     * @param format
     * @param quality
     * @return byte[]
     */
    @CheckForNull
    public static byte[] compressBitmap(@CheckForNull Bitmap b, Bitmap.CompressFormat format,
                                        int quality) {
        if (b == null)
            return null;

        byte[] ret = null;

        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            b.compress(format, quality, output);
            ret = output.toByteArray();
        } catch (Exception e) {
            LogUtils.e(e, "Failed to compressBitmap, %s, %d", format, quality);
        } finally {
            IOUtils.close(output);
        }

        return ret;
    }


    public static Bitmap getCircleBitmap(Bitmap bitmap, float radius) {
        Paint paint = new Paint();
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff000000;
        final Rect rect = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(rect.centerX(), rect.centerY(), radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * Recycle a bitmap.
     *
     * @param bm
     */
    public static void recycle(Bitmap bm) {
        try {
            if (bm != null && !bm.isRecycled())
                bm.recycle();
        } catch (Exception e) {
            LogUtils.e("Failed to recycle bitmap: " + e); //$NON-NLS-1$
        }
    }

    /**
     * Force to clear a bitmap whenever it needs.
     */
    public static void forceClearBitmap() {
        System.gc();
        System.runFinalization();
        System.gc();
    }

    /**
     * Get the device ID.
     *
     * @param ctx
     * @return String
     */
    @NonNull
    public static String getDeviceID(@NonNull Context ctx) {
        String duid = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        return (duid == null || duid.length() == 0) ? "emulator" : duid;
    }

    public static String getString(int id) {
        return CardManager.getApplicationContext().getString(id);
    }

    public static String[] getStringArray(int id) {
        return CardManager.getApplicationContext().getResources().getStringArray(id);
    }

    /**
     * Check whether it's emulator or device.
     *
     * @param ctx
     * @return boolean
     */
    public static boolean isEmulator(@NonNull Context ctx) {
        return getDeviceID(ctx).equals("emulator") || "sdk".equals(android.os.Build.MODEL)
                || "google_sdk".equals(android.os.Build.MODEL);
    }

    /**
     * Creat a group of slide animations.
     *
     * @param fromGravity
     * @return AnimationSet
     */
    @NonNull
    public static AnimationSet makeSlideAnimation(int fromGravity) {
        AnimationSet a = new AnimationSet(true);
        AlphaAnimation fade = new AlphaAnimation(0f, 1.0f);
        a.addAnimation(fade);

        TranslateAnimation slide = null;
        switch (fromGravity) {
            case Gravity.TOP:
                slide =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            case Gravity.LEFT:
                slide =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                                0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            case Gravity.RIGHT:
                slide =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                                0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            case Gravity.BOTTOM:
                slide =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                break;
            default:
                break;
        }

        if (slide != null)
            a.addAnimation(slide);

        a.setInterpolator(new AccelerateDecelerateInterpolator());
        return a;
    }

    /**
     * Get into phone's Gallery.
     *
     * @param a
     * @param requestCode
     */
    public static void startGalleryActivity(@CheckForNull Activity a, int requestCode) {
        if (a == null) {
            if (D)
                d("won't start gallery, activity is null");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        a.startActivityForResult(intent, requestCode);
    }

    /**
     * Create a CharSequence from a Drawable object.
     *
     * @param d
     * @param text
     * @param w
     * @param h
     * @return CharSequence
     */
    public static CharSequence prefixWithDrawable(Drawable d, String text, int w, int h) {
        if (d == null) {
            LogUtils.w("drawable is null?"); //$NON-NLS-1$
            return text;
        }
        if (text == null)
            text = ""; //$NON-NLS-1$
        SpannableString b = new SpannableString(" " + text); //$NON-NLS-1$
        d.setBounds(0, 0, w, h);
        b.setSpan(new ImageSpan(d, DynamicDrawableSpan.ALIGN_BASELINE), 0, 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return b;
    }

    /**
     * Get a bitmap from phone's photos storage.
     *
     * @param a
     * @param data
     * @param maxWidth
     * @param maxHeight
     * @param respectOrientation
     * @return Bitmap
     */
    @CheckForNull
    public static Bitmap getCameraBitmap(@NonNull Activity a, @NonNull Intent data, int maxWidth,
                                         int maxHeight, boolean respectOrientation) {
        Bitmap bm = null;
        try {
            File tmp = TMP_PHOTO; // assign to a local first to avoid a warning
            // of Findbugs
            if (tmp != null && tmp.exists()) {
                // Uri uri = ViewUtils.TMP_PHOTO_URI;
                // startPhotoRotate(a,uri);
                bm =
                        createScaledBitmap(a.getContentResolver(), TMP_PHOTO_URI, maxWidth, maxHeight,
                                respectOrientation);
                tmp.delete();
                TMP_PHOTO = null;
                TMP_PHOTO_URI = null;
            }
        } catch (Exception e) {
            w("Failed to get camera picture from TMP_PHOTO: " + e);
        }
        try {

            if (bm == null) {
                bm = (Bitmap) data.getExtras().get("data");
                if (bm == null) {
                    bm = BitmapFactory.decodeStream(a.getContentResolver().openInputStream(data.getData()));
                }
                if (bm != null) {
                    Bitmap tmp = createScaledBitmap(bm, maxWidth, maxHeight);
                    if (tmp != bm)
                        bm.recycle();
                    bm = tmp;
                }
            }
        } catch (Exception e) {
            w("Failed to get camera picture from extras: " + e);
        }

        return bm;
    }

    @CheckForNull
    public static Bitmap createBitmapFromBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            LogUtils.w(e, "create Bitmap From Bytes error");
            return null;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap =
                    Bitmap.createBitmap(width, height,
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);

            drawable.draw(canvas);
        } catch (Exception e) {
            LogUtils.w(e, "to bitmap error w = %d h = %d", width, height);
            return null;
        } catch (OutOfMemoryError oom) {
            return null;
        }

        return bitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null)
            return null;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * @param bitmap
     * @param roundPx
     * @param size    0: left part 1: leftTop 2: left Bottom 3: Top part 4: right part
     * @return
     */
    public static Bitmap getPartRoundedCornerBitmap(Bitmap bitmap, float roundPx, int size) {
        if (bitmap == null)
            return null;
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        // if(bitmap.getWidth() > SysUtils.WIDTH){
        // w = SysUtils.WIDTH;
        // h = bitmap.getHeight() * w / bitmap.getWidth();
        // LogUtils.d("bitmap size is larger than screen %f %f bitmap old w %d h %d",w,h,bitmap.getWidth(),bitmap.getHeight());
        // }
        Bitmap output = null;
        try {
            output = Bitmap.createBitmap((int) w, (int) h, Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            LogUtils.e(e, "rounded corner error w %f h %f", w, h);
            return null;
        }
        Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint1 = new Paint();

        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        canvas.save();
        Region r = new Region();
        Path p = new Path();
        p.moveTo(0, bitmap.getHeight() / 2);
        if (size == 1) {// Top

            p.lineTo(bitmap.getWidth() / 2, 0);
            p.lineTo(bitmap.getWidth(), 0);
            p.lineTo(bitmap.getWidth(), bitmap.getHeight());
            p.lineTo(0, bitmap.getHeight());
            p.lineTo(0, bitmap.getHeight() / 2);
            p.close();

        } else if (size == 2) {// Bottom
            p.lineTo(bitmap.getWidth() / 2, bitmap.getHeight());
            p.lineTo(bitmap.getWidth(), bitmap.getHeight());
            p.lineTo(0, bitmap.getHeight());
            p.lineTo(0, 0);
            p.lineTo(0, bitmap.getHeight() / 2);
            p.close();
        } else if (size == 0) {
            p.addRect(new RectF(bitmap.getWidth() / 2, 0, bitmap.getWidth(), bitmap.getHeight()),
                    Direction.CW);
        } else if (size == 3) {
            p.addRect(new RectF(0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight()),
                    Direction.CW);
        } else if (size == 4) {
            p.addRect(new RectF(0, 0, bitmap.getWidth() / 2, bitmap.getHeight()), Direction.CW);
        }
        r.setPath(p, new Region(rect));
        canvas.clipRegion(r);
        canvas.drawBitmap(bitmap, rect, rectF, paint1);
        canvas.restore();
        return output;
    }

    public static Bitmap circleBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int r = w > h ? h : w;
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(1);
        // paint.setColor(0xff000000);
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        // RectF rectf = new RectF(0F, 0F, w, h);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.RED);

        canvas.drawRoundRect(rectF, r, r, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rectF, paint);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(rp(4));
        paint.setStyle(Style.STROKE);
        canvas.drawOval(rectF, paint);
        return output;
    }

    public static Bitmap circleBitmapWithoutRing(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int r = w > h ? h : w;
        if (w != h) {
            bitmap = Bitmap.createScaledBitmap(bitmap, r, r, true);
        }
        Bitmap output = Bitmap.createBitmap(r, r, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        // paint.setColor(0xff000000);

        final Rect rect = new Rect(0, 0, r, r);
        final RectF rectF = new RectF(rect);
        // RectF rectf = new RectF(0F, 0F, w, h);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawRoundRect(rectF, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return output;
    }

    public static Bitmap circleBitmap(Bitmap bitmap, int widthPx) {
        widthPx = rp(widthPx);
        int off = widthPx + 1;
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        // if(bitmap.getWidth() > SysUtils.WIDTH){
        // w = SysUtils.WIDTH;
        // h = bitmap.getHeight() * w / bitmap.getWidth();
        // LogUtils.d("bitmap size is larger than screen %f %f bitmap old w %d h %d",w,h,bitmap.getWidth(),bitmap.getHeight());
        // }
        int sizeBitmap = (int) (w > h ? h : w);
        w = sizeBitmap + off * 2;
        h = sizeBitmap + off * 2;

        Bitmap output = null;
        try {
            output = Bitmap.createBitmap((int) w, (int) h, Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            LogUtils.e(e, "circleBitmap  error");
            return null;
        }
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        final Rect rect = new Rect(widthPx, widthPx, (int) w - widthPx, (int) h - widthPx);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setStrokeWidth(widthPx);
        paint.setStyle(Style.STROKE);

        // final Rect rect1 = new Rect(off, off, w-off,h-off);
        // final RectF rectF1 = new RectF(rect1);
        canvas.drawBitmap(circleBitmapWithoutRing(bitmap), off, off, paint);
        // canvas.drawBitmap(circleBitmapWithoutRing(bitmap), rect1, rectF1,
        // paint);
        // paint.set
        paint.setColor(Color.WHITE);

        canvas.drawOval(rectF, paint);
        return output;

    }

    public static Bitmap createRepeater(int width, int height, Bitmap src) {
        int wCount = (width + src.getWidth() - 1) / src.getWidth();
        int hCount = (height + src.getHeight() - 1) / src.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int idx = 0; idx < wCount; ++idx) {
            for (int i = 0; i < hCount; ++i)
                canvas.drawBitmap(src, idx * src.getWidth(), i * src.getHeight(), null);
        }

        return bitmap;
    }

    public static Bitmap bitmapFromRes(@CheckForNull Context con, int id) {
        Resources res = con.getResources();
        InputStream is = res.openRawResource(id);
        return BitmapFactory.decodeStream(is);

    }

    public static Bitmap bitmapRotate(Bitmap b, int degree) {
        Matrix matrix = new Matrix();
        matrix.postScale(1f, 1f);
        matrix.postRotate(degree);
        Bitmap dstbmp = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
        return dstbmp;
    }

    /**
     * Show a toast with a specific view.
     *
     * @param view
     * @param duration
     */
    public static void showToast(@NonNull final View view, final int duration) {
        if (isMainThread())
            showToastIMT(view, duration);
        else
            runInHandlerThread(new Runnable() {
                public void run() {
                    showToastIMT(view, duration);
                }
            });
    }

    /**
     * Show a toast with specific message.
     *
     * @param msg
     * @param duration
     */
    public static void showToast(@CheckForNull final String msg, final int duration) {
        showToast(msg, duration, false);
    }

    /**
     * Show a toast with specific message indicating errors or not.
     *
     * @param msg
     * @param duration
     * @param isError
     */
    public static void showToast(@CheckForNull final String msg, final int duration,
                                 final boolean isError) {
        if (LangUtils.isEmpty(msg)) {
            return;
        }

        if (ViewUtils.isMainThread()) {
            showToastIMT(msg, duration, isError);
        } else {
            ViewUtils.runInHandlerThread(new Runnable() {
                public void run() {
                    showToastIMT(msg, duration, isError);
                }
            });
        }
    }

    /**
     * Show SoftKeyboard of system whenever needed.
     *
     * @param text
     */
    public static void showSoftKeyboard(EditText text) {
        try {
            text.requestFocusFromTouch();
            ((InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(text, 0);
        } catch (Exception e) {
            e(e, "Failed to show soft keyboard");
        }
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm && imm.isActive()) {
            View view = activity.getCurrentFocus();
            if (null != view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Find a view by it's ID in an Activity.
     *
     * @param <T>
     * @param a
     * @param viewID
     * @return T
     */
    @NonNull
    public static <T> T find(@NonNull Activity a, int viewID) {
        return (T) a.findViewById(viewID);
    }

    /**
     * Find a view by it's ID in a view.
     *
     * @param <T>
     * @param a
     * @param viewID
     * @return T
     */
    @NonNull
    public static <T> T find(@NonNull View a, int viewID) {
        return (T) a.findViewById(viewID);
    }


    public static <T> T find(Window window, int viewID) {
        return (T) window.findViewById(viewID);
    }

    /**
     * Get a WindowManager.
     *
     * @param ctx
     * @return WindowManager
     */
    public static WindowManager getWindowManager(Context ctx) {
        WindowManager ret = crackWindowManager();
        if (ret == null && ctx != null)
            try {
                ret = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            } catch (Exception e) {
                w("Failed to get WindowManager");
            }

        return ret;
    }

    /**
     * Private methods
     */
    private static void showToastIMT(String msg, int duration, boolean isError) {
        try {
            if (!isError)
                Toast.makeText(CardManager.getApplicationContext(), msg, duration).show();
            else {
                TextView tv = new TextView(CardManager.getApplicationContext());
                tv.setBackgroundColor(Color.LTGRAY);
                tv.setTextColor(Color.RED);
                tv.setText(msg);
                Toast t = new Toast(tv.getContext());
                t.setView(tv);
                t.setDuration(Toast.LENGTH_SHORT); // hard coded duration
                t.show();
            }
        } catch (Exception e) {
            w("Failed to show toast: " + e);
        }
    }

    private static void showToastIMT(View view, int duration) {
        try {
            Toast t = new Toast(view.getContext());
            t.setView(view);
            t.setDuration(duration);
            t.show();
        } catch (Exception e) {
            w("Failed to show toast: " + e);
        }
    }

    private static WindowManager crackWindowManager() {
        try {
            Class<?> c = Class.forName("android.view.WindowManagerImpl");
            Method m = c.getDeclaredMethod("getDefault");
            WindowManager wm = (WindowManager) m.invoke(null);
            return wm;
        } catch (Exception e) {
            i(e, "Failed to find WindowManagerImpl");
        }

        return null;
    }


    public static byte[] bitmap2bytes(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (null != b && b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap combineFBBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2, (bgHeight - fgHeight) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    }

    public static Bitmap combineTBBitmap(Context context, Bitmap top, Bitmap bittom, int w, int h) {
        if (top == null) {
            return null;
        }
        Bitmap newmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        // canvas.drawColor(context.getResources().getColor(R.color.darkOrange));
        canvas.drawBitmap(top, 0, 0, null);
        return newmap;
    }

    public static Bitmap combineLRBitmap(Bitmap left, Bitmap right) {
        if (left == null) {
            return null;
        }
        int tWidth = left.getWidth();
        int tHeight = left.getHeight();
        Bitmap newmap = Bitmap.createBitmap(tWidth * 2, tHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(left, 0, 0, null);
        canvas.drawBitmap(right, tWidth, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    }

    public static Bitmap combineBigFBBitmap(Bitmap left, Bitmap right, Bitmap foreground) {
        if (left == null) {
            return null;
        }
        // int tWidth = left.getWidth();
        // int tHeight = left.getHeight();
        // Bitmap newmap = Bitmap.createBitmap(tWidth*2, tHeight,
        // Config.ARGB_8888);
        // Canvas canvas = new Canvas(newmap);
        Bitmap bg = combineLRBitmap(left, right);
        Bitmap newmap = combineFBBitmap(bg, foreground);

        // canvas.drawBitmap(bottom, 0, tHeight, null);
        // canvas.save(Canvas.ALL_SAVE_FLAG);
        // canvas.restore();
        return newmap;
    }


    public static String getVersionName() {
        Context context = CardApplication.getApplication();
        if (null == context) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        String version = "";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
            LogUtils.e("version is %s", version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    public static void checkDensity(Context context) {
        double density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            // "xxxhdpi";
            ViewUtils.showToast(String.format("xxxhdpi, density=%s", density), Toast.LENGTH_LONG);
        }
        if (density >= 3.0 && density < 4.0) {
            // xxhdpi
            ViewUtils.showToast(String.format("xxhdpi, density=%s", density), Toast.LENGTH_LONG);
        }
        if (density >= 2.0 && density < 3.0) {
            // xhdpi
            ViewUtils.showToast(String.format("xhdpi, density=%s", density), Toast.LENGTH_LONG);
        }
        if (density >= 1.5 && density < 2.0) {
            // hdpi
            ViewUtils.showToast(String.format("hdpi, density=%s", density), Toast.LENGTH_LONG);
        }
        if (density >= 1.0 && density < 1.5) {
            // mdpi
            ViewUtils.showToast(String.format("mdpi, density=%s", density), Toast.LENGTH_LONG);
        }
        if (density < 1.0) {
            // ldpi
            ViewUtils.showToast(String.format("ldpi, density=%s", density), Toast.LENGTH_LONG);
        }
    }

    public static void extendViewTouchArea(final View currentView, final int left, final int top,
                                           final int right, final int bottom) {
        if (currentView == null)
            return;
        View parentView = (View) currentView.getParent();
        parentView.post(new Runnable() {
            @Override
            public void run() {
                Rect delegateArea = new Rect();
                currentView.getHitRect(delegateArea);
                delegateArea.right += right;
                delegateArea.bottom += bottom;
                delegateArea.left -= left;
                delegateArea.top -= top;
                TouchDelegate touchDelegate = new TouchDelegate(delegateArea, currentView);
                if (View.class.isInstance(currentView.getParent())) {
                    ((View) currentView.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * 在给定的图片的右上角加上圆圈
     *
     * @param icon 给定的图片
     */
    public static Bitmap generatorContactCountIcon(Resources res, Bitmap icon) {
        int iconSize = (int) res.getDimension(android.R.dimen.app_icon_size);
        Bitmap contactIcon = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
        Canvas canvas = new Canvas(contactIcon);

        Paint iconPaint = new Paint();
        iconPaint.setDither(true);
        iconPaint.setFilterBitmap(true);
        Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
        Rect dst = new Rect(0, 0, iconSize, iconSize);
        canvas.drawBitmap(icon, src, dst, iconPaint);


        Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        countPaint.setColor(Color.YELLOW);
        countPaint.setTextSize(20f);
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawCircle(iconSize - 8, 15, 15, countPaint);
        return contactIcon;
    }


    /**
     * @param v
     * @return 0:leftX; 1:topY; 2:width; 3:height
     */
    public static int[] getLocation(View v) {
        int[] location = new int[2];
        int[] loc2 = new int[4];
        v.getLocationOnScreen(location);

        loc2[0] = v.getLeft();
        loc2[1] = location[1];
        loc2[2] = v.getRight() - v.getLeft();
        loc2[3] = v.getBottom() - v.getTop();
        return loc2;
    }

    public static int getOrientation(/* Context context, Uri */String photoUri) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(photoUri);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (rotation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            } else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            } else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            } else
                return 0;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap getCorrectlyOrientedImage(Context context, String photoPath,
                                                   int targetWidth, int targetHeight) throws IOException {
        Uri photoUri = Uri.fromFile(new File(photoPath));
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(/* context, photoUri */photoPath);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > targetWidth || rotatedHeight > targetHeight) {
            float widthRatio = ((float) rotatedWidth) / ((float) targetWidth);
            float heightRatio = ((float) rotatedHeight) / ((float) targetHeight);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            options.inSampleSize = (int) maxRatio;
            options.inPreferredConfig = Config.RGB_565;
            try {
                srcBitmap = BitmapFactory.decodeStream(is, null, options);
            } catch (OutOfMemoryError e) {
                options.inSampleSize = (int) maxRatio * 2;
                options.inPreferredConfig = Config.RGB_565;
                try {
                    srcBitmap = BitmapFactory.decodeStream(is, null, options);
                } catch (OutOfMemoryError e1) {
                    return null;
                }
            }
        } else {
            try {
                srcBitmap = BitmapFactory.decodeStream(is);
            } catch (OutOfMemoryError e) {
                options.inSampleSize = 2;
                options.inPreferredConfig = Config.RGB_565;
                try {
                    srcBitmap = BitmapFactory.decodeStream(is, null, options);
                } catch (OutOfMemoryError e1) {
                    return null;
                }
            }
        }
        is.close();

        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            Bitmap dstBitmap = null;
            try {
                dstBitmap =
                        Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(),
                                matrix, true);
                return dstBitmap;
            } catch (OutOfMemoryError e) {
                options.inSampleSize = 2;
                options.inPreferredConfig = Config.RGB_565;
                try {
                    srcBitmap = BitmapFactory.decodeStream(is, null, options);
                    if (srcBitmap != null) {
                        dstBitmap =
                                Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(),
                                        matrix, true);
                        return dstBitmap;
                    }
                } catch (OutOfMemoryError e1) {
                    return null;
                }
                return null;
            }
        }

        return srcBitmap;
    }


    public static void showSquareToast(final String msg, final int duration) {
        if (LangUtils.isEmpty(msg))
            return;
        if (ViewUtils.isMainThread())
            showCollectToast(msg, duration);
        else {
            ViewUtils.runInHandlerThread(new Runnable() {
                public void run() {
                    showCollectToast(msg, duration);
                }
            });
        }
    }

    private static void showCollectToast(String msg, int duration) {
        TextView tv =
                (TextView) View.inflate(CardManager.getApplicationContext(), R.layout.view_toast_center_tv,
                        null);
        tv.setText(msg);
        Toast t = new Toast(tv.getContext());
        t.setView(tv);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.setDuration(Toast.LENGTH_SHORT); // hard coded duration
        t.show();
    }


    public static Bitmap doScreenShot(View view) {
        LogUtils.d("2525 view.getWidth()=%s; view.getHeight()=%s", view.getWidth(), view.getHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (null != bitmap) {
//            ViewUtils.showToast("get screen shot!", Toast.LENGTH_LONG);
            return bitmap;
        } else {
            try {
                bitmap = Bitmap.createBitmap(view.getWidth(),
                        view.getHeight(), Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError oom) {
                try {
                    bitmap = Bitmap.createBitmap(view.getWidth(),
                            view.getHeight(), Config.RGB_565);
                } catch (OutOfMemoryError oom2) {
                    oom2.printStackTrace();
                }
            }

            if (bitmap != null) {
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
            }

            return bitmap;
        }
    }


    public static String createImageFile() throws IOException {
        String imageFileName = "xxoneside." + System.currentTimeMillis();
        File image = File.createTempFile(imageFileName,
                ".jpg",
                CardManager.getOfficialRootFile());
        return image.getAbsolutePath();
    }


    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static void setImageResource(ImageView imageView, int resId) {
        if (imageView == null || imageView.getContext() == null) {
            return;
        }
        try {
            imageView.setImageResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            try {
                imageView.setImageBitmap(ViewUtils.readBitmap(imageView.getContext(), resId));
            } catch (OutOfMemoryError e1) {
                e1.printStackTrace();
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }

        return hasNavigationBar;
    }

    /**
     * @param view
     * @param color
     */
    public static void setBackgroundRoundCornerColor(View view, int color) {
        if (view == null) {
            return;
        }
        setBackgroundRoundCornerColor(view, color, pr(view.getContext(), 2));
    }

    public static void setBackgroundRoundCornerColor(View view, int color, float padding) {
        if (view == null) {
            return;
        }
        int strokeWidth = 0;
        float roundRadius = padding;
        int strokeColor = Color.TRANSPARENT;
        int fillColor = color;

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(gd);
        } else {
            view.setBackground(gd);
        }
    }

    /**
     * View描边
     *
     * @param view        待描边的View
     * @param borderColor 描边颜色
     */
    public static void setViewBorder(View view, int borderColor) {
        setViewBorder(view, borderColor, rp(1));
    }

    /**
     * View描边
     *
     * @param view        待描边的View
     * @param borderColor 描边颜色
     * @param borderWidth 边宽度
     */
    public static void setViewBorder(View view, int borderColor, int borderWidth) {
        setViewBorder(view, borderColor, borderWidth, 0);
    }

    /**
     * View描边
     *
     * @param view        待描边的View
     * @param borderColor 描边颜色
     * @param borderWidth 边宽度
     * @param roundRadius 边角弧度半径
     */
    public static void setViewBorder(View view, int borderColor, int borderWidth, int roundRadius) {
        setViewBackgroundStyle(view, borderColor, Color.TRANSPARENT, borderWidth, roundRadius);
    }

    /**
     * 设置View背景
     *
     * @param view      view
     * @param fillColor 背景填充颜色
     */
    public static void setViewBackground(View view, int fillColor) {
        setViewBackground(view, fillColor, 0);
    }

    /**
     * 设置View背景
     *
     * @param view        view
     * @param fillColor   背景填充颜色
     * @param roundRadius 边角弧度半径
     */
    public static void setViewBackground(View view, int fillColor, int roundRadius) {
        setViewBackgroundStyle(view, fillColor, fillColor, 0, roundRadius);
    }

    /**
     * 给指定的View背景样式
     *
     * @param view   指定的View
     * @param color  描边颜色
     * @param radius 弧度
     */
    public static void setViewBackgroundStyle(View view, int color, int fillColor, int borderWidth, int radius) {
        if (view == null) {
            return;
        }
        int strokeWidth = borderWidth;
        int roundRadius = radius;
        int strokeColor = color;

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(gd);
        } else {
            view.setBackground(gd);
        }
    }

    /**
     * 生成TitleBar的右侧TextView
     *
     * @param context context
     * @param text    title
     * @return textView
     */
    public static TextView createTitleBarRightTextView(Context context, String text) {
        return createTitleBarRightTextView(context, text, R.color.papaya_primary_color);
    }

    /**
     * 生成TitleBar的右侧TextView
     *
     * @param context context
     * @param text    title
     * @return textView
     */
    public static TextView createTitleBarRightTextView(Context context, String text, int colorId) {
        if (context == null) {
            return null;
        }
        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setTextColor(context.getResources().getColor(colorId));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setMaxEms(5);
        textView.setPadding(0, 0, ViewUtils.rp(context, 10), 0);
        textView.setText(text);

        return textView;
    }

    /**
     * 生成TitleBar的右侧ImageView
     *
     * @param context context
     * @param resId   图片id
     * @return imageView
     */
    public static ImageView createTitleBarRightImageView(Context context, int resId) {
        if (context == null) {
            return null;
        }

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(resId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        return imageView;
    }

    public static Animation loadAnimation(Context context, int id)
            throws Resources.NotFoundException {

        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            return createAnimationFromXml(context, parser);
        } catch (XmlPullParserException ex) {
            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
                    Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex) {
            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
                    Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null) parser.close();
        }
    }


    private static Animation createAnimationFromXml(Context c, XmlPullParser parser)
            throws XmlPullParserException, IOException {

        return createAnimationFromXml(c, parser, null, Xml.asAttributeSet(parser));
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser,
                                                    AnimationSet parent, AttributeSet attrs) throws XmlPullParserException, IOException {

        Animation anim = null;

        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();

        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("set")) {
                anim = new AnimationSet(c, attrs);
                createAnimationFromXml(c, parser, (AnimationSet) anim, attrs);
            } else if (name.equals("alpha")) {
                anim = new AlphaAnimation(c, attrs);
            } else if (name.equals("scale")) {
                anim = new ScaleAnimation(c, attrs);
            } else if (name.equals("rotate")) {
                anim = new RotateAnimation(c, attrs);
            } else if (name.equals("translate")) {
                anim = new TranslateAnimation(c, attrs);
            } else {
                try {
                    anim = (Animation) Class.forName(name).getConstructor(Context.class, AttributeSet.class).newInstance(c, attrs);
                } catch (Exception te) {
                    throw new RuntimeException("Unknown animation name: " + parser.getName() + " error:" + te.getMessage());
                }
            }

            if (parent != null) {
                parent.addAnimation(anim);
            }
        }

        return anim;

    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    /**
     * 设置View的背景
     *
     * @param target     目标View
     * @param drawableId drawableId
     */
    public static void setViewBackgroundDrawable(View target, int drawableId) {
        if (target == null) {
            return;
        }

        Drawable drawable = ContextCompat.getDrawable(target.getContext(), drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            target.setBackgroundDrawable(drawable);
        } else {
            target.setBackground(drawable);
        }
    }
}
