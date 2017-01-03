package com.oneside.ui.customer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.base.adapter.XSimpleAdapter;
import com.oneside.R;
import com.oneside.model.beans.XUserPhysicalRecord;

import java.util.List;

/**
 * Created by pingfu on 16-8-30.
 */
public class UserPhysicalRecordsAdapter extends XSimpleAdapter<XUserPhysicalRecord> {
    private OnAddActionHandler mAddActionHandler;

    public UserPhysicalRecordsAdapter(Context context, List<XUserPhysicalRecord> records) {
        super(context, records);
    }

    public void setAddActionHandler(OnAddActionHandler addActionHandler) {
        mAddActionHandler = addActionHandler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.adapter_physical_item, null);
            holder.llTitle = (LinearLayout) view.findViewById(R.id.ll_title);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_date);
            holder.tvMuscle = (TextView) view.findViewById(R.id.tv_muscle);
            holder.tvWeight = (TextView) view.findViewById(R.id.tv_weight);
            holder.tvFat = (TextView) view.findViewById(R.id.tv_fat);
            holder.llAddAction = findViewById(view, R.id.ll_add_action);

            holder.llAddAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mAddActionHandler != null) {
                        mAddActionHandler.addAction();
                    }
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        setData(holder, getItem(position));

        if(position < getCount() - 1) {
            holder.llAddAction.setVisibility(View.GONE);
        } else {
            holder.llAddAction.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void setData(ViewHolder holder, XUserPhysicalRecord item) {
        if(holder == null || item == null) {
            return;
        }

        holder.tvDate.setText(item.date);
        holder.tvMuscle.setText(String.format("%skg", item.muscle));
        holder.tvWeight.setText(String.format("%skg", item.weight));

        holder.tvFat.setText(item.getShowBodyFatRate());
    }

    public interface OnAddActionHandler {
        void addAction();
    }

    private static class ViewHolder {
        LinearLayout llTitle;
        TextView tvDate;
        TextView tvMuscle;
        TextView tvWeight;
        TextView tvFat;
        LinearLayout llAddAction;
    }
}
