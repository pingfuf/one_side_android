package com.kuaipao.ui.customer;

import android.os.Bundle;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.manager.R;

/**
 * Created by ZhanTao on 5/9/16.
 */
public class AddPhysicalRecordPhotoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_fit_data);
        setTitle(getString(R.string.fit_data_activity_title), true);
    }
}
