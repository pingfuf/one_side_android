package com.oneside.hy;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.base.CardConfig;
import com.oneside.base.inject.From;
import com.oneside.ui.view.CircularProgressView;
import com.oneside.R;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;

import java.util.Date;

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
        }
    }
}
