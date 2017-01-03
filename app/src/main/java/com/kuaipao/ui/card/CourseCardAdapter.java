package com.kuaipao.ui.card;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuaipao.base.adapter.XSimpleAdapter;
import com.kuaipao.base.view.XRoundImageView;
import com.kuaipao.manager.R;
import com.kuaipao.model.beans.CoachPersonalCard;
import com.kuaipao.utils.LangUtils;

import java.util.List;

/**
 * Created by pingfu on 16-9-12.
 */
public class CourseCardAdapter extends XSimpleAdapter<CoachPersonalCard> {
    //卡牌未激活状态
    private static final int UNACTIVE_STATUE = 0;

    //卡牌已激活状态
    private static final int ACTIVE_STATUS = 1;

    //卡牌未已使用状态
    private static final int USED_STATUS = 2;

    //卡牌已过期状态
    private static final int EXPIRED_STATUS = 3;

    private static final int REFUND_STATUS = 9;

    public CourseCardAdapter(Context context, List<CoachPersonalCard> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = createItemView(R.layout.adapter_course_card);
            if (convertView != null) {
                holder = new ViewHolder();
                holder.llDate = findViewById(convertView, R.id.ll_date);
                holder.tvBeginDate = findViewById(convertView, R.id.tv_begin_time);
                holder.ivHeader = findViewById(convertView, R.id.iv_header);
                holder.tvName = findViewById(convertView, R.id.tv_name);
                holder.tvDescription = findViewById(convertView, R.id.tv_desc);
                holder.tvPrice = findViewById(convertView, R.id.tv_price);
                holder.tvRemainCourses = findViewById(convertView, R.id.tv_remain_times);
                holder.llBottom = findViewById(convertView, R.id.ll_bottom);
                holder.tvPriceMark = findViewById(convertView, R.id.tv_price_mark);

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, getItem(position));

        return convertView;
    }

    private void setData(ViewHolder holder, CoachPersonalCard item) {
        if(holder == null || item == null) {
            return;
        }

        holder.llDate.setVisibility(item.isShowDate ? View.VISIBLE : View.GONE);
        holder.tvBeginDate.setText(item.date);
        if(item.coach != null) {
            holder.tvName.setText(item.coach.name);
            holder.ivHeader.setImageUri(item.coach.avatar);
        } else {
            holder.ivHeader.setImageUri("");
            holder.tvName.setText("");
        }

        holder.tvDescription.setText(item.name);
        holder.tvPrice.setText(LangUtils.formatPrice(item.price));
        holder.tvRemainCourses.setText("剩余课程： " + String.format("%s节", item.remainCourses));

        switch (item.status) {
            case USED_STATUS:
            case EXPIRED_STATUS:
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.llBottom.setBackgroundColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvBeginDate.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvDescription.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvPriceMark.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.ivHeader.setGray(true);
                holder.tvRemainCourses.setText("课程已终止");
                break;
            case REFUND_STATUS:
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.llBottom.setBackgroundColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvBeginDate.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvDescription.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.tvPriceMark.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
                holder.ivHeader.setGray(true);
                break;
            case UNACTIVE_STATUE:
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_black));
                holder.llBottom.setBackgroundColor(Color.parseColor("#bd86ef"));
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.title_red_color));
                holder.tvBeginDate.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                holder.tvDescription.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                holder.tvPriceMark.setTextColor(mContext.getResources().getColor(R.color.title_red_color));
                holder.ivHeader.setGray(false);
                break;
            default:
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_black));
                holder.llBottom.setBackgroundColor(Color.parseColor("#bd86ef"));
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.title_red_color));
                holder.tvBeginDate.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                holder.tvDescription.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_66));
                holder.ivHeader.setGray(false);
                break;
        }

        if(item.remainCourses == 0) {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
            holder.llBottom.setBackgroundColor(mContext.getResources().getColor(R.color.hint_gray));
            holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
            holder.tvBeginDate.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
            holder.tvDescription.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
            holder.tvPriceMark.setTextColor(mContext.getResources().getColor(R.color.hint_gray));
            holder.ivHeader.setGray(true);
        }
    }

    private static class ViewHolder {
        LinearLayout llDate;
        TextView tvBeginDate;
        XRoundImageView ivHeader;
        TextView tvName;
        TextView tvDescription;
        TextView tvPrice;
        TextView tvRemainCourses;
        TextView tvPriceMark;
        LinearLayout llBottom;
    }
}
