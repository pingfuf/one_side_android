package com.oneside.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.oneside.base.utils.XGlideUtils;
import com.oneside.utils.LangUtils;
import com.oneside.R;

import java.util.ArrayList;
import java.util.List;

public class CommentPicturesAdapter extends BaseAdapter {


    private Context mContext;
    private List<String> list;
    private List<Boolean> listIsGif;


    public CommentPicturesAdapter(Context con, List<String> list) {
        this.mContext = con;
        if (this.list == null) {
            this.list = new ArrayList<String>();
        }
        if (LangUtils.isNotEmpty(list)) {
            this.list.addAll(list);
        }


        if (this.listIsGif == null) {
            this.listIsGif = new ArrayList<Boolean>();
        }
        for (String img : list) {
            this.listIsGif.add(false);
        }
    }

    public CommentPicturesAdapter(Context con, List<String> list, List<Boolean> listIsGif) {
        this.mContext = con;
        if (this.list == null) {
            this.list = new ArrayList<String>();
        }
        if (LangUtils.isNotEmpty(list)) {
            this.list.addAll(list);
        }

        if (this.listIsGif == null) {
            this.listIsGif = new ArrayList<Boolean>();
        }
        if (LangUtils.isNotEmpty(listIsGif)) {
            this.listIsGif.addAll(listIsGif);
        }
    }


    public void setData(List<String> strList, List<Boolean> isGifList) {
        this.list.clear();
        if(strList != null) {
            this.list.addAll(strList);
        }
        this.listIsGif.clear();

        if(isGifList != null) {
            this.listIsGif.addAll(isGifList);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public String getItem(int position) {

        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    public void setList(List<String> newList, List<Boolean> listIsGif) {
//        if (newList == null) {
//            this.list = new ArrayList<String>();
//        } else {
//            this.list = newList;
//        }
//
//        if (listIsGif == null) {
//            this.listIsGif = new ArrayList<Boolean>();
//        } else {
//            this.listIsGif = listIsGif;
//        }
//
//        notifyDataSetChanged();
//
//    }
//
//    public void addList(List<String> addList) {
//        if (this.list == null) {
//            this.list = new ArrayList<String>();
//        }
//        if (addList != null) {
//            this.list.addAll(addList);
//            notifyDataSetChanged();
//        }
//    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.comment_pics_grid_item, null);

            holder.pic = (ImageView) convertView.findViewById(R.id.iv_comments_pics_grid_item);
            holder.ivIsGif = (ImageView) convertView.findViewById(R.id.iv_pic_is_gif);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String strPath = /* list.get(position) */getItem(position);

        if (LangUtils.isNotEmpty(listIsGif) && listIsGif.size() >= 0 && listIsGif.size() > position
                && listIsGif.get(position)) {
            holder.ivIsGif.setVisibility(View.VISIBLE);
        } else
            holder.ivIsGif.setVisibility(View.GONE);

        XGlideUtils.loadImage(holder.pic, strPath);

        return convertView;
    }


    private static class ViewHolder {
        private ImageView pic;
        private ImageView ivIsGif;
    }
}
