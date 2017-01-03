package com.kuaipao.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.kuaipao.utils.ViewUtils.find;

import com.kuaipao.model.UsrServiceItem;
import com.kuaipao.manager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magi on 15/12/25.
 */
public class UsrServiceItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<UsrServiceItem> mData;

    public UsrServiceItemAdapter(Context mContext, List<UsrServiceItem> mData) {
        this.mContext = mContext;
        this.mData = new ArrayList<>();
        if (null != mData) {
            this.mData.addAll(mData);
        }
    }

    public void setData(List<UsrServiceItem> mData) {
        if (null == this.mData)
            this.mData = new ArrayList<>();
        this.mData.clear();
        if (null != mData) {
            this.mData.addAll(mData);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mData)
            return 0;
        else
            return mData.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mData)
            return null;
        else
            return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.adapter_usr_service, null);
            holder = new ViewHolder();
            holder.srvIcon = find(convertView, R.id.usr_service_img);
            holder.srvName = find(convertView, R.id.usr_service_name);
            holder.topicNum = find(convertView, R.id.tv_merchant_topics_count);
            holder.topicNumLayout = find(convertView, R.id.layout_merchant_topics_count);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        UsrServiceItem item = mData.get(position);
        if (item == null)
            return convertView;
        holder.srvName.setText(item.getName());
        holder.srvIcon.setImageResource(item.getIconResourceID());
        holder.topicNumLayout.setVisibility(item.getUnreadNum() > 0 ? View.VISIBLE : View.GONE);
        holder.topicNum.setText(String.valueOf(item.getUnreadNum()));
        return convertView;
    }

    private class ViewHolder {
        ImageView srvIcon;
        TextView srvName;
        RelativeLayout topicNumLayout;
        TextView topicNum;
    }
}
