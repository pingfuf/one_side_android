package com.kuaipao.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuaipao.utils.LangUtils;
import com.kuaipao.manager.R;

public class PayPackageView extends RelativeLayout implements View.OnClickListener {

    private TextView priceView;
    private TextView iconView;
    private TextView unitView;
    private TextView titleView;
    private boolean selected;
    private Context mContext;
    private String title;

    public PayPackageView(Context context) {
        super(context);

        mContext = context;
        initView(context);
    }

    public PayPackageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.PayPackageView);
        title = tArray.getString(R.styleable.PayPackageView_ppv_title);
        tArray.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_pay_package, this);
        priceView = (TextView) findViewById(R.id.tv_price);
        iconView = (TextView) findViewById(R.id.tv_icon);
        unitView = (TextView) findViewById(R.id.tv_unit);
        titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(title);
    }

    public void setPrice(String text) {
        priceView.setText(text);
    }

    public int getPrice() {
        int price = LangUtils.intValue(priceView.getText());
        return price;
    }
  
  /*public void setTitle(String text) {
    titleView.setText(text);
  }*/

    private void setBg(int drawable) {
        this.setBackgroundResource(drawable);
    }

    private void setTextColor(int color) {
        priceView.setTextColor(color);
        iconView.setTextColor(color);
        unitView.setTextColor(color);
    }

    private void setIconTextColor(int color) {
        iconView.setTextColor(color);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            setTextColor(mContext.getResources().getColor(R.color.papaya_primary_color));
//      setIconTextColor(mContext.getResources().getColor(R.color.papaya_primary_color));
        } else {
            setTextColor(mContext.getResources().getColor(R.color.text_color_deep_gray));
        }
    }

    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
