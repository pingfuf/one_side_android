package com.oneside.ui.adapter;

import static com.oneside.utils.ViewUtils.find;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oneside.model.BalanceMessage;
import com.oneside.utils.LangUtils;
import com.oneside.R;

public class BalanceAdapter extends BaseAdapter {

    private Context mContext;
    private List<BalanceMessage> mBalanceMsg;

    public BalanceAdapter(Context context, List<BalanceMessage> msg) {
        mContext = context;

        if (mBalanceMsg == null)
            mBalanceMsg = new ArrayList<BalanceMessage>();
        mBalanceMsg.addAll(msg);
    }

    @Override
    public int getCount() {
        if (null == mBalanceMsg)
            return 0;
        else
            return mBalanceMsg.size();
    }

    public void setData(List<BalanceMessage> data) {
        mBalanceMsg.clear();
        if (null != data) {
            mBalanceMsg.addAll(data);
        }
        notifyDataSetChanged();

    }

    @Override
    public Object getItem(int position) {
        if (LangUtils.isEmpty(mBalanceMsg) || position < 0)
            return null;
        return mBalanceMsg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_balance, null);
            holder.showDate = find(convertView, R.id.show_date);
            holder.msgPay = find(convertView, R.id.show_consume_money);
            holder.msgTime = find(convertView, R.id.show_details_time);
//            holder.msgTitle = find(convertView, R.id.show_title);
            holder.msgContent = find(convertView, R.id.show_msg);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        BalanceMessage bmPre = (BalanceMessage) getItem(position - 1);
        BalanceMessage bm = (BalanceMessage) getItem(position);
        boolean showDate = true;
        if (bm != null) {
            if (bmPre == null) {
                showDate = true;
            } else {
                showDate = !LangUtils.isSameMonth(bmPre.getInsert_time(), bm.getInsert_time());
            }

            holder.showDate.setVisibility(showDate ? View.VISIBLE : View.GONE);
            holder.showDate.setText(new SimpleDateFormat("yyyy年MM月").format(bm.getInsert_time()));

            holder.msgContent.setText(bm.getDesc());
            if (bm.getExpense() != 0 && bm.getIncome() == 0) {
                holder.msgPay.setText(String.format("- %s", bm.getExpense()));
                holder.msgPay.setTextColor(mContext.getResources().getColor(R.color.line_green));
            } else if (bm.getExpense() == 0 && bm.getIncome() != 0) {
                holder.msgPay.setText(String.format("+ %s", bm.getIncome()));
                holder.msgPay.setTextColor(mContext.getResources().getColor(R.color.papaya_primary_color));

            }
            holder.msgTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bm.getInsert_time()));
//            holder.msgTitle.setText(bm.getReason());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView showDate;
        //        TextView msgTitle;
        TextView msgContent;
        TextView msgTime;
        TextView msgPay;
    }
}
