package com.kuaipao.base.view.filter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuaipao.base.adapter.XSimpleAdapter;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.InjectUtils;
import com.kuaipao.manager.R;

import java.util.List;

/**
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-10
 * Time: 14:21
 * Author: pingfu
 * FIXME
 */
public class SingleColumnListAdapter extends XSimpleAdapter<String> {
    private int currentChosenPosition;
    private int selectTextColorId;

    /**
     * 0-> 选中变灰，不选中变白
     * 1-> 不选中变灰，选中不变色
     */
    private int backgroundColorId = 0;

    public SingleColumnListAdapter(Context context) {
        super(context);
    }

    public void setChosenPosition(int p) {
        currentChosenPosition = p;
        notifyDataSetChanged();
    }

    public void setSelectTextColorId(int colorId) {
        selectTextColorId = colorId;
    }

    public void setBackgroundColorId(int colorId) {
        backgroundColorId = colorId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView itemView;
        if(convertView == null) {
            itemView = new ItemView(mContext);
        } else {
            itemView = (ItemView) convertView;
        }
        itemView.setBackgroundTpye(backgroundColorId);
        itemView.setData(getItem(position), position == currentChosenPosition, selectTextColorId);

        return itemView;
    }

    public static class ItemView extends RelativeLayout {
        @From(R.id.tv_expand_name)
        private TextView tvName;

        @From(R.id.rl_content)
        private RelativeLayout rlContent;

        private int backgroundType;

        public ItemView(Context context) {
            super(context);
            inflate(context, R.layout.filter_list_item, this);
            InjectUtils.autoInject(this);
        }

        public void setData(String name, boolean isChosen, int selectColorId) {
            tvName.setText(name);
            if (isChosen) {
                tvName.setTextColor(getResources().getColor(selectColorId));
                rlContent.setBackgroundColor(getResources().getColor(R.color.layout_background));
            } else {
                tvName.setTextColor(getResources().getColor(R.color.text_color_deep_gray));
                if(backgroundType == 0) {
                    rlContent.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        }

        public void setBackgroundTpye(int type) {
            backgroundType = type;
            if(type != 0) {
                rlContent.setBackgroundColor(getResources().getColor(R.color.layout_background));
            }
        }
    }
}
