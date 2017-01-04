package com.oneside.ui.user;

import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.oneside.base.BaseActivity;
import com.oneside.model.CardUser;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;
import com.oneside.utils.WebUtils;
import com.oneside.R;

public class UserInfoActivity extends BaseActivity {

    private CardUser mCardUser;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_customer_type);
        setTitle(getString(R.string.user_info_title), true);
        initUI();

        String localString = IOUtils.getPreferenceValue(CardUser.JSON_DATA);
        if (LangUtils.isNotEmpty(localString)) {
            JSONObject data = WebUtils.parseJsonObject(localString);
            mCardUser = CardUser.getCardUserByJson(data);
        }

        if (mCardUser == null) {
            finish();
            return;
        }
    }

    private void initUI() {

    }
}
