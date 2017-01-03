package com.kuaipao.ui.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuaipao.base.adapter.XSimpleAdapter;
import com.kuaipao.manager.R;
import com.kuaipao.model.beans.MembershipCard;
import com.kuaipao.utils.LangUtils;

import java.util.List;

/**
 * Created by pingfu on 16-9-9.
 */
public class MembershipCardAdapter extends XSimpleAdapter<MembershipCard> {
    //卡牌未激活状态
    private static final int UNACTIVE_STATUE = 0;

    //卡牌已激活状态
    private static final int ACTIVE_STATUS = 1;

    //卡牌未已使用状态
    private static final int STOP_STATUS = 2;

    //卡牌已过期状态
    private static final int EXPIRED_STATUS = 3;

    private static final int REFUND_STATUS = 9;

    public MembershipCardAdapter(Context context, List<MembershipCard> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = createItemView(R.layout.adapter_membership_card);
            if(convertView == null) {
                return null;
            }

            holder = new ViewHolder();

            holder.llDate = findViewById(convertView, R.id.ll_date);
            holder.tvDate = findViewById(convertView, R.id.tv_date);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvBeginTime = (TextView) convertView.findViewById(R.id.tv_begin_time);
            holder.tvEndTime = (TextView) convertView.findViewById(R.id.tv_end_time);
            holder.tvPriceMark = findViewById(convertView, R.id.tv_price_mark);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.vBottom = findViewById(convertView, R.id.view_bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, getItem(position));

        return convertView;
    }

    private void setData(ViewHolder holder, MembershipCard item) {
        if(holder == null || item == null) {
            return;
        }

        holder.llDate.setVisibility(item.isShowDate ? View.VISIBLE : View.GONE);
        holder.tvDate.setText(item.date);
        holder.tvName.setText(item.name);
        holder.tvBeginTime.setText(item.beginTime);
        holder.tvPrice.setText("" + item.price);

        String endTime = item.endTime != null ? item.endTime : "---";
        switch(item.status) {
            case UNACTIVE_STATUE:
                holder.tvEndTime.setText("有效期至：---");
                break;
            case EXPIRED_STATUS:
                holder.tvEndTime.setText("已于" + endTime + "过期");
                break;
            case REFUND_STATUS:
                String refundTime = item.refundTime != null ? LangUtils.formatAlldayTime(item.refundTime) : "---";
                holder.tvEndTime.setText("已于" + refundTime + "退卡");
                break;
            default:
                holder.tvEndTime.setText("有效期至：" + endTime);
                break;
        }

        switch (item.status) {
            case EXPIRED_STATUS:
            case REFUND_STATUS:
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.vBottom.setBackgroundColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvBeginTime.setBackgroundColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvPriceMark.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvEndTime.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                break;
            case UNACTIVE_STATUE:
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_black));
                holder.vBottom.setBackgroundColor(mContext.getResources().getColor(R.color.order_orange));
                holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                holder.tvBeginTime.setBackgroundColor(mContext.getResources().getColor(R.color.order_orange));
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.title_red_color));
                holder.tvPriceMark.setTextColor(mContext.getResources().getColor(R.color.title_red_color));
                holder.tvEndTime.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                break;
            default:
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_black));
                holder.vBottom.setBackgroundColor(mContext.getResources().getColor(R.color.order_orange));
                holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                holder.tvBeginTime.setBackgroundColor(mContext.getResources().getColor(R.color.order_orange));
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.title_red_color));
                holder.tvPriceMark.setTextColor(mContext.getResources().getColor(R.color.title_red_color));
                holder.tvEndTime.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                break;
        }
    }

    private static class ViewHolder {
        LinearLayout llDate;
        TextView tvDate;
        TextView tvName;
        TextView tvBeginTime;
        TextView tvEndTime;
        TextView tvPrice;
        TextView tvPriceMark;
        View vBottom;
    }
}
