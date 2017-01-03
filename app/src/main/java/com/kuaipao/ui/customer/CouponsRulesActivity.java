package com.kuaipao.ui.customer;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.manager.R;

import android.os.Bundle;

public class CouponsRulesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_rules);
        setTitle(getString(R.string.coupon_help), true);
    }
}
