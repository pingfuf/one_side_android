package com.kuaipao.ui.customer;

import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.model.CardUser;
import com.kuaipao.manager.CardSessionManager;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;
import com.kuaipao.manager.R;

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
