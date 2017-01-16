package com.oneside.ui.story;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneside.R;
import com.oneside.base.adapter.XSimpleAdapter;
import com.oneside.model.beans.Article;
import com.oneside.model.response.JokeListResponse;

import java.util.List;

/**
 * Created by fupingfu on 2017/1/16.
 */

public class JokesAdapter extends XSimpleAdapter<JokeListResponse.JokeArticle> {
    public JokesAdapter(Context context, List<JokeListResponse.JokeArticle> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = createItemView(R.layout.ui_joke_item);
            if(convertView != null) {
                holder = new ViewHolder();
                holder.tvTitle = findViewById(convertView, R.id.tv_title);
                holder.tvContent = findViewById(convertView, R.id.tv_content);

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, getItem(position));

        return convertView;
    }

    private void setData(ViewHolder holder, JokeListResponse.JokeArticle item) {
        if(holder == null || item == null) {
            return;
        }

        holder.tvTitle.setText(item.title);
        holder.tvContent.setText(item.text);
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvContent;
    }
}
