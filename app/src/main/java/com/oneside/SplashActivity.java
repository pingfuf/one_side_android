package com.oneside;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.oneside.base.BaseActivity;
import com.oneside.manager.CardManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.ui.LoginActivity;
import com.oneside.ui.MainActivity;
import com.oneside.utils.Constant;
import com.oneside.utils.ViewUtils;

public class SplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);

        if (CardSessionManager.getInstance().isLogin()) {
            jumpActivity();
        } else {
            jumpActivity();
        }
        CardManager.logUmengEvent(Constant.UMENG_EVENT_OPEN);
    }

    private void jumpActivity() {
        ViewUtils.postDelayed(new Runnable() {

            public void run() {
                if (!isFinishing()) {
                    xStartActivity(MainActivity.class, LoginActivity.LOGIN_PAGE_CODE);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, 1500);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LoginActivity.LOGIN_PAGE_CODE) {
            if(resultCode == RESULT_OK ) {
                xStartActivity(MainActivity.class);
                finish();
            } else {
                finish();
            }
        }
    }
}
