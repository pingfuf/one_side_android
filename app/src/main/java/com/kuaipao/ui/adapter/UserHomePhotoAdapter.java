package com.kuaipao.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kuaipao.base.utils.XGlideUtils;
import com.kuaipao.model.UserHomePhotoImg;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.manager.R;

import java.util.ArrayList;
import java.util.List;

import static com.kuaipao.utils.ViewUtils.find;

/**
 * Created by MVEN on 16/1/26.
 */
public class UserHomePhotoAdapter extends BaseAdapter {
    private Context mContext;
    private View.OnClickListener mImgListLogoClicker;

    public interface OnImgClickListner {
        void onImgClick(int index);
    }

    private class UserHomePhotoUnit {
        public UserHomePhotoImg photoImg0;
        public UserHomePhotoImg photoImg1;
        public UserHomePhotoImg photoImg2;
    }

    private OnImgClickListner onImgClickListener;

    public void setOnImgClickListener(OnImgClickListner onImgClickListener) {
        this.onImgClickListener = onImgClickListener;
    }

    private List<UserHomePhotoUnit> homePhotoUnitList;

    public UserHomePhotoAdapter(Context mContext) {
        this.mContext = mContext;
        if (mImgListLogoClicker == null)
            mImgListLogoClicker = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    if (onImgClickListener != null) {
                        onImgClickListener.onImgClick(position - 1);
                    }
                }
            };
    }

    public void setListData(List<UserHomePhotoImg> photoImgs) {
        if (this.homePhotoUnitList == null)
            this.homePhotoUnitList = new ArrayList<>();
        this.homePhotoUnitList.clear();

        if (LangUtils.isNotEmpty(photoImgs)) {
            int circlesSize = photoImgs.size();
            int unitCount = (circlesSize + 2) / 3;
            for (int i = 0; i < unitCount; i++) {
                UserHomePhotoUnit photo = new UserHomePhotoUnit();
                int baseIndex = 3 * i;
                photo.photoImg0 = photoImgs.get(baseIndex);

                if (baseIndex + 1 < circlesSize) {
                    photo.photoImg1 = photoImgs.get(baseIndex + 1);
                }
                if (baseIndex + 2 < circlesSize) {
                    photo.photoImg2 = photoImgs.get(baseIndex + 2);
                }
                this.homePhotoUnitList.add(photo);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == homePhotoUnitList)
            return 0;
        else
            return homePhotoUnitList.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == homePhotoUnitList)
            return null;
        else
            return homePhotoUnitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.adapter_user_home_photo, null);
            holder = new ViewHolder();
            holder.layoutLeft = find(convertView, R.id.layout_left);
            holder.ivLeft = find(convertView, R.id.iv_left);
            holder.layoutCenter = find(convertView, R.id.layout_center);
            holder.ivCenter = find(convertView, R.id.iv_center);
            holder.layoutRight = find(convertView, R.id.layout_right);
            holder.ivRight = find(convertView, R.id.iv_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (LangUtils.isNotEmpty(homePhotoUnitList) && position >= 0 && position < homePhotoUnitList.size()) {
            UserHomePhotoUnit photo = homePhotoUnitList.get(position);
            if (photo.photoImg0 != null) {
                holder.layoutLeft.setVisibility(View.VISIBLE);
                holder.layoutLeft.setTag(3 * position + 1);
                holder.layoutLeft.setOnClickListener(mImgListLogoClicker);
                holder.ivLeft.setImageResource(R.drawable.ic_user_logo_default_88_in_user_info);
                XGlideUtils.loadImage(holder.ivLeft, photo.photoImg0.getThumb(), true);
            } else {
                holder.layoutLeft.setVisibility(View.INVISIBLE);
                holder.layoutLeft.setOnClickListener(null);
            }
            if (photo.photoImg1 != null) {
                holder.layoutCenter.setVisibility(View.VISIBLE);
                holder.layoutCenter.setTag(3 * position + 2);
                holder.layoutCenter.setOnClickListener(mImgListLogoClicker);
                XGlideUtils.loadImage(holder.ivCenter, photo.photoImg1.getThumb(), true);
            } else {
                holder.layoutCenter.setVisibility(View.INVISIBLE);
                holder.layoutCenter.setOnClickListener(null);
            }

            if (photo.photoImg2 != null) {
                holder.layoutRight.setVisibility(View.VISIBLE);
                holder.layoutRight.setTag(3 * position + 3);
                holder.layoutRight.setOnClickListener(mImgListLogoClicker);
                XGlideUtils.loadImage(holder.ivRight, photo.photoImg2.getThumb(), true);
            } else {
                holder.layoutRight.setVisibility(View.INVISIBLE);
                holder.layoutRight.setOnClickListener(null);
            }
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout layoutLeft;
        LinearLayout layoutCenter;
        LinearLayout layoutRight;
        ImageView ivLeft;
        ImageView ivCenter;
        ImageView ivRight;
    }
}
