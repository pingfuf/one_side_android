package com.kuaipao.ui.card;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.InjectUtils;
import com.kuaipao.utils.Constant;
import com.kuaipao.ui.view.CollapsibleTextViewMerchant;
import com.kuaipao.manager.R;

/**
 * Created by MVEN on 16/5/9.
 */
public class YearCardDescActivity extends BaseActivity {

    @From(value = R.id.year_card_desc)
    private CollapsibleTextViewMerchant cardDescTv;

    @From(value = R.id.year_exchange_desc)
    private TextView processDescTv;

    @From(value = R.id.year_card_desc_layout)
    private RelativeLayout descLayout;

    @From(value = R.id.year_card_exchange_layout)
    private RelativeLayout exchangeLayout;

    @From(value = R.id.iv_right_arrow_year_desc)
    private ImageView arrowDesc;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_year_card_desc);
        initUI();
    }

    private void initUI() {
        setTitle(getString(R.string.year_card_desc_and_exchange_pro), true);
        InjectUtils.autoInject(this);
        exchangeLayout.setOnClickListener(this);
        descLayout.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        if (b == null)
            return;
        cardDescTv.setDesc(b.get(Constant.JUMP_TO_YEAR_CARD_DESC_ACTIVITY_DESC).toString(), TextView.BufferType.NORMAL, arrowDesc);

        String[] proc = b.get(Constant.JUMP_TO_YEAR_CARD_DESC_ACTIVITY_EXCHANGE).toString().split("\n");
        String tmp = "";
        int l = proc.length;
        for (int i = 0; i < l; i++) {
            if (i == proc.length - 1) {
                tmp += "  " + proc[i];
            } else
                tmp += "  " + proc[i] + "\n";

        }
        processDescTv.setText(tmp);
    }

    @Override
    public void onClick(View v) {
        if (v == descLayout) {
            cardDescTv.performOnClick();
        }
    }
}
