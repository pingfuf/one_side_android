package com.oneside.ui.coach;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.base.adapter.XSimpleAdapter;
import com.oneside.R;
import com.oneside.model.XReceiveRecord;

import java.util.List;

/**
 * Created by pingfu on 16-9-13.
 */
public class CoachReceiveRecordAdapter extends XSimpleAdapter<XReceiveRecord> {
    private OnAddActionHandler mAddActionHandler;

    public CoachReceiveRecordAdapter(Context context, List<XReceiveRecord> items) {
        super(context, items);
    }

    public void setAddActionHandler(OnAddActionHandler addActionHandler) {
        mAddActionHandler = addActionHandler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = createItemView(R.layout.adapter_receive_record_item);
            if(convertView != null) {
                holder = new ViewHolder();
                holder.tvName = findViewById(convertView, R.id.tv_name);
                holder.tvDate = findViewById(convertView, R.id.tv_date);
                holder.tvDesc = findViewById(convertView, R.id.tv_desc);
                holder.llAdd = findViewById(convertView, R.id.ll_add_record);
                holder.llAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mAddActionHandler != null) {
                            mAddActionHandler.addRecord();;
                        }
                    }
                });

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, getItem(position));
        if(position == getCount() - 1) {
            holder.llAdd.setVisibility(View.VISIBLE);
        } else {
            holder.llAdd.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void setData(ViewHolder holder, XReceiveRecord item) {
        if(holder == null || item == null) {
            return;
        }

        if(item.user != null) {
            holder.tvName.setText(item.user.name);
        }
        holder.tvDate.setText(item.date);
        holder.tvDesc.setText(item.description);
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvDate;
        TextView tvDesc;
        LinearLayout llAdd;
    }

    public interface OnAddActionHandler {
        void addRecord();
    }
}
