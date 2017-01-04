package com.oneside.ui.user;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.base.adapter.XSimpleAdapter;
import com.oneside.R;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;

import java.util.List;

/**
 * Created by pingfu on 16-9-8.
 */
public class CustomerTypeAdapter extends XSimpleAdapter<String> {
    private static final int BORDER = ViewUtils.rp(1);
    private int mSelectedPosition = -1;
    private int itemWidth;
    private int itemHeight;

    private OnItemClickHandler mClickHandler;

    public CustomerTypeAdapter(Context context, List<String> items) {
        super(context, items);
        itemWidth = (SysUtils.WIDTH - 60 * BORDER) / 3;
        itemHeight = BORDER * 55;
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();
    }

    public void setClickHandler(OnItemClickHandler onItemClickHandler) {
        mClickHandler = onItemClickHandler;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        LinearLayout llContent;
        if (convertView == null) {
            llContent = new LinearLayout(mContext);
            GridView.LayoutParams params = new AbsListView.LayoutParams(itemWidth, itemHeight);
            llContent.setLayoutParams(params);
            llContent.setGravity(Gravity.CENTER);

            textView = new TextView(mContext);
            textView.setId(R.id.auto_focus);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);

            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(itemWidth, BORDER * 40);
            llContent.addView(textView, tvParams);
        } else {
            llContent = (LinearLayout) convertView;
            textView = (TextView) llContent.findViewById(R.id.auto_focus);
        }

        int borderColor = mContext.getResources().getColor(R.color.balance_divider_line_color);
        textView.setTextColor(mContext.getResources().getColor(R.color.text_color_gray_33));
        if (position == mSelectedPosition) {
            borderColor = mContext.getResources().getColor(R.color.title_red_color);
            textView.setTextColor(borderColor);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClickHandler != null) {
                    mClickHandler.onItemClick(position);
                }
            }
        });
        ViewUtils.setViewBorder(textView, borderColor, BORDER, 4 * BORDER);
        textView.setText(getItem(position));

        return llContent;
    }

    public interface OnItemClickHandler {
        void onItemClick(int position);
    }
}
