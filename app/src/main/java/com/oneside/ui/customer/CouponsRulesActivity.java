package com.oneside.ui.customer;

import com.oneside.base.BaseActivity;
import com.oneside.R;

import android.os.Bundle;

public class CouponsRulesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_rules);
        setTitle(getString(R.string.coupon_help), true);
    }
}
