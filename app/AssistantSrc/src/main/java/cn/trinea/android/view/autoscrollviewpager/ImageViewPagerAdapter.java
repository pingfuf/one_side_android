/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package cn.trinea.android.view.autoscrollviewpager;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.oneside.utils.JumpCenter;
import com.oneside.utils.LangUtils;
import com.oneside.utils.SysUtils;
import com.oneside.R;

import java.util.ArrayList;
import java.util.List;


/**
 * ImagePagerAdapter
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImageViewPagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<ViewpagerImageItem> imageIdList;

    private int size;

    public ImageViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<ViewpagerImageItem> imageIdList){
        if(this.imageIdList == null)
            this.imageIdList = new ArrayList<>();
        this.imageIdList.clear();
        if(LangUtils.isNotEmpty(imageIdList)){
            this.imageIdList.addAll(imageIdList);
        }
        this.size = imageIdList.size();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // Infinite loop
        return imageIdList == null ? 0 : imageIdList.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    public int getPosition(int position) {
        return position % (size == 0 ? 1 : size);
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
//        ViewHolder holder;
        if (view == null) {
//            holder = new ViewHolder();
            view = new ImageView/*LazyImageView*/(context);
//            view.setTag(holder);
        } else {
//            holder = (ViewHolder)view.getTag();
        }

//        ((ImageView)view).setImageResource(R.drawable.ic_merchant_default_photo);
        ((ImageView)view).setScaleType(ImageView.ScaleType.FIT_XY);

        if(LangUtils.isNotEmpty(imageIdList) && imageIdList.size() > getPosition(position)) {
            Uri uri = Uri.parse(imageIdList.get(getPosition(position)).getLogoUrl());
            try {
                Glide.with(context).load(uri)./*centerCrop().placeholder(R.drawable.ic_merchant_default_photo)
                    .error(R.drawable.ic_merchant_default_photo).*/override(SysUtils.WIDTH, SysUtils.WIDTH).into((ImageView) view);
//        holder.imageView.setImageUrl(imageIdList.get(getPosition(position)).getUrl());
            }catch (OutOfMemoryError error){
                error.printStackTrace();
            }

            final String mStrBannerContentUrl = imageIdList.get(getPosition(position)).getUrl();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LangUtils.isEmpty(mStrBannerContentUrl))
                        return;
//                CardManager.logUmengEvent(Constant.UMENG_EVENT_BANNER_CLICK);
                    JumpCenter.JumpWebActivity(context, mStrBannerContentUrl);
                }
            });
        }else {
            ((ImageView)view).setImageResource(R.drawable.layout_background);
        }

        return view;
    }

//    public static class ViewHolder {
//
//        ImageView imageView;
//    }
}
