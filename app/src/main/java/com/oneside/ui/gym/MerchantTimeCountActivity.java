package com.oneside.ui.gym;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.net.RequestDelegate;
import com.oneside.ui.CustomDialog;
import com.oneside.ui.home.OpenDoorActivity;
import com.oneside.utils.Constant;
import com.oneside.utils.IOUtils;
import com.oneside.utils.JumpCenter;
import com.oneside.utils.LangUtils;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.base.net.UrlRequest;
import com.oneside.R;

import java.util.HashMap;

import static com.oneside.utils.ViewUtils.find;

/**
 * Created by MVEN on 16/3/21.
 */
public class MerchantTimeCountActivity extends BaseActivity {
    @From(R.id.merchant_time_count_real_time)
    private TextView timeCountTv;

    @From(R.id.tv_second)
    private TextView tvSecond;

    @From(R.id.merchant_time_count_check_out)
    private ImageView leaveMerchantImg;

    @From(R.id.merchant_time_count_check_in)
    private ImageView resumeMerchantImg;

    @From(R.id.merchant_time_count_questions)
    private TextView reOpenDoorTv;

    private static final String FETCH_NOW_USER_COUNT_TIME = "";
    private Handler stepTimeHandler;
    private Runnable mTicker;
    private long startTime = 0;
    private volatile long continuedTime = 0;
    long countTime = 0;
    private long startTimeInMerchant = -1;
    //跳转 MerchantOpenDoorActivity 传递的参数， 用来判断是否来自MerchantOpenDoorActivity界面
    private int state = 0;
    private boolean isFromHome = false;
    private long merchantID;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_merchant_time_count);
        setTitle("", true);
        mTitleBar.setBackPressedImageResource(R.drawable.gym_card_manager_close_white);

        initUI();
        initData();
    }

    private void initUI() {
        reOpenDoorTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        reOpenDoorTv.getPaint().setAntiAlias(true);
        ViewUtils.setBackgroundRoundCornerColor(tvSecond, Color.parseColor("#ff8400"), ViewUtils.rp(20));
        leaveMerchantImg.setOnClickListener(this);
        resumeMerchantImg.setOnClickListener(this);
        reOpenDoorTv.setOnClickListener(this);
    }

    @Override
    protected boolean isTitleBarOverlay() {
        return true;
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            boolean isFirstTimeSucc = b.getBoolean(Constant.INTENT_KEY_MERCHANT_TIME_COUNT_FIRST, false);
//            if (isFirstTimeSucc)
//                showSuccDialog();
            merchantID = b.getLong(Constant.INTENT_OPEN_MERCHANT_DOOR_MERHCANT_ID, -1);
            state = b.getInt(Constant.INTENT_STATE_OF_IN_OR_OUT_MERCHANT, 0);
            isFromHome = b.getBoolean(Constant.INTENT_OPEN_MERCHANT_DOOR_FROM_HOME);
        }
        continuedTime = LangUtils.parseLong(IOUtils.getPreferenceValue(merchantID + Constant.PREFERENCE_KEY_IN_MERCHANT_TOTAL_TIME), 0);
        startTimeInMerchant = LangUtils.parseLong(IOUtils.getPreferenceValue(merchantID + Constant.PREFERENCE_KEY_IN_MERCHANT_START_TIME), -1);

        startTimeCount();
    }

    private void startTimeCount() {
        timeCountTv.setText("00:00");
        tvSecond.setText("00");
        stepTimeHandler = new Handler();

        startTime = startTimeInMerchant == -1 ? System.currentTimeMillis() : startTimeInMerchant;
        mTicker = new Runnable() {
            public void run() {
//                ViewUtils.showSquareToast(LangUtils.format("curTime : %s", continuedTime), Toast.LENGTH_SHORT);
                showTimeCount(continuedTime + System.currentTimeMillis() - startTime);
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                stepTimeHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    /**
     * 离开后结束计时线程
     */
    private void endTimeCount() {
        if (stepTimeHandler != null) {
            stepTimeHandler.removeCallbacks(mTicker);
        }
    }

    public void showTimeCount(long time) {
        //if more than 3 hours; reset and finish fit
//        ViewUtils.showToast("currentTime = " + time, Toast.LENGTH_SHORT);
        if (time >= 10800000) {
            endTimeCount();
            timeCountTv.setText("03:00");
            tvSecond.setText("00");
            return;
        }
        countTime = time;
        long hourc = time / 3600000;
        String hour = "0" + hourc;

        long minuec = (time - hourc * 3600000) / (60000);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());

        timeCountTv.setText(hour + ":" + minue);
        tvSecond.setText(sec);
    }

    private void fetchNowCountTime() {
        HashMap<String, Object> params = new HashMap<>();
        UrlRequest r = new UrlRequest(FETCH_NOW_USER_COUNT_TIME, params);
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String message) {

            }

            @Override
            public void requestFinished(UrlRequest request) {

            }
        });
        r.start();
    }

    @Override
    public void onClick(View v) {
        if (v == leaveMerchantImg) {
            //结束健身，离开场馆
//            showOutMerchantDlg(0);
            leftMerchant(Constant.ACTION_OF_OPEN_DOOR_FOR_FINISH, Constant.ACTIVITY_RESULT_END_MERCHANT);
        } else if (v == resumeMerchantImg) {
            //暂时离店
//            showOutMerchantDlg(1);
            leftMerchant(Constant.ACTION_OF_OPEN_DOOR_FOR_PAUSE, Constant.ACTIVITY_RESULT_OUT_MERCHANT);
        } else if (v == reOpenDoorTv) {
            showReOpenDoorDialog();
        }
    }

    /**
     * 结束离店、暂时健身弹框
     *
     * @param type 0为结束健身, 1为暂时离店
     */
    private void showOutMerchantDlg(final int type) {
        CustomDialog.Builder b = new CustomDialog.Builder(this)
                .setPositiveButton(R.string.yes, null);
        CustomDialog d = b.create();

        String msg = "默默地记下这一刻的坚持与汗水吧!";
        if (type == 1) {
            msg = "已开启暂时离店模式,您的计时已暂停,再次开门扫码将继续开启计时";
        }
        d.setMessage(msg);
        d.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type == 1) {
                    leftMerchant(Constant.ACTION_OF_OPEN_DOOR_FOR_PAUSE, Constant.ACTIVITY_RESULT_OUT_MERCHANT);
                } else {
                    leftMerchant(Constant.ACTION_OF_OPEN_DOOR_FOR_FINISH, Constant.ACTIVITY_RESULT_END_MERCHANT);
                }
            }
        });
        d.show();
    }

    /**
     * UI
     */
    private void showSuccDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this, R.style.RoundCornerDialog).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);

        Window window = dlg.getWindow();
        View contentView = LayoutInflater.from(this).inflate(R.layout.dlg_qr_scan_succ_layout, null);

        Button btn = find(contentView, R.id.dlg_qr_succ_btn);
        TextView warn = find(contentView, R.id.dlg_qr_reopen_door);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        String warnStr = getString(R.string.open_door_fail_reopen_text);
        SpannableString span = new SpannableString(warnStr);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_net_btn)), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new UnderlineSpan(), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        warn.setText(span);

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IOUtils.removePreferenceValue(Constant.PREFERENCE_KEY_IN_MERCHANT_TOTAL_TIME);
                IOUtils.removePreferenceValue(merchantID + Constant.PREFERENCE_KEY_IN_MERCHANT_START_TIME);
                dlg.dismiss();
                setResult(Constant.ACTIVITY_RESULT_REOPEN_MERCHANT_DOOR);
                finish();
            }
        });
        window.setContentView(contentView);
        int screenHeight = SysUtils.HEIGHT;
        int screenWidth = SysUtils.WIDTH;
        if (screenHeight > 0 && screenWidth > 0) {
            dlg.getWindow().setLayout(screenWidth * 3 / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        window.setGravity(Gravity.CENTER);
    }

    private void showReOpenDoorDialog() {

        final AlertDialog dlg = new AlertDialog.Builder(this, R.style.RoundCornerDialog).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);

        Window window = dlg.getWindow();
        View contentView = LayoutInflater.from(this).inflate(R.layout.dlg_merchant_time_count_reopen_door, null);
        ImageView closeView = find(contentView, R.id.dlg_close_off_merchant_icon);
        Button btn = find(contentView, R.id.dlg_reopen_merchant_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromHome) {
                    Bundle b = new Bundle();
                    IOUtils.removePreferenceValue(merchantID + Constant.PREFERENCE_KEY_IN_MERCHANT_START_TIME);
//                    IOUtils.savePreferenceValue(merchantID + Constant.PREFERENCE_KEY_IN_MERCHANT_TOTAL_TIME, LangUtils.parseString(countTime));
                    b.putBoolean(Constant.REOPEN_DOOR_AFTER_IN_MERCHANT_TIME_COUNT, true);
                    b.putLong(Constant.INTENT_OPEN_MERCHANT_DOOR_MERHCANT_ID, merchantID);
                    JumpCenter.Jump2Activity(MerchantTimeCountActivity.this, OpenDoorActivity.class, -1, b);
                    finish();
                } else {

                    IOUtils.removePreferenceValue(merchantID + Constant.PREFERENCE_KEY_IN_MERCHANT_START_TIME);
                    setResult(Constant.ACTIVITY_RESULT_REOPEN_MERCHANT_DOOR);
                    finish();
                }
            }
        });
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        String warnStr = getString(R.string.open_door_fail_reopen_text);
        SpannableString span = new SpannableString(warnStr);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_net_btn)), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new UnderlineSpan(), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        window.setContentView(contentView);
        int screenHeight = SysUtils.HEIGHT;
        int screenWidth = SysUtils.WIDTH;
        if (screenHeight > 0 && screenWidth > 0) {
            dlg.getWindow().setLayout(screenWidth * 3 / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        window.setGravity(Gravity.CENTER);
    }


    /**
     * 离店操作
     *
     * @param status        离店的状态：暂时离店还是真的离开
     * @param requestResult request类型
     */
    private void leftMerchant(int status, int requestResult) {
        endTimeCount();
        //state==0: come from merchantOpenDoorActivity
        if (state != 0) {
            Bundle b = new Bundle();
            b.putInt(Constant.BUNDLE_JUMP_TO_OPEN_DOOR_ACTION, status);
            b.putLong(Constant.INTENT_OPEN_MERCHANT_DOOR_MERHCANT_ID, merchantID);
            JumpCenter.Jump2Activity(MerchantTimeCountActivity.this, OpenDoorActivity.class, -1, b);
        } else {
            setResult(requestResult);
        }
        MerchantTimeCountActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endTimeCount();
    }
}
