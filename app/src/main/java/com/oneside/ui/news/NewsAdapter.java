package com.oneside.ui.news;

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
 * 新闻类
 * Created by fupingfu on 2017/1/5.
 */
public class NewsAdapter extends XSimpleAdapter<ArticleSummary> {

    public NewsAdapter(Context context, List<ArticleSummary> summaries) {
        super(context, summaries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = createItemView(R.layout.ui_news_item);
            if(convertView != null) {
                holder = new ViewHolder();
                holder.ivHeader = findViewById(convertView, R.id.iv_header);
                holder.tvTitle = findViewById(convertView, R.id.tv_title);
                holder.tvDesc = findViewById(convertView, R.id.tv_desc);

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

        holder.ivHeader.setImageUri(item.img);
        holder.tvTitle.setText(item.title);
        holder.tvDesc.setText(item.desc);
    }

    private static class ViewHolder {
        XRoundImageView ivHeader;
        TextView tvTitle;
        TextView tvDesc;
    }
}
