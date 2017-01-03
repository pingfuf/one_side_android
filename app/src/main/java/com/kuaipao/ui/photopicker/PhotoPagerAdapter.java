package com.kuaipao.ui.photopicker;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.SysUtils;
import com.kuaipao.ui.view.CircularProgressView;
import com.kuaipao.manager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoPagerAdapter extends PagerAdapter {

    private List<String> paths = new ArrayList<String>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public PhotoPagerAdapter(Context mContext, List<String> paths) {
        this.mContext = mContext;
        this.paths = paths;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.photo_pager_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);
        final ImageView ivGif = (ImageView) itemView.findViewById(R.id.iv_gif_detail);

        final CircularProgressView progressBar = (CircularProgressView) itemView.findViewById(R.id.progress_bar);

        final String path = paths.get(position);

        final Uri uri;
        boolean isFromWeb;
        if (path.startsWith("//")) {
            uri = Uri.parse(Constant.HTTP + path);
            isFromWeb = true;
        } else if (path.startsWith("http")) {
            uri = Uri.parse(path);
            isFromWeb = true;
        } else {
            uri = Uri.fromFile(new File(path));
            isFromWeb = false;
        }

        progressBar.setVisibility(View.VISIBLE);

        int width = Math.min(800, SysUtils.WIDTH);
        int height = Math.min(800, SysUtils.HEIGHT);
        if (isFromWeb) {
            Glide.with(mContext).load(uri).into(new SimpleTarget<GlideDrawable>(width, height) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    progressBar.setVisibility(View.GONE);

                    LogUtils.d("v3131 onResourceReady");
                    if (resource != null) {
                        ImageViewTargetFactory factory = new ImageViewTargetFactory();
                        Target target = null;
                        if (resource instanceof GifDrawable) {
                            LogUtils.d("v3131 onResourceReady resource instanceof GifDrawable");
                            ivGif.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);

                            target = factory.buildTarget(ivGif, GifDrawable.class);

                        } else {
                            ivGif.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            LogUtils.d("v3131 onResourceReady resource not instanceof GifDrawable");

                            target = new GlideDrawableImageViewTarget(imageView);
                        }
                        if (target != null)
                            target.onResourceReady(resource, null);
                    }

                }

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    LogUtils.d("v3131 onLoadStarted path=%s", path);
                    ivGif.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.ic_default_photo);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    LogUtils.d("v3131 onLoadFailed path=%s", path);
                    progressBar.setVisibility(View.GONE);
                    ivGif.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.default_pic_fail);
                }
            });

        } else {
            Glide.with(mContext).load(uri).asGif().into(new SimpleTarget<GifDrawable>(width, height) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    LogUtils.d("v3131 onLoadStarted path=%s", path);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    int w = Math.min(600, SysUtils.WIDTH);
                    int h = Math.min(600, SysUtils.HEIGHT);
                    Glide.with(mContext).load(uri).into(new SimpleTarget<GlideDrawable>(w, h) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            LogUtils.d("v3131 onResourceReady; path=%s; resource=%s", path, resource);
                            progressBar.setVisibility(View.GONE);

                            if (resource != null) {
                                ivGif.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);

                                Target target = new GlideDrawableImageViewTarget(imageView);
                                target.onResourceReady(resource, null);
                            }
                        }


                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            ivGif.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.drawable.ic_default_photo);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            progressBar.setVisibility(View.GONE);
                            ivGif.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.drawable.default_pic_fail);
                        }
                    });
                }

                @Override
                public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                    LogUtils.d("v3131 onResourceReady path=%s; resource=%s", path, resource);
                    LogUtils.d("bitmapSize: height = %s, width = %s", resource.getIntrinsicHeight(), resource.getIntrinsicWidth());
                    if (resource != null) {
                        ivGif.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                        LogUtils.d("v3131 onResourceReady into(ivGif) start");
                        ImageViewTargetFactory factory = new ImageViewTargetFactory();
                        Target target = factory.buildTarget(ivGif, GifDrawable.class);
                        target.onResourceReady(resource, null);

                        LogUtils.d("v3131 onResourceReady into(ivGif) end");
                    }
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });

        container.addView(itemView);

        return itemView;
    }


    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
