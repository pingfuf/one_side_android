package com.kuaipao.ui.photopicker;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kuaipao.utils.JumpCenter;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.manager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.ViewpagerImageItem;

public class CircleFindPagerAdapter extends PagerAdapter {

    private List<ViewpagerImageItem> paths = new ArrayList<ViewpagerImageItem>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private int size;


    public CircleFindPagerAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<ViewpagerImageItem> datas) {
        if (this.paths == null)
            this.paths = new ArrayList<>();
        this.paths.clear();
        if (LangUtils.isNotEmpty(datas)) {
            this.paths.addAll(datas);
        }
        this.size = this.paths.size();
        notifyDataSetChanged();
    }


    public int getPosition(int position) {
        return position % (size == 0 ? 1 : size);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.circle_find_view_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);

        if (LangUtils.isNotEmpty(paths) && position >= 0 && position < paths.size()) {

            final String path = paths.get(position).getLogoUrl();

            final Uri uri;
            if (path.startsWith("http")) {
                uri = Uri.parse(path);
            } else {
                uri = Uri.fromFile(new File(path));
            }
            try {
                Glide.with(mContext).load(uri)/*.centerCrop()*/.placeholder(R.drawable.ic_default_photo)
                        .error(R.drawable.default_pic_fail).override(800, 800).into(imageView);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LangUtils.isNotEmpty(paths.get(position).getUrl()))
                        JumpCenter.JumpWebActivity(mContext, paths.get(position).getUrl());
                }
            });
        }

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
