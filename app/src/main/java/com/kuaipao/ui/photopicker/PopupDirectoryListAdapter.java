package com.kuaipao.ui.photopicker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuaipao.base.utils.XGlideUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.manager.R;

public class PopupDirectoryListAdapter extends BaseAdapter {

    private Context context;

    private List<PhotoDirectory> directories = new ArrayList<PhotoDirectory>();

    private LayoutInflater mLayoutInflater;

    private int currentCheckedIndex = 0;


    public PopupDirectoryListAdapter(Context context, List<PhotoDirectory> directories) {
        this.context = context;
        this.directories = directories;

        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return directories.size();
    }


    @Override
    public PhotoDirectory getItem(int position) {
        return directories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return directories.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.photo_picker_item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoDirectory directory = directories.get(position);

        XGlideUtils.loadImage(holder.ivCover, directory.getCoverPath(), true);

        holder.tvName.setText(directory.getName());
        holder.tvCount.setText(context.getString(R.string.file_count,
                LangUtils.isEmpty(directory.getPhotos()) ? 0 : directory.getPhotos().size()));

        if (position == MediaStoreHelper.INDEX_ALL_PHOTOS) {
            holder.tvCount.setVisibility(View.GONE);
        } else
            holder.tvCount.setVisibility(View.VISIBLE);

        if (currentCheckedIndex == position) {
            holder.ivChecked.setVisibility(View.VISIBLE);
        } else
            holder.ivChecked.setVisibility(View.GONE);

        return convertView;
    }

    public void setCurrentCheckedIndex(int currentCheckedIndex) {
        this.currentCheckedIndex = currentCheckedIndex;
    }

    public int getCurrentCheckedIndex() {
        return this.currentCheckedIndex;
    }

    private class ViewHolder {

        public ImageView ivCover;
        public TextView tvName;
        public TextView tvCount;
        public ImageView ivChecked;

        public ViewHolder(View rootView) {
            ivCover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
            tvName = (TextView) rootView.findViewById(R.id.tv_dir_name);
            tvCount = (TextView) rootView.findViewById(R.id.tv_dir_item_count);
            ivChecked = (ImageView) rootView.findViewById(R.id.iv_checked);
        }
    }

}

