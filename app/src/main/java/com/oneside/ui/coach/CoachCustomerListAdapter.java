package com.oneside.ui.coach;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneside.base.adapter.XSimpleAdapter;
import com.oneside.base.view.XRoundImageView;
import com.oneside.R;
import com.oneside.model.beans.XCustomer;
import com.oneside.utils.Constant;
import com.oneside.utils.LangUtils;
import com.oneside.utils.WebUtils;

import java.util.List;

/**
 * 教练会员列表adapter
 * <p/>
 * Created by pingfu on 16-9-1.
 */
public class CoachCustomerListAdapter extends XSimpleAdapter<XCustomer> {
    private static final int ST_NORMAL = 0;
    private static final int ST_ACTIVE = 1; // 激活状态ad
    private static final int ST_STOP = 2;
    private static final int ST_EXPIRED = 3;
    private static final int ST_REFUND = 9;
    private static final int T_IMPORT = 11;

    public CoachCustomerListAdapter(Context context, List<XCustomer> customers) {
        super(context, customers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.ui_coach_students_item, null);
            holder = new ViewHolder();
            holder.ivHeader = (XRoundImageView) view.findViewById(R.id.iv_header);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
            holder.ivStatus = (ImageView) view.findViewById(R.id.iv_status);
            holder.tvStatus = (TextView) view.findViewById(R.id.tv_status);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setData(holder, getItem(position));

        return view;
    }

    private void setData(ViewHolder holder, XCustomer item) {
        if (item == null || holder == null) {
            return;
        }

        holder.ivHeader.setImageUri(item.avatar);
        if(LangUtils.isEmpty(item.name)) {
            holder.tvName.setText("小熊会员");
        } else {
            holder.tvName.setText(item.name);
        }
        holder.tvTime.setText(LangUtils.formatDate(item.insertTime, mContext.getString(R.string.format_no_sec)));

        if (item.membershipCard == null) {
            holder.ivStatus.setImageResource(R.mipmap.ic_green_label);
            holder.tvStatus.setText("访客");
            holder.tvStatus.setTextColor(Color.parseColor("#80d33d"));
        } else {
            int status = WebUtils.getJsonInt(item.membershipCard, "status");
            switch (status) {
                case ST_NORMAL:
                case ST_ACTIVE:
                case ST_STOP:
                    if (item.mcCount > 1) {
                        //需要续费
                        holder.ivStatus.setImageResource(R.mipmap.ic_orange_label);
                        holder.tvStatus.setText("续费");
                        holder.tvStatus.setTextColor(Color.parseColor("#fd6e37"));
                    } else {
                        //会员
                        holder.ivStatus.setImageResource(R.mipmap.ic_yellow_label);
                        holder.tvStatus.setText("会员");
                        holder.tvStatus.setTextColor(Color.parseColor("#ffb72c"));
                    }
                    break;
                case ST_EXPIRED:
                case ST_REFUND:
                    //过期了
                    holder.ivStatus.setImageResource(R.mipmap.ic_gray_label);
                    holder.tvStatus.setTextColor(Color.parseColor("#bbbbbb"));
                    holder.tvStatus.setText("过期");
                    break;

                default:
                    //过期了
                    holder.ivStatus.setImageResource(R.mipmap.ic_gray_label);
                    holder.tvStatus.setText("过期");
                    holder.tvStatus.setTextColor(Color.parseColor("#bbbbbb"));
                    break;
            }
        }
    }

    private static class ViewHolder {
        XRoundImageView ivHeader;
        TextView tvName;
        TextView tvTime;
        ImageView ivStatus;
        TextView tvStatus;
    }
}
