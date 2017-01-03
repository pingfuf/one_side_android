package com.kuaipao.ui.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuaipao.base.adapter.XSimpleAdapter;
import com.kuaipao.manager.R;
import com.kuaipao.model.beans.XGym;

import java.util.List;

/**
 * Created by pingfu on 16-11-9.
 */
public class ChosenGymAdapter extends XSimpleAdapter<XGym> {
    private int mPosition;

    public ChosenGymAdapter(Context context, List<XGym> data) {
        super(context, data);
        mPosition = -1;
    }

    public void setSelectedPosition(int position) {
        mPosition = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvName = null;
        ImageView ivSelected = null;
        if(convertView == null) {
            convertView = createItemView(R.layout.ui_chosen_gym_item);
            if(convertView != null) {
                tvName = findViewById(convertView, R.id.tv_name);
                ivSelected = findViewById(convertView, R.id.iv_select);
            }
        } else {
            tvName = findViewById(convertView, R.id.tv_name);
            ivSelected = findViewById(convertView, R.id.iv_select);
        }

        if(tvName != null && ivSelected != null) {
            tvName.setText(getItem(position).name);
            if(position == mPosition) {
                tvName.setTextColor(mContext.getResources().getColor(R.color.title_red_color));
                ivSelected.setVisibility(View.VISIBLE);
            } else {
                tvName.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                ivSelected.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
