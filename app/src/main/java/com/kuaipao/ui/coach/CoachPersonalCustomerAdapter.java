package com.kuaipao.ui.coach;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kuaipao.base.adapter.XSimpleAdapter;
import com.kuaipao.base.view.XRoundImageView;
import com.kuaipao.manager.R;
import com.kuaipao.model.beans.CoachMember;
import com.kuaipao.model.beans.XCustomer;
import com.kuaipao.model.beans.XMember;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.List;

/**
 * Created by pingfu on 16-9-13.
 */
public class CoachPersonalCustomerAdapter extends XSimpleAdapter<CoachMember> {
    private static final int SIZE = ViewUtils.rp(80);

    public CoachPersonalCustomerAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = createItemView(R.layout.adapter_coach_member);
            if(convertView != null) {
                holder = new ViewHolder();
                holder.tvName = findViewById(convertView, R.id.tv_name);
                holder.ivHeader = findViewById(convertView, R.id.iv_header);

                holder.tvTime = findViewById(convertView, R.id.tv_time);
                holder.vStatus = findViewById(convertView, R.id.v_status);
                holder.tvDesc = findViewById(convertView, R.id.tv_desc);

                convertView.setTag(holder);
            }
        } else {
          holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, getItem(position));

        return convertView;
    }

    private void setData(ViewHolder holder, CoachMember item) {
        if(holder == null || item == null) {
            return;
        }

        holder.ivHeader.setImageUri(item.getUser().avatar);
        holder.tvName.setText(item.getUser().name);
        holder.tvTime.setText(LangUtils.formatDate(item.getCoachService().getStartTime(),
                mContext.getString(R.string.format_no_sec)));

        float per = (float) item.getRemainCount() / (float) item.getAllCount();
        ViewGroup.LayoutParams params = holder.vStatus.getLayoutParams();
        params.width = (int)(per * SIZE);
        holder.vStatus.setLayoutParams(params);
        String desc = String.format("共%s节，剩余%s节", item.getAllCount(), item.getRemainCount());
        holder.tvDesc.setText(desc);
    }

    private static class ViewHolder {
        XRoundImageView ivHeader;
        TextView tvName;
//        TextView tvDate;
        TextView tvTime;
        View vStatus;
        TextView tvDesc;
    }
}
