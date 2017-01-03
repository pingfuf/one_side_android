package com.kuaipao.ui.adapter;

import static com.kuaipao.utils.LangUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuaipao.model.AddressMessage;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

public class AddressListAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressMessage> mLists;
    private List<String> choseAddress;
    private boolean isEdited = false;

    public AddressListAdapter(Context context, ArrayList<AddressMessage> address) {
        if (mLists == null)
            mLists = new ArrayList<AddressMessage>();
        if (isNotEmpty(address))
            for (int i = 0; i < address.size(); i++)
                if (Constant.ADDRESS_TYPE_NORMAL.equals(address.get(i).getType()))
                    mLists.add(address.get(i));
        mContext = context;
    }

    @Override
    public int getCount() {
        if (LangUtils.isEmpty(mLists))
            return 0;
        else
            return mLists.size();
    }

    @Override
    public AddressMessage getItem(int position) {
        if (LangUtils.isEmpty(mLists))
            return null;
        else
            return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setEditState(boolean isEdited) {
        this.isEdited = isEdited;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<AddressMessage> address) {
        mLists.clear();
        if (isNotEmpty(address))
            for (int i = 0; i < address.size(); i++)
                if (Constant.ADDRESS_TYPE_NORMAL.equals(address.get(i).getType()))
                    mLists.add(address.get(i));
        notifyDataSetChanged();
    }

    public void setChosenAddress(ArrayList<String> choose) {
        if (choseAddress == null)
            choseAddress = new ArrayList<String>();
        else
            choseAddress.clear();
        for (int i = 0; i < choose.size(); i++) {
            choseAddress.add(choose.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        boolean chosen = false;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.ui_me_address, null);
            holder.mAddressIcon = ViewUtils.find(convertView, R.id.me_address_img);
            holder.mDelIcon = ViewUtils.find(convertView, R.id.me_address_del);
            holder.mTypeAddressTv = ViewUtils.find(convertView, R.id.me_address_title);
            holder.mHorizontalLine = ViewUtils.find(convertView, R.id.me_address_horizontal_line);
            holder.mMainAddressTv = ViewUtils.find(convertView, R.id.me_address_main);
            holder.mDetailsAddressTv = ViewUtils.find(convertView, R.id.me_address_details);
            holder.mArrawIcon = ViewUtils.find(convertView, R.id.me_address_right_arrow);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (isEdited) {
            if (LangUtils.isNotEmpty(choseAddress))
                for (int i = 0; i < choseAddress.size(); i++) {
                    String[] l = choseAddress.get(i).split(" ");
                    if (LangUtils.parseInt(l[1]) == position) {
                        chosen = true;
                    }
                }
            else
                chosen = false;
            holder.mDelIcon.setVisibility(View.VISIBLE);
            holder.mArrawIcon.setVisibility(View.GONE);
            if (chosen) {
                holder.mDelIcon.setImageResource(R.drawable.ic_gym_select);
            } else
                holder.mDelIcon.setImageResource(R.drawable.ic_gym_normal);
        } else {
            holder.mDelIcon.setVisibility(View.GONE);
            holder.mArrawIcon.setVisibility(View.VISIBLE);
        }
        if (position == mLists.size() - 1) {
            holder.mHorizontalLine.setVisibility(View.GONE);
        } else
            holder.mHorizontalLine.setVisibility(View.VISIBLE);
        holder.mAddressIcon.setImageResource(R.drawable.ic_normal_address);
        holder.mTypeAddressTv.setText(mContext.getString(R.string.address_normal_title));
        holder.mMainAddressTv.setText(mLists.get(position).getName());
        holder.mDetailsAddressTv.setText(mLists.get(position).getAddress());

        return convertView;
    }

    private class ViewHolder {
        private ImageView mDelIcon, mAddressIcon, mArrawIcon;
        private TextView mMainAddressTv, mDetailsAddressTv, mTypeAddressTv;
        private View mHorizontalLine;
    }
}
