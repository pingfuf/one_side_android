package com.kuaipao.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.kuaipao.model.BuyCardPackage;
import com.kuaipao.manager.CardManager;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

public class BuyCardPackageAdapter extends BaseAdapter {


    private Context context;
    private List<BuyCardPackage> list;


    // private int location;

    public BuyCardPackageAdapter(Context con, List<BuyCardPackage> list) {
        this.context = con;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        if (LangUtils.isNotEmpty(list)) {
            this.list.addAll(list);
        }
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public BuyCardPackage getItem(int position) {
        if (list == null) {
            return null;
        } else {

            return list.get(position);
        }
    }

    public void setItemSelected(int position) {
        for (BuyCardPackage BuyCardPackage : list) {
            BuyCardPackage.setSelected(false);
        }
        this.list.get(position).setSelected(true);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<BuyCardPackage> newList) {
        if (newList == null) {
            this.list = new ArrayList<>();
        } else {
            this.list = newList;
        }
        notifyDataSetChanged();

    }

    public void addList(List<BuyCardPackage> addList) {
        if (this.list != null) {
            this.list.addAll(addList);
            notifyDataSetChanged();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.package_item, null);
            holder.tv_package_desc = (TextView) convertView.findViewById(R.id.tv_package_desc);
            holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
            holder.layout_desc_container = (LinearLayout) convertView.findViewById(R.id.layout_desc);
            holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_checked);
            holder.layout_item = (RelativeLayout) convertView.findViewById(R.id.layout_package_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BuyCardPackage buyCardPackage = list.get(position);
        if (position == (list.size() - 1)) {
            RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) holder.layout_item.getLayoutParams();
            params.bottomMargin = ViewUtils.rp(15);
            holder.layout_item.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) holder.layout_item.getLayoutParams();
            params.bottomMargin = ViewUtils.rp(0);
            holder.layout_item.setLayoutParams(params);
        }
        String mainDesc = buyCardPackage.getTitle();

        if (mainDesc != null && mainDesc.contains("(")) {
            Spannable spanString = new SpannableString(mainDesc);
            spanString.setSpan(new AbsoluteSizeSpan(ViewUtils.rp(12)), mainDesc.indexOf("("), mainDesc.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            holder.tv_package_desc.setText(spanString);
        } else {
            holder.tv_package_desc.setText(mainDesc);
        }
        holder.tv_unit.setText(CardManager.getApplicationContext().getResources()
                .getString(R.string.money_unit, buyCardPackage.getPrice() / 100));
//    holder.iv_selected
//        .setImageResource(buyCardPackage.isSelected() ? R.drawable.pay_select_checkbox_pressed
//            : R.drawable.pay_select_checkbox_normal);
//    LogUtils.d("dddddddddd buyCardPackage.isSelected():%s", buyCardPackage.isSelected());
        if (buyCardPackage.isSelected()) {
            holder.iv_selected.setImageResource(R.drawable.pay_select_checkbox_pressed);
            holder.tv_unit.setTextColor(context.getResources().getColor(R.color.papaya_primary_color));
            holder.layout_item.setBackgroundResource(R.drawable.corners_list_item_white_pressed_bg);
        } else {
            holder.iv_selected.setImageResource(R.drawable.pay_select_checkbox_normal);
            holder.tv_unit.setTextColor(context.getResources().getColor(R.color.text_color_deep_gray));
            holder.layout_item.setBackgroundResource(R.drawable.corners_list_item_white_bg);
        }
//    holder.layout_desc_container.removeAllViews();
        int childCount = holder.layout_desc_container.getChildCount();
        if (childCount > 1) {
            holder.layout_desc_container.removeViews(1, childCount - 1);
        }
        if (buyCardPackage.getDesc() != null && buyCardPackage.getDesc().size() > 0) {
            ArrayList<String> descs = buyCardPackage.getDesc();
            for (String desc : descs) {

                TextView textView = new TextView(context);
                textView.setTextSize(11);
                textView.setTextColor(context.getResources().getColor(R.color.text_color_gray));
                textView.setText("· " + desc);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                holder.layout_desc_container.addView(textView, params);
            }
        }
    /*if (LangUtils.isNotEmpty(buyCardPackage.getDesc2())) {
      TextView textView = new TextView(context);
      textView.setTextSize(10);
      textView.setDefaultTextColor(context.getResources().getColor(R.color.text_color_gray));
      textView.setText("· " + buyCardPackage.getDesc2());
    if (LangUtils.isNotEmpty(buyCardPackage.getDesc())) {
      
      TextView textView = new TextView(context);
      textView.setTextSize(10);
      textView.setText("· " + buyCardPackage.getDesc().get(0));
      LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      holder.layout_desc_container.addView(textView, params);
    }
    if (LangUtils.isNotEmpty(buyCardPackage.getDesc()) && buyCardPackage.getDesc().size() > 1) {
      TextView textView = new TextView(context);
      textView.setTextSize(10);
      textView.setText("· " + buyCardPackage.getDesc().get(1));
      LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      params.topMargin = ViewUtils.rp(3);
      holder.layout_desc_container.addView(textView, params);
    }*/

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_package_desc;
        private TextView tv_unit;
        private LinearLayout layout_desc_container;
        private ImageView iv_selected;
        private RelativeLayout layout_item;
    }

}
