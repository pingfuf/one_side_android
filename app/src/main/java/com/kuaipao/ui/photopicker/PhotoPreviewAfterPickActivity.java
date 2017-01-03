package com.kuaipao.ui.photopicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.manager.CardManager;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.SysUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

import java.io.File;
import java.io.IOException;

public class PhotoPreviewAfterPickActivity extends BaseActivity implements OnTouchListener {
    public final static String EXTRA_WILL_CROP_IMAGE = "will_crop_image";
    public final static String EXTRA_WILL_CROP_IMG_FOR_BG = "extra_will_crop_img_for_bg";
    public final static String EXTRA_PHOTO_PATH = "photo_path";

    private ImageView ivImageView;
    private ImageView ivImageClip;
    private ClipView mClipView;

    private boolean bWillCropImage = false;
    private boolean bWillCropForBg = false;

    private String mStrCurrentPhotoPath;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    /**
     * 动作标志：无
     */
    private static final int NONE = 0;
    /**
     * 动作标志：拖动
     */
    private static final int DRAG = 1;
    /**
     * 动作标志：缩放
     */
    private static final int ZOOM = 2;
    /**
     * 初始化动作标志
     */
    private int mode = NONE;

    /**
     * 记录起始坐标
     */
    private PointF start = new PointF();
    /**
     * 记录缩放时两指中间点坐标
     */
    private PointF mid = new PointF();
    private float oldDist = 1f;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_preview_after_pick);
        setTitle("", true);
        // setTitle(R.string.title_activity_scan_choose_pic);
        initData();
        initView();
    }

    private void initData() {
        mStrCurrentPhotoPath = getIntent().getStringExtra(EXTRA_PHOTO_PATH);
        bWillCropImage = getIntent().getBooleanExtra(EXTRA_WILL_CROP_IMAGE, false);
        bWillCropForBg = getIntent().getBooleanExtra(EXTRA_WILL_CROP_IMG_FOR_BG, false);
    }

    private void initView() {
        ivImageView = (ImageView) findViewById(R.id.iv_image_view);
        ivImageClip = (ImageView) findViewById(R.id.iv_image_clip);
        mClipView = (ClipView) findViewById(R.id.iv_clip_view);

        File file = new File(mStrCurrentPhotoPath);
        LogUtils.d(">>>> initView() mStrCurrentPhotoPath=%s", mStrCurrentPhotoPath);
        if (!file.exists())
            return;
        LogUtils.d(">>>> initView() file.exists()");

        final Uri uri;
        if (mStrCurrentPhotoPath.startsWith("http")) {
            uri = Uri.parse(mStrCurrentPhotoPath);
        } else {
            uri = Uri.fromFile(file);
        }


        if (bWillCropImage) {
            mClipView.setVisibility(View.VISIBLE);
            ivImageClip.setVisibility(View.VISIBLE);
            ivImageView.setVisibility(View.GONE);
            ivImageClip.setOnTouchListener(this);

            ViewTreeObserver observer = ivImageClip.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                public void onGlobalLayout() {
                    ivImageClip.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    initClipView(uri);
                }
            });
        } else if (bWillCropForBg) {
            mClipView.setVisibility(View.VISIBLE);
            ivImageClip.setVisibility(View.VISIBLE);
            ivImageView.setVisibility(View.GONE);
            ivImageClip.setOnTouchListener(this);
            mClipView.setClipHeight((int) (SysUtils.WIDTH / 1.44));
            mClipView.setClipLeftMargin(0);
            mClipView.setClipTopMargin((SysUtils.HEIGHT - mClipView.getClipHeight()) / 2);
            mClipView.setClipWidth(SysUtils.WIDTH);
            ViewTreeObserver observer = ivImageClip.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                public void onGlobalLayout() {
                    ivImageClip.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    initClipView(uri);
                }
            });
        } else {
            mClipView.setVisibility(View.GONE);
            ivImageClip.setVisibility(View.GONE);
            ivImageView.setVisibility(View.VISIBLE);
            setTitle(String.format("%d/%d", 1, 1));

//      new GlideUtils(this).loadDefinedSizedImage(uri, ivImageView, 
//          R.drawable.ic_merchant_default_photo, R.drawable.default_pic_fail, 800, 800, true);
            try {
                Glide.with(this).load(uri)/*.centerCrop()*/.placeholder(R.drawable.ic_default_photo)
                        .error(R.drawable.default_pic_fail).override(800, 800).into(ivImageView);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
        }
    }

    private void initClipView(final Uri uri) {
        mClipView.setCustomTopBarHeight(0/* ViewUtils.rp(50) */);
        mClipView.addOnDrawCompleteListener(new ClipView.OnDrawListenerComplete() {
            @Override
            public void onDrawComplete() {
                mClipView.removeOnDrawCompleteListener();
                int clipHeight = mClipView.getClipHeight();
                int clipWidth = mClipView.getClipWidth();
                int midX = mClipView.getClipLeftMargin() + (clipWidth / 2);
                int midY = mClipView.getClipTopMargin() + (clipHeight / 2);

                Bitmap bitmap = getBitmap(uri);
                if (bitmap == null)
                    return;

                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                // 按裁剪框求缩放比例
                float scale = (clipWidth * 1.0f) / imageWidth;
                if (imageWidth > imageHeight) {
                    scale = (clipHeight * 1.0f) / imageHeight;
                }

                // 起始中心点
                float imageMidX = imageWidth * scale / 2;
                float imageMidY = mClipView.getCustomTopBarHeight() + imageHeight * scale / 2;
                ivImageClip.setScaleType(ScaleType.MATRIX);

                matrix.postScale(scale, scale);
                matrix.postTranslate(midX - imageMidX, midY - imageMidY);
                ivImageClip.setImageMatrix(matrix);
                ivImageClip.setImageBitmap(bitmap);
            }
        });
    }

    private Bitmap getBitmap(Uri uri) {
//    return ViewUtils.createScaledBitmap(this.getContentResolver(), uri, SysUtils.WIDTH,
//        SysUtils.HEIGHT - ViewUtils.rp(50), false);
        try {
            return ViewUtils.getCorrectlyOrientedImage(this, mStrCurrentPhotoPath,
                    SysUtils.WIDTH, SysUtils.HEIGHT - ViewUtils.rp(50));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    protected boolean onRightClick(View view) {
        Intent i = new Intent();
        if (bWillCropImage || bWillCropForBg) {

            i.putExtra(EXTRA_PHOTO_PATH, cropAndSaveBitmapIntoFile());
            setResult(RESULT_OK, i);

            finish();
            return true;
        } else {
            i.putExtra(EXTRA_PHOTO_PATH, mStrCurrentPhotoPath);
            setResult(RESULT_OK, i);
            finish();
        }
        return true;
    }

    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                // 设置开始点位置
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
        return true;
    }

    /**
     * 多点触控时，计算最先放下的两指距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * 获取裁剪框内截图
     *
     * @return file path
     */
    private String cropAndSaveBitmapIntoFile() {
        if (LangUtils.isEmpty(mStrCurrentPhotoPath))
            return null;

        long currentTime = System.currentTimeMillis();

        Bitmap finalBitmap = getCroppedImage();

        if (finalBitmap == null) {
            return null;
        }


        File fileOriginal = new File(mStrCurrentPhotoPath);
        String imageFileName = fileOriginal.getName() + "_" + currentTime + "_crop";
        if (imageFileName.length() > 25)
            imageFileName = imageFileName.substring(0, 25);
        File cropFile = null;
        try {
            cropFile = File.createTempFile(imageFileName, // prefix
                    ".jpg", // suffix
                    CardManager.getOfficialRootFile() // directory
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cropFile != null) {
            IOUtils.saveBitmap(finalBitmap, cropFile, Bitmap.CompressFormat.JPEG, 85);
        }
        if (finalBitmap != null) {
            finalBitmap.recycle();
        }
        LogUtils.d(">>>> cropAndSaveBitmapIntoFile() passed=%s", System.currentTimeMillis() - currentTime);

        return cropFile != null ? cropFile.getAbsolutePath() : null;
    }

    private Bitmap getCurrentDisplayedImage() {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(ivImageClip.getWidth(), ivImageClip.getHeight(), Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError e) {
            try {
                result = Bitmap.createBitmap(ivImageClip.getWidth() / 2, ivImageClip.getHeight() / 2, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError e1) {
                return null;
            }
        }
        Canvas c = new Canvas(result);
        ivImageClip.draw(c);
        return result;
    }


    public Bitmap getCroppedImage() {
        // // 获取截屏
        // View view = this.getWindow().getDecorView();
        // view.setDrawingCacheEnabled(true);
        // view.buildDrawingCache();
        //
        // // 获取状态栏高度
        // Rect frame = new Rect();
        // this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // int statusBarHeight = frame.top +
        // getResources().getDimensionPixelSize(R.dimen.base_title_height);
        //
        // Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
        // mClipView.getClipLeftMargin(), mClipView.getClipTopMargin()
        // + statusBarHeight, mClipView.getClipWidth(),
        // mClipView.getClipHeight());
        //
        // // 释放资源
        // view.destroyDrawingCache();

        Bitmap mCurrentDisplayedBitmap = getCurrentDisplayedImage();

        if (mCurrentDisplayedBitmap == null)
            return null;

        Bitmap result;

        try {
            result = Bitmap.createBitmap(mCurrentDisplayedBitmap, mClipView.getClipLeftMargin(),
                    mClipView.getClipTopMargin(), mClipView.getClipWidth(), mClipView.getClipHeight());
        } catch (IllegalArgumentException e) {
//      e.printStackTrace();
            result = mCurrentDisplayedBitmap;
        } catch (OutOfMemoryError e) {
//      e.printStackTrace();
            result = mCurrentDisplayedBitmap;
        }
        // Crop the subset from the original Bitmap.
        return result;
    }
}
