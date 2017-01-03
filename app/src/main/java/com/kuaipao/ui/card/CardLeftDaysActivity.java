package com.kuaipao.ui.card;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.manager.CardManager;
import com.kuaipao.manager.CardSessionManager;
import com.kuaipao.model.BuyCardOrder;
import com.kuaipao.model.CardUser;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.manager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ZhanTao on 3/18/16.
 */
public class CardLeftDaysActivity extends BaseActivity {

    private TextView tvThisWeek;
    private TextView tvThisDate;

    private TextView tvAllLeft;
    private TextView tvAllLeftDate;

    private TextView tvCurrentLeft;
    private TextView tvCurrentLeftDate;
    private boolean isFromSpecialMerchant = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_card_left_days);

        initUI();
        updateUI();
        mTitleBar.setBackPressedImageResource(R.drawable.selector_btn_back_black);
        mTitleBar.setBackgroundResource(R.color.white);
        mTitleBar.setTitleColor(getResources().getColor(R.color.black));
    }

    private void initUI() {
        tvThisWeek = (TextView) findViewById(R.id.tv_card_left_days_week);
        tvThisDate = (TextView) findViewById(R.id.tv_card_left_days_date);
        tvAllLeft = (TextView) findViewById(R.id.tv_card_left_days_all_ranges);
        tvAllLeftDate = (TextView) findViewById(R.id.tv_card_left_days_all_ranges_date);
        tvCurrentLeft = (TextView) findViewById(R.id.tv_card_left_days_current_range);
        tvCurrentLeftDate = (TextView) findViewById(R.id.tv_card_left_days_current_range_date);
    }

    private void updateUI() {

        Bundle b = getIntent().getExtras();
        if (b != null) {
            isFromSpecialMerchant = b.getBoolean(Constant.INTENT_KEY_JUMP_TO_LEFT_DAY_FROM_SPECIAL_CARD, false);
        }
        String title = getString(R.string.activity_card_left_days_title);
        if(isFromSpecialMerchant) {
            title = getString(R.string.activity_card_special_left_days_title);
        }
        setTitle(title);
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.format_year_month_day_1));

        tvThisWeek.setText(new SimpleDateFormat("EEEE").format(new Date()));

        String strCurrentDate = dateFormat.format(new Date());
        tvThisDate.setText(strCurrentDate);

        if (CardSessionManager.getInstance().isLogin()) {
            CardUser cardUser = CardManager.getCardUser();

            if (cardUser != null) {
                int allLeftDays;
                if (isFromSpecialMerchant) {
                    allLeftDays = cardUser.getO2SpaceRemainDays();
                } else
                    allLeftDays = cardUser.getRamainDays();

                if (allLeftDays < 0)
                    allLeftDays = 0;
                tvAllLeft.setText(LangUtils.parseString(allLeftDays));
                tvCurrentLeft.setText(LangUtils.parseString(allLeftDays));

                if (allLeftDays == 0) {
                    tvAllLeftDate.setVisibility(View.INVISIBLE);
                    tvCurrentLeftDate.setVisibility(View.INVISIBLE);
                } else {
                    tvAllLeftDate.setVisibility(View.VISIBLE);
                    tvCurrentLeftDate.setVisibility(View.VISIBLE);

                    tvAllLeftDate.setText(getString(R.string.activity_card_left_days_date_range,
                            dateFormat.format(isFromSpecialMerchant ? cardUser.getO2ExpiredDate() : cardUser.getExpiredDate())));

                    if (isFromSpecialMerchant) {
                        tvCurrentLeft.setText(LangUtils.parseString(cardUser.getO2CurrRemainDays()));
                        tvCurrentLeftDate.setText(getString(R.string.activity_card_left_days_date_range, dateFormat.format(LangUtils.dateByAddingTimeDay(new Date(), cardUser.getO2CurrRemainDays()))));
                        return;
                    }


                    ArrayList<BuyCardOrder> buyOrders = cardUser.getBuyOrders();
                    if (buyOrders == null)
                        return;
                    boolean bGetCurrentRangeFail = false;


                    BuyCardOrder currentBco = null;
                    if (LangUtils.isEmpty(buyOrders)) {
                        bGetCurrentRangeFail = true;
                    } else {
                        for (BuyCardOrder bco : buyOrders) {
                            if (bco.getEndTime() != null && bco.getStartTime() != null)
                                if (bco.getStartTime().before(new Date()) && !bco.getEndTime().before(new Date())) {
                                    currentBco = bco;
                                    break;
                                }
                        }
                        if (currentBco == null)
                            bGetCurrentRangeFail = true;
                    }
                    if (bGetCurrentRangeFail) {
                        tvCurrentLeft.setText(LangUtils.parseString(allLeftDays));
                        tvCurrentLeftDate.setText(tvAllLeftDate.getText().toString());
                    } else {
                        int currentLeft = LangUtils.daysBetweenDate(currentBco.getEndTime(), new Date()) + 1;
                        LogUtils.d("v3030 currentLeftDays=%s; endTime=%s", currentLeft, dateFormat.format(currentBco.getEndTime()));
                        if (currentLeft > allLeftDays)
                            currentLeft = allLeftDays;

                        tvCurrentLeft.setText(LangUtils.parseString(currentLeft));
                        tvCurrentLeftDate.setText(getString(R.string.activity_card_left_days_date_range,
                                dateFormat.format(LangUtils.dateByAddingTimeDay(currentBco.getEndTime(), 1))));
                    }
                }
            }
        }

    }
}
