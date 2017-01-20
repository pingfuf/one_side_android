package com.oneside;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.oneside.base.BaseActivity;
import com.oneside.manager.CardManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.ui.LoginActivity;
import com.oneside.ui.MainActivity;
import com.oneside.utils.Constant;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

import java.util.List;

public class SplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);

        jumpActivity(MainActivity.class, true);
        CardManager.logUmengEvent(Constant.UMENG_EVENT_OPEN);
    }

    private void jumpActivity(final Class<? extends BaseActivity> clazz, final boolean isFinish) {
        ViewUtils.postDelayed(new Runnable() {

            public void run() {
                if (!isFinishing()) {
                    xStartActivity(clazz, LoginActivity.LOGIN_PAGE_CODE);
                    if(isFinish) {
                        finish();
                    }
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
