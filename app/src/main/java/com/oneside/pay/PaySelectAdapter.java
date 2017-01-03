package com.oneside.pay;

import static com.oneside.utils.ViewUtils.*;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.oneside.R;

/**
 * Created by Guo Ming on 05/05/15.
 */

public class PaySelectAdapter extends BaseAdapter {
    private int[][] payNameIconIds = {
            {R.drawable.pay_select_alipay, R.string.dialog_pay_name_alipay},
            {R.drawable.pay_select_wx, R.string.dialog_pay_name_wx}};

    private boolean[] payCheckStatus = {true, false};
    private Context mContext;

    public PaySelectAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return payNameIconIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getSelectedPosition() {
        for (int i = 0; i < payCheckStatus.length; i++) {
            if (payCheckStatus[i]) {
                return i;
            }
        }
        return -1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PayViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.page_list_item, null);
            holder = new PayViewHolder(convertView);
            convertView.setTag(holder);
        } else {
        }
        holder = (PayViewHolder) convertView.getTag();
        holder.setData(position);
        return convertView;
    }

    private class PayViewHolder {
        private ImageView mIconView;
        private TextView mNameView;
        private RadioButton mCheckBox;
        private View mRootView;

        public PayViewHolder(View view) {
            mIconView = find(view, R.id.pay_item_icon);
            mNameView = find(view, R.id.pay_item_name);
            mCheckBox = find(view, R.id.pay_item_checkbox);
            mCheckBox.setClickable(false);
            mRootView = view;

        }

        public void setData(int position) {
            mIconView.setImageResource(payNameIconIds[position][0]);
            mNameView.setText(payNameIconIds[position][1]);
            if (mCheckBox.isChecked() != payCheckStatus[position]) {
                mCheckBox.setChecked(payCheckStatus[position]);
            }
            mCheckBox.setTag(position);
            mRootView.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    int p = (Integer) mCheckBox.getTag();
                    if (!payCheckStatus[p]) {
                        changeCheckBox(p, !payCheckStatus[p]);
                    }
                }
            });
        }

        private void changeCheckBox(int position, boolean isChecked) {
            for (int i = 0; i < payCheckStatus.length; i++) {
                payCheckStatus[i] = (i == position ? isChecked : !isChecked);
            }
            notifyDataSetChanged();
        }
    }

}
