package com.kuaipao.ui.view;

import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogSelectItemView extends LinearLayout {

    private ImageView mImageView;
    private TextView mNameView;

    public DialogSelectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public DialogSelectItemView(Context context) {
        super(context);
        initViews(context);
    }

    public void setImage(int bitmapId) {
        mImageView.setImageResource(bitmapId);
    }

    public void setImage(Drawable bitmap) {
        mImageView.setImageDrawable(bitmap);
    }

    public void setName(CharSequence name) {
        mNameView.setText(name);
    }

    private void initViews(Context context) {
        View.inflate(context, R.layout.dialog_select_item, this);
        mImageView = ViewUtils.find(this, R.id.dialog_select_image_view);
        mNameView = ViewUtils.find(this, R.id.dialog_select_name_view);
    }

}
