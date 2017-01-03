package com.kuaipao.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kuaipao.utils.LangUtils;
import com.kuaipao.manager.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SupportCityAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private String mSelectedCity;

    public SupportCityAdapter(Context context, String selectedCity) {
        mContext = context;
        mSelectedCity = selectedCity;
    }

    public void setList(List<String> list) {
        if (mList == null)
            mList = new ArrayList<String>();
        mList.clear();
        if (!LangUtils.isEmpty(list))
            mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return LangUtils.isEmpty(mList) ? 0 : mList.size();
    }

    @Override
    public String getItem(int position) {
        if (LangUtils.isEmpty(mList))
            return null;
        if (position < 0 || position >= mList.size())
            return null;
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.support_city_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tv_city);
            viewHolder.ivChecked = (ImageView) convertView.findViewById(R.id.iv_checked);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        if (!LangUtils.isEmpty(item)) {
            viewHolder.tvCityName.setText(item);
            if (item.equals(mSelectedCity)) {
                viewHolder.ivChecked.setVisibility(View.VISIBLE);
            } else
                viewHolder.ivChecked.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView tvCityName;
        public ImageView ivChecked;
    }

    public String getSelectedCity() {
        return mSelectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.mSelectedCity = selectedCity;
        notifyDataSetChanged();
    }

}
