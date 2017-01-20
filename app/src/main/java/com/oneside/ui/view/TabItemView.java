package com.oneside.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

public class TabItemView extends LinearLayout {
    private ImageView ivIcon;
    private TextView tvName;
    private int selectedResId;
    private int unSelectedResId;
    private boolean mSelectedState;

    public TabItemView(Context context) {
        super(context);
        init();
    }

    public TabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_tab_item, this);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvName = (TextView) findViewById(R.id.tv_name);
    }

    public void setData(int selectedResId, int unSelectedResId, CharSequence text) {
        this.selectedResId = selectedResId;
        this.unSelectedResId = unSelectedResId;
        tvName.setText(text);
        tvName.setTextColor(getResources().getColor(R.color.text_color_gray_66));

        setSelectedState(false);
    }

    public void setSelectedState(boolean isSelected) {
        mSelectedState = isSelected;
        if(isSelected) {
            ivIcon.setImageResource(selectedResId);
            tvName.setTextColor(getResources().getColor(R.color.title_red_color));
        } else {
            ivIcon.setImageResource(unSelectedResId);
            tvName.setTextColor(getResources().getColor(R.color.text_color_gray_66));
        }
    }

    public boolean getSelectedState() {
        return mSelectedState;
    }
}

