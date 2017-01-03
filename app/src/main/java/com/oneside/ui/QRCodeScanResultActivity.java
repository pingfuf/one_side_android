package com.oneside.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.base.BaseActivity;
import com.oneside.manager.CardDataManager;
import com.oneside.manager.CardDataManager.DataResultListener;
import com.oneside.manager.CardSessionManager;
import com.oneside.manager.CardSessionManager.NetworkStatus;
import com.oneside.model.CardOrder;
import com.oneside.model.event.QRScanResultEvent;
import com.oneside.utils.Constant;
import com.oneside.utils.JumpCenter;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QRCodeScanResultActivity extends BaseActivity {

    private String mStrScanResult;
    private CardOrder mCurrentOrder;

    private ImageView ivCheckImg;
    private TextView tvCheckResult;
    private TextView tvCheckResultTip;

    private RelativeLayout layoutSuccess;
    private TextView tvSuccessCode;
    private TextView tvSuccessCourse;
    private TextView tvSuccessTime;
    private TextView tvSuccessMerchant;

    private RelativeLayout layoutSuggestion;

    private LinearLayout layoutHorizontalButtons;
    private TextView tvButtonLeft;
    private TextView tvButtonRight;

    private LinearLayout layoutVerticalButtons;
    //  private TextView tvButtonTop;
    private TextView tvButtonBottom;
    private TextView tvBottomPhoneTip;

    private volatile boolean bSuccess;

    private static final int FAIL_CODE_0 = 0;
    private static final int FAIL_CODE_1 = 1;
    private static final int FAIL_CODE_NETWORK = 2;
    private volatile int mFailCode = -1;

    private volatile String mStrFailTip;
    String alert_html;//网页内容
    //                            i.putExtra(Constant.GIFT_XX_COURSE_ALERT_HTML, alert_html);
    String alert_proportion;
//                        i.putExtra(Constant.GIFT_XX_COURSE_ALERT_PROPORTION, alert_proportion);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan_result);
        setTitle(getString(R.string.scan_title), true);
        initUI();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mStrScanResult = bundle.getString(Constant.SCAN_QR_CODE_RESULT);
                mCurrentOrder = (CardOrder) bundle.getSerializable(Constant.SCAN_QR_CODE_ORDER);
//        ViewUtils.showToast(mStrScanResult, Toast.LENGTH_LONG);
            }
        }
        updateUI();
        checkinScanCode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissLoadingDialog();
    }

    private void initUI() {
        ivCheckImg = (ImageView) findViewById(R.id.iv_check_img);
        tvCheckResult = (TextView) findViewById(R.id.tv_check_result);
        tvCheckResultTip = (TextView) findViewById(R.id.tv_check_result_tip);

        layoutSuccess = (RelativeLayout) findViewById(R.id.layout_success);
        tvSuccessCode = (TextView) findViewById(R.id.tv_success_code);
        tvSuccessCourse = (TextView) findViewById(R.id.tv_success_course);
        tvSuccessTime = (TextView) findViewById(R.id.tv_success_time);
        tvSuccessMerchant = (TextView) findViewById(R.id.tv_success_merchant);

        layoutSuggestion = (RelativeLayout) findViewById(R.id.layout_suggest);

        layoutHorizontalButtons = (LinearLayout) findViewById(R.id.layout_horizontal_buttons);
        tvButtonLeft = (TextView) findViewById(R.id.tv_horizontal_button_left);
        tvButtonLeft.setOnClickListener(this);
        tvButtonRight = (TextView) findViewById(R.id.tv_horizontal_button_right);
        tvButtonRight.setOnClickListener(this);

        layoutVerticalButtons = (LinearLayout) findViewById(R.id.layout_vertical_buttons);
//    tvButtonTop = (TextView) findViewById(R.id.tv_vertical_button_top);
//    tvButtonTop.setOnClickListener(this);
        tvButtonBottom = (TextView) findViewById(R.id.tv_vertical_button_bottom);
        tvButtonBottom.setOnClickListener(this);
        tvBottomPhoneTip = (TextView) findViewById(R.id.tv_phone_tip);

    }

    private void updateUI() {
        ivCheckImg.setVisibility(View.VISIBLE);
        if (bSuccess) {
            ivCheckImg.setImageResource(R.drawable.image_success_icon);
            tvCheckResult.setText(R.string.scan_success_tip);
            QRScanResultEvent event = new QRScanResultEvent(true);
            event.setOrderID(mCurrentOrder.getOrderID());
            EventBus.getDefault().post(event);
            tvCheckResult.setTextColor(getResources().getColor(R.color.green));
            tvCheckResultTip.setVisibility(View.GONE);

            layoutSuccess.setVisibility(View.VISIBLE);
            if (mCurrentOrder != null) {
                tvSuccessCode.setText(mCurrentOrder.getOrderCode());
                tvSuccessCourse.setText(mCurrentOrder.getClassName());

                Date startTime = mCurrentOrder.getStartTime();
                Date endTime = mCurrentOrder.getEndTime();
                if (startTime != null && endTime != null) {
                    SimpleDateFormat dateFormat =
                            new SimpleDateFormat(getString(R.string.format_ignore_year_month_hour),
                                    Locale.getDefault());
                    StringBuilder sbTime = new StringBuilder(dateFormat.format(startTime));

                    SimpleDateFormat timeFormat =
                            new SimpleDateFormat(getString(R.string.fomat_ignore_year_month_day_week),
                                    Locale.getDefault());
                    sbTime.append(" ");
                    sbTime.append(timeFormat.format(startTime));
                    sbTime.append("-");
                    sbTime.append(timeFormat.format(endTime));
                    tvSuccessTime.setText(sbTime.toString());
                } else
                    tvSuccessTime.setText(" ");
                tvSuccessMerchant.setText(mCurrentOrder.getMerchantName());
            }

            layoutSuggestion.setVisibility(View.GONE);
            layoutHorizontalButtons.setVisibility(View.GONE);
            layoutVerticalButtons.setVisibility(View.VISIBLE);
//      tvButtonTop.setVisibility(View.GONE);
            tvButtonBottom.setVisibility(View.VISIBLE);
            tvBottomPhoneTip.setVisibility(View.GONE);
        } else {
            ivCheckImg.setImageResource(R.drawable.ic_fail_icon);
            tvCheckResult.setText(R.string.scan_fail_tip);
            tvCheckResult.setTextColor(getResources().getColor(R.color.gray));
            tvCheckResultTip.setVisibility(View.VISIBLE);
            tvCheckResultTip
                    .setText(LangUtils.isEmpty(mStrFailTip) ? getString(R.string.scan_fail_network)
                            : mStrFailTip);
            layoutSuccess.setVisibility(View.GONE);

            if (mFailCode == FAIL_CODE_NETWORK) {
                layoutSuggestion.setVisibility(View.VISIBLE);
                layoutHorizontalButtons.setVisibility(View.GONE);
                layoutVerticalButtons.setVisibility(View.GONE);
            } else if (mFailCode == FAIL_CODE_1) {
                layoutSuggestion.setVisibility(View.VISIBLE);
                layoutHorizontalButtons.setVisibility(View.VISIBLE);
                layoutVerticalButtons.setVisibility(View.GONE);
            } else if (mFailCode == FAIL_CODE_0) {
                layoutSuggestion.setVisibility(View.GONE);
                layoutHorizontalButtons.setVisibility(View.GONE);

                layoutVerticalButtons.setVisibility(View.VISIBLE);
                tvBottomPhoneTip.setVisibility(View.VISIBLE);
//        tvButtonTop.setVisibility(View.VISIBLE);
                tvButtonBottom.setVisibility(View.VISIBLE);
            } else {
                ivCheckImg.setVisibility(View.GONE);
                tvCheckResultTip.setText("");
                tvCheckResult.setText("");
                layoutSuggestion.setVisibility(View.GONE);
                layoutHorizontalButtons.setVisibility(View.GONE);
                layoutVerticalButtons.setVisibility(View.GONE);
            }
        }
    }

    private void checkinScanCode() {
        if (CardSessionManager.getInstance().getNetworkStatus() == NetworkStatus.OffLine) {
            ViewUtils.showToast(getString(R.string.no_network_warn), Toast.LENGTH_SHORT);
            return;
        }
        showLoadingDialog(getString(R.string.scaning_tip));
        CardDataManager.checkinScanCode(mCurrentOrder.getOrderCode(), mStrScanResult,
                new DataResultListener() {
                    @Override
                    public void onFinish(boolean ret, Object... params) {

                        LogUtils.d(">>>> ret=%s", ret);
                        if (params != null)
                            LogUtils.d(">>>> params.length=%s", params.length);
                        if (ret) {// success
                            bSuccess = true;
                            alert_html = (String) params[0];
                            alert_proportion = (String) params[1];
                        } else {// failed
                            bSuccess = false;
                            if (params != null && params.length > 0) {
                                mStrFailTip = (String) params[0];
                                mFailCode = (Integer) params[1];
                                LogUtils.d(">>>> mStrFailTip=%s, mFailCode=%s", mStrFailTip, mFailCode);
                            } else {// unknow reason
                                mFailCode = FAIL_CODE_NETWORK;
                                mStrFailTip = "";
                            }
                        }
                        ViewUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                                updateUI();
                            }
                        });
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(tvButtonLeft)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.SINGLE_CARD_CLASS_ID, mCurrentOrder.getClassID());
        } else if (v.equals(tvButtonRight)) {
            Intent intent =
                    new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                            + QRCodeScanResultActivity.this.getResources().getString(
                            R.string.official_phone_number)));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
//    else if (v.equals(tvButtonTop)) {
//      Bundle b = new Bundle();
//      b.putSerializable(Constant.SCAN_QR_CODE_ORDER, mCurrentOrder);
//      JumpCenter.Jump2Activity(this, QRCodeScanningActivity.class, -1, b);
//      QRCodeScanResultActivity.this.finish();
//    } 
        else if (v.equals(tvButtonBottom)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.REGISTER_SUCC_CODE, bSuccess);
            bundle.putSerializable(Constant.SCAN_QR_CODE_ORDER, mCurrentOrder);
            bundle.putString(Constant.GIFT_XX_COURSE_ALERT_HTML, alert_html);
            bundle.putString(Constant.GIFT_XX_COURSE_ALERT_PROPORTION, alert_proportion);
            bundle.putBoolean(Constant.EXTRA_JUMP_FROM_SCAN_OR_ORDER_RESULT, true);
            JumpCenter.Jump2Activity(QRCodeScanResultActivity.this, MainActivity.class, -1, bundle);
            QRCodeScanResultActivity.this.finish();
        }

    }
}
