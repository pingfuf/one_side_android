package com.oneside.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.oneside.base.utils.XGlideUtils;
import com.oneside.utils.Constant;
import com.oneside.utils.LangUtils;
import com.oneside.R;
import com.oneside.utils.LogUtils;


/**
 * 可以接受网络和本地URI图片的ImageView  Note load res need just called setImageResource
 * <p/>
 * Created by pingfu on 16-7-1.
 */
public class XImageView extends ImageView {
    //边框颜色
    protected int mBorderColor;

    //边框大小
    protected int mBorderSize;

    //默认图片ID
    protected int mDefaultResId = R.drawable.ic_default_photo;

    //网络异常图片ID
    protected int mNetErrorResId = R.drawable.ic_default_photo;

    private String uri;//local path or net work path;

    public XImageView(Context context) {
        super(context);
    }

    public XImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public XImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.XImageView);
            mBorderSize = a.getDimensionPixelSize(R.styleable.XImageView_border_width, 0);
            mBorderColor = a.getColor(R.styleable.XImageView_border_color, Color.TRANSPARENT);
            int defaultResId = a.getResourceId(R.styleable.XImageView_default_src, -1);
            int defaultNetErrorId = a.getResourceId(R.styleable.XImageView_net_error_src, -1);
            if (defaultResId > 0) {
                mDefaultResId = defaultResId;
            }

            if (defaultNetErrorId > 0) {
                mNetErrorResId = defaultNetErrorId;
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    @Override
    public void setImageResource(int resId) {
        Glide.with(getContext())
                .load(resId)
                .asBitmap()
                .centerCrop()
                .into(this);
    }

    /**
     * 通过URI获取图片内容
     *
     * @param uri 图片资源的URI
     */
    public void setImageUri(String uri) {
        setImageUri(uri, mDefaultResId);
    }

    /**
     * 通过URI获取图片内容
     *
     * @param uri          图片资源的URI
     * @param defaultResId 默认的图片资源ID
     */
    public void setImageUri(String uri, int defaultResId) {
        mDefaultResId = defaultResId;
        setImageUri(uri, mDefaultResId, mNetErrorResId);
    }

    /**
     * 通过URI获取图片内容
     *
     * @param uri           图片资源的URI
     * @param defaultResId  默认的图片资源ID
     * @param netErrorResId 获取URI错误的时候的资源id
     */
    public void setImageUri(String uri, int defaultResId, int netErrorResId) {
        if (LangUtils.isEmpty(uri)) {
            setImageResource(defaultResId);
            return;
        }
        String realUri = uri;

        //谁能告诉我为什么url以“//”开头，还以数据库中存储就是这样为接口，理所当然的不改
        //能这么干吗？
        if (uri.startsWith("//")) {
            realUri = Constant.HTTP + uri;
        }
        mDefaultResId = defaultResId;
        mNetErrorResId = netErrorResId;
        XGlideUtils.loadImage(this, realUri, true, defaultResId, netErrorResId);
    }
}
