package com.oneside.base.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.oneside.manager.CardManager;
import com.oneside.R;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;

import java.io.File;

/**
 * Created by pingfu on 16-7-1.
 */
public class XGlideUtils {
    /**
     * 加载图片
     *
     * @param target 目标View
     * @param strImg 图片URI
     */
    public static void loadImage(ImageView target, String strImg) {
        loadImage(target, strImg, false);
    }

    public static void loadImage(ImageView target, String strImg, boolean show) {
        loadImage(target, strImg, show, R.drawable.ic_default_photo);
    }

    public static void loadImage(ImageView target, String strImg, boolean show, int defaultResId) {
        loadImage(target, strImg, show, defaultResId, R.drawable.ic_default_photo);
    }

    /**
     * 加载Image资源，ImageView的资源分为网络资源和
     * @param target
     * @param strImg
     * @param show
     * @param defaultResId
     * @param errorResId
     */
    public static void loadImage(ImageView target, String strImg, boolean show, int defaultResId, int errorResId) {
        loadDefinedSizedImage(target, strImg, defaultResId, errorResId, show);
    }

    private static void loadDefinedSizedImage(ImageView imageView, String strImg, int defaultResID, int errorResID, boolean bAlwaysShow) {
        if(imageView == null || imageView.getContext() == null) {
            return;
        }
        if (defaultResID != -1) {
            ViewUtils.setImageResource(imageView, defaultResID);
        }
        if (strImg != null) {
            final Uri uri;
            if (strImg.startsWith("http")) {
                uri = Uri.parse(strImg);
            } else {
                uri = Uri.fromFile(new File(strImg));
            }
            loadDefinedSizedImage(uri, imageView, defaultResID, errorResID, -1, -1, bAlwaysShow);
        }
    }

    private static void loadDefinedSizedImage(Uri uri, ImageView imageView, int defaultResID, int errorResID,
                                       int definedWidth, int definedHeight, boolean bAlwaysShow) {
        if (defaultResID != -1) {
            imageView.setImageResource(defaultResID);
        }
        if (!bAlwaysShow) {
            return;
        }



        try {
            if (uri != null) {
                if (definedWidth == -1 && definedHeight == -1) {
                    if (defaultResID != -1 && errorResID != -1) {
                        Glide.with(imageView.getContext())
                                .load(uri)
                                .asBitmap()
                                .centerCrop()
                                .placeholder(defaultResID)
                                .error(errorResID)
                                .into(imageView);
                    } else {
                        Glide.with(imageView.getContext())
                                .load(uri)
                                .asBitmap()
                                .centerCrop()
                                .into(imageView);
                    }
                } else {
                    if (defaultResID != -1 && errorResID != -1) {
                        Glide.with(imageView.getContext())
                                .load(uri)
                                .asBitmap()
                                .override(definedWidth, definedHeight)
                                .placeholder(defaultResID)
                                .error(errorResID)
                                .into(imageView);
                    } else {
                        Glide.with(imageView.getContext())
                                .load(uri)
                                .asBitmap()
                                .override(definedWidth, definedHeight)
                                .into(imageView);
                    }
                }
            }
        } catch (OutOfMemoryError oom) {
            oom.printStackTrace();
        }
    }
}
