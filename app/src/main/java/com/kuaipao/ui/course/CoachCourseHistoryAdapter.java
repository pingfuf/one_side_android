package com.kuaipao.ui.course;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuaipao.base.adapter.XSimpleAdapter;
import com.kuaipao.manager.R;
import com.kuaipao.model.response.CoachCourseListResponse;
import com.kuaipao.utils.LangUtils;

import java.util.List;

/**
 * Created by pingfu on 16-11-11.
 */
public class CoachCourseHistoryAdapter extends XSimpleAdapter<CoachCourseListResponse.CoachCourseItem> {
    private OnCourseItemClickHandler mItemClickHandler;

    public CoachCourseHistoryAdapter(Context context, List<CoachCourseListResponse.CoachCourseItem> data) {
        super(context, data);
    }

    public void setItemClickHandler(OnCourseItemClickHandler itemClickHandler) {
        mItemClickHandler = itemClickHandler;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = createItemView(R.layout.ui_coach_course_history);
            if (convertView != null) {
                holder = new ViewHolder();
                holder.vLayout = findViewById(convertView, R.id.layout);
                holder.vLayout.setBackgroundColor(mContext.getResources().getColor(R.color.layout_background));
                holder.llTop = findViewById(convertView, R.id.ll_top);
                holder.llContainer = findViewById(convertView, R.id.ll_container);

                holder.tvDate = findViewById(convertView, R.id.tv_date);
                holder.tvTime = findViewById(convertView, R.id.tv_time);
                holder.tvMember = findViewById(convertView, R.id.tv_name);
                holder.tvDesc = findViewById(convertView, R.id.tv_desc);
                holder.vTop = findViewById(convertView, R.id.v_top);
                holder.ivFailedWarn = findViewById(convertView, R.id.iv_failed_warn);
                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, getItem(position), position);

        return convertView;
    }

    private void setData(ViewHolder holder, CoachCourseListResponse.CoachCourseItem item, final int position) {
        if (holder == null || item == null) {
            return;
        }

        if (item.showDate) {
            holder.llTop.setVisibility(View.VISIBLE);
            holder.vTop.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        } else {
            holder.llTop.setVisibility(View.GONE);
            holder.vTop.setVisibility(View.GONE);
        }
        holder.llTop.setVisibility(item.showDate ? View.VISIBLE : View.GONE);
        holder.tvDate.setText(LangUtils.formatAlldayTime(item.date));

        holder.tvTime.setText(item.time);
        holder.tvDate.setText(LangUtils.formatDate(item.date, "MM月dd日"));

        if (item.member != null) {
            holder.tvMember.setText(item.member.name);
        }

        holder.tvDesc.setText(item.courseName);
        holder.ivFailedWarn.setVisibility(item.isUploadFailed ? View.VISIBLE : View.GONE);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickHandler != null) {
                    mItemClickHandler.onItemClick(position);
                }
            }
        });
    }

    private static class ViewHolder {
        LinearLayout vLayout;
        LinearLayout llTop;
        LinearLayout llContainer;
        TextView tvDate;
        TextView tvTime;
        TextView tvMember;
        TextView tvDesc;
        View vTop;
        ImageView ivFailedWarn;
    }

    public interface OnCourseItemClickHandler {
        void onItemClick(int position);
    }
}
