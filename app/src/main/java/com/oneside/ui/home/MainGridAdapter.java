package com.oneside.ui.home;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneside.base.adapter.XSimpleAdapter;
import com.oneside.R;
import com.oneside.ui.MainActivity;
import com.oneside.utils.ViewUtils;

import java.util.List;

/**
 * Created by pingfu on 16-9-5.
 */
public class MainGridAdapter extends XSimpleAdapter<MainActivity.MainItem> {
    private static final int SIZE = ViewUtils.rp(50);
    private OnCoachCourseUploadFailedHandler uploadFailedHandler;

    private int mHeight = -1;

    public MainGridAdapter(Context context, List<MainActivity.MainItem> items) {
        super(context, items);
    }

    public void setUploadFailedHandler(OnCoachCourseUploadFailedHandler uploadFailedHandler) {
        this.uploadFailedHandler = uploadFailedHandler;
    }

    public void setItemHeight(int height) {
        mHeight = height;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null) {
            view = createItemView(R.layout.ui_main_grid_item);
            if(view == null) {
                return null;
            }
            holder = new ViewHolder();
            holder.rlContent = findViewById(view, R.id.rl_container);
            holder.llRemark = findViewById(view, R.id.ll_remark);
            GridView.LayoutParams params = new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            holder.llRemark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(uploadFailedHandler != null) {
                        uploadFailedHandler.uploadCoachCourseDetail();
                    }
                }
            });
            if(mHeight > 0) {
                params.height = mHeight;
            }

            holder.rlContent.setLayoutParams(params);
            holder.rlContent.setGravity(Gravity.CENTER);
            holder.rlContent.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            holder.imageView = findViewById(view, R.id.iv_header);
            holder.textView = findViewById(view, R.id.tv_name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setData(holder, getItem(position));

        return view;
    }

    private void setData(ViewHolder holder, MainActivity.MainItem item) {
        if(holder == null || item == null) {
            return;
        }

        if(item.resId > 0) {
            holder.imageView.setImageResource(item.resId);
        }

        holder.llRemark.setVisibility(item.isFailed ? View.VISIBLE : View.GONE);

        holder.textView.setText(item.name);
    }

    private static class ViewHolder {
        RelativeLayout rlContent;
        LinearLayout llRemark;
        ImageView imageView;
        TextView textView;
    }

    public interface OnCoachCourseUploadFailedHandler {
        void uploadCoachCourseDetail();
    }
}
