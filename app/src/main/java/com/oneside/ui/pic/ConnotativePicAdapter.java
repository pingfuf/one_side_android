package com.oneside.ui.pic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneside.R;
import com.oneside.base.adapter.XSimpleAdapter;
import com.oneside.base.view.XRoundImageView;
import com.oneside.model.beans.ArticleSummary;

import java.util.List;

/**
 * Created by fupingfu on 2017/1/18.
 */
public class ConnotativePicAdapter extends XSimpleAdapter<ArticleSummary> {
    public ConnotativePicAdapter(Context context, List<ArticleSummary> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = createItemView(R.layout.ui_connotative_pic_item);
            if (convertView != null) {
                holder = new ViewHolder();
                holder.ivHeader = findViewById(convertView, R.id.iv_header);
                holder.tvTitle = findViewById(convertView, R.id.tv_title);
                holder.tvContent = findViewById(convertView, R.id.tv_desc);

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, getItem(position));

        return convertView;
    }

    private void setData(ViewHolder holder, ArticleSummary item) {
        if(holder == null || item == null) {
            return;
        }

        holder.tvTitle.setText(item.title);
        holder.tvContent.setText(item.desc);
    }

    private static class ViewHolder {
        XRoundImageView ivHeader;
        TextView tvTitle;
        TextView tvContent;
    }
}
