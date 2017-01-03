package com.oneside.ui.coach;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.R;
import com.oneside.ui.MainActivity;
import com.oneside.utils.ViewUtils;

/**
 * 顾客接待成功页面
 *
 * @author pingfu 16-11-04
 */
public class CoachReceiveSuccessActivity extends BaseActivity {

    @From(R.id.btn_continue)
    private Button btnContinue;

    @From(R.id.btn_go_to_main)
    private Button btnGotoMain;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_coach_receive_success);

        btnContinue.setOnClickListener(this);
        btnGotoMain.setOnClickListener(this);

        ViewUtils.setViewBackground(btnContinue, getResources().getColor(R.color.green_open_door), ViewUtils.rp(2));
        ViewUtils.setViewBorder(btnGotoMain, getResources().getColor(R.color.line_gray), ViewUtils.rp(1), ViewUtils.rp(2));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == btnContinue) {
            setResult(RESULT_OK);
            finish();
        } else if (v == btnGotoMain) {
            xStartActivity(MainActivity.class);
        }
    }
}
