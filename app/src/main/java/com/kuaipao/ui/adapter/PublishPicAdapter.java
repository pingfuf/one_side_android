package com.kuaipao.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.kuaipao.base.adapter.XSimpleAdapter;
import com.kuaipao.base.utils.XGlideUtils;
import com.kuaipao.base.view.XImageView;
import com.kuaipao.manager.R;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.List;

public class PublishPicAdapter extends XSimpleAdapter<String> {
    public static final String ADD_TAG = "ADD_TAG";
    public static final int ITEM_SIZE = ViewUtils.rp(66);

    public PublishPicAdapter(Context context) {
        super(context);
    }

    @Override
    public void setData(List<String> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }

        if (mData.size() < 9) {
            mData.add(ADD_TAG);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        XImageView ivPic;
        if (convertView == null) {
            ivPic = new XImageView(mContext);
            GridView.LayoutParams layoutParams = new GridView.LayoutParams(ITEM_SIZE, ITEM_SIZE);
            ivPic.setLayoutParams(layoutParams);
            ivPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivPic.setBackgroundColor(mContext.getResources().getColor(R.color.layout_background));
        } else {
            ivPic = (XImageView) convertView;
        }

        String strPath = getItem(position);
        if (ADD_TAG.equals(strPath)) {
            ivPic.setImageResource(R.drawable.add_photo_box);
        } else {
            ivPic.setImageUri(strPath, R.drawable.ic_default_photo);

        }

        return ivPic;
    }
}
