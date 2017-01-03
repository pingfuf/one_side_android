package com.kuaipao.ui.course;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuaipao.base.adapter.XSimpleAdapter;
import com.kuaipao.manager.R;
import com.kuaipao.model.beans.CoachCourse;
import com.kuaipao.model.beans.XCourse;
import com.kuaipao.model.response.CoachCourseListResponse;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by pingfu on 16-9-28.
 */
public class CoachCourseAdapter extends XSimpleAdapter<CoachCourseListResponse.CoachCourseItem> {
    private OnCourseItemClickHandler mItemClickHandler;

    public CoachCourseAdapter(Context context, List<CoachCourseListResponse.CoachCourseItem> data) {
        super(context, data);
    }

    public void setItemClickHandler(OnCourseItemClickHandler itemClickHandler) {
        mItemClickHandler = itemClickHandler;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = createItemView(R.layout.adapter_coach_course);
            if (convertView != null) {
                holder = new ViewHolder();
                holder.llTop = findViewById(convertView, R.id.ll_top);
                holder.llContainer = findViewById(convertView, R.id.ll_container);

                holder.tvDate = findViewById(convertView, R.id.tv_date);
                holder.tvTime = findViewById(convertView, R.id.tv_time);
                holder.tvMember = findViewById(convertView, R.id.tv_name);
                holder.tvDesc = findViewById(convertView, R.id.tv_desc);
                holder.vTop = findViewById(convertView, R.id.v_top);
                holder.vBottom = findViewById(convertView, R.id.ll_bottom);
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

        if(item.showDate) {
            holder.llTop.setVisibility(View.VISIBLE);
            holder.vTop.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        } else {
            holder.llTop.setVisibility(View.GONE);
            holder.vTop.setVisibility(View.GONE);
        }

        Date today = new Date();
        Date next = LangUtils.dateByAddingTimeDay(today, 1);
        String date = LangUtils.formatDate(item.date, "MM月dd日");
        if(LangUtils.isSameDay(item.date, today)) {
            holder.tvDate.setText("今天  " + date);
        } else if(LangUtils.isSameDay(item.date, next)){
            holder.tvDate.setText("明天  " + date);
        } else {
            holder.tvDate.setText(date);
        }

        holder.tvTime.setText(item.time);
        if(item.member != null) {
            holder.tvMember.setText(item.member.name);
        }

        holder.vBottom.setVisibility(position == getCount() - 1 ? View.VISIBLE : View.GONE);

        holder.tvDesc.setText(item.courseName);
        holder.ivFailedWarn.setVisibility(item.isUploadFailed ? View.VISIBLE : View.GONE);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickHandler != null) {
                    mItemClickHandler.onItemClick(position);
                }
            }
        });
    }

    private static class ViewHolder {
        LinearLayout llTop;
        LinearLayout llContainer;
        TextView tvDate;
        TextView tvTime;
        TextView tvMember;
        TextView tvDesc;
        View vBottom;
        View vTop;
        ImageView ivFailedWarn;
    }

    public interface OnCourseItemClickHandler {
        void onItemClick(int position);
    }
}
