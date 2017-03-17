package com.oneside.base.hy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.oneside.base.BaseActivity;
import com.oneside.base.CardConfig;
import com.oneside.base.inject.From;
import com.oneside.base.rn.RNConfig;
import com.oneside.base.rn.RNRootActivity;
import com.oneside.R;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;

import static com.oneside.utils.LangUtils.cc_dateByMovingToBeginningOfDay;
import static com.oneside.utils.ViewUtils.find;
import static com.oneside.utils.WebUtils.getJsonArray;
import static com.oneside.utils.WebUtils.getJsonObject;

/**
 * Created by ZhanTao on 1/14/16.
 */
public class HyConfigActivity extends BaseActivity {

    @From(R.id.cbx_mock)
    private CheckBox cbxMock;

    @From(R.id.edt_ip)
    private EditText edtIp;

    @From(R.id.btn_submit)
    private Button btnSubmit;

    @From(R.id.btn_temp)
    private Button btnTemp;

    @From(R.id.btn_test_rn)
    private Button btnTestRn;

    @From(R.id.edt_rn)
    private EditText edtRn;

    @From(R.id.btn_rn)
    private Button btnRn;

    @From(R.id.cbx_rn)
    private CheckBox cbxRn;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_fit_data_detail);

        setTitle("mock配置", true);

        cbxMock.setChecked("true".equals(IOUtils.getPreferenceValue(CardConfig.IS_MOCK)));
        edtIp.setText(IOUtils.getPreferenceValue(CardConfig.SERVER_URL));
        btnSubmit.setOnClickListener(this);
        cbxMock.setOnClickListener(this);
        btnTemp.setOnClickListener(this);
        btnTestRn.setOnClickListener(this);

        btnRn.setOnClickListener(this);
        edtRn.setText(IOUtils.getPreferenceValue(RNConfig.RN_SERVER));
        cbxRn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btnSubmit) {
            String ipStr = edtIp.getText().toString();
            if (!LangUtils.isEmpty(ipStr)) {
                String server = ipStr;
                if(!server.startsWith("http")) {
                    server = "http://" + server;
                }
                IOUtils.savePreferenceValue(CardConfig.SERVER_URL, server);
            }
        } else if (v == cbxMock) {
            String mock = cbxMock.isChecked() ? "true" : "false";
            IOUtils.savePreferenceValue(CardConfig.IS_MOCK, mock);
        } else if(v == btnTemp) {
            String url = edtIp.getText().toString() + ":8080";
            url += "/hybrid/hy/index?temp=aaa";
            startWebActivity(url, true);
        } else if(v == btnTestRn) {
            Intent intent = new Intent(this, RNRootActivity.class);
            startActivity(intent);
        } else if (v == btnRn) {
            String ip = edtIp.getText().toString();
            IOUtils.savePreferenceValue(RNConfig.RN_SERVER, ip);
        } else if (v == cbxRn) {
            RNConfig.shouldUpdate = cbxRn.isChecked();
        }
    }
}
