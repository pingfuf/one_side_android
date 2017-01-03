package com.oneside.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.utils.XXUtils;
import com.oneside.base.view.XImageView;
import com.oneside.base.view.XRoundImageView;
import com.oneside.model.response.MemberDetailInfoResponse;
import com.oneside.model.response.MemberRecordInfoResponse;
import com.oneside.ui.card.CustomerCardsActivity;
import com.oneside.ui.coach.AddReceiveRecordActivity;
import com.oneside.ui.coach.CoachReceiveRecordListActivity;
import com.oneside.R;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;

/**
 * 学员详情页面
 *
 * Created by MVEN on 16/7/26
 */
public class CustomerDetailActivity extends BaseActivity {
    //卡牌未激活状态
    private static final int UNACTIVE_STATUE = 0;

    //卡牌已激活状态
    private static final int ACTIVE_STATUS = 1;

    //卡牌暂停状态
    private static final int STOP_STATUS = 2;

    //卡牌已过期状态
    private static final int EXPIRED_STATUS = 3;

    //退款状态
    private static final int REFUND_STATUS = 9;

    @From(R.id.iv_top_header)
    private XImageView ivTop;

    @From(R.id.iv_header)
    private XRoundImageView ivHeader;

    @From(R.id.tv_name)
    private TextView tvName;

    @From(R.id.ll_phone)
    private LinearLayout llPhone;

    @From(R.id.tv_phone)
    private TextView tvPhone;

    @From(R.id.ll_card)
    private LinearLayout llCard;

    @From(R.id.tv_card)
    private TextView tvCard;

    @From(R.id.tv_card_end_time)
    private TextView tvCardEndTime;

    @From(R.id.tv_personal_total_course)
    private TextView tvPersonalCardTotalCount;

    @From(R.id.tv_personal_remain_course)
    private TextView tvPersonalCardRemainCount;

    @From(R.id.ll_add_record)
    private LinearLayout llAddRecord;

    @From(R.id.ll_course)
    private LinearLayout llCourse;

    @From(R.id.ll_record)
    private LinearLayout llRecord;

    @From(R.id.ll_record_content)
    private LinearLayout llRecordContent;

    @From(R.id.tv_record_num)
    private TextView tvRecordNum;

    @From(R.id.tv_coach_name)
    private TextView tvCoachName;

    @From(R.id.tv_record_date)
    private TextView tvRecordDate;

    @From(R.id.tv_record_desc)
    private TextView tvRecordDesc;

    @From(R.id.tv_record_no_data)
    private TextView tvRecordNoData;

    @From(R.id.ll_physical)
    private LinearLayout llPhysical;

    @From(R.id.tv_physical_num)
    private TextView tvPhysicalNum;

    @From(R.id.ll_physical_content)
    private LinearLayout llPhysicalContent;

    @From(R.id.tv_date)
    private TextView tvDate;

    @From(R.id.tv_weight)
    private TextView tvWeight;

    @From(R.id.tv_muscle)
    private TextView tvMuscle;

    @From(R.id.tv_fat)
    private TextView tvFat;

    @From(R.id.tv_physical_no_data)
    private TextView tvPhysicalNoData;

    @From(R.id.ll_add_physical)
    private LinearLayout llAddPhysical;

    @XAnnotation
    private CustomerPageParam mPageParam;

    private boolean isMemberDetailInfoRequestFinished;
    private boolean isMemberRecordInfoRequestFinished;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_customer_detail);
        setTitle("", true);
        mTitleBar.setBackPressedImageResource(R.drawable.ic_arrow_left_white);

        initUI();

        fetchCustomerInfo();
        fetchCustomerRecord();
        showLoadingDialog();
    }

    /**
     * 获取用户信息
     */
    private void fetchCustomerInfo() {
        BaseRequestParam param = new BaseRequestParam();
        param.addUrlParams(mPageParam.memberId);
        startRequest(XService.MemberDetailInfo, param);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.e("onNewIntent");
        fetchCustomerRecord();
        fetchCustomerInfo();
    }

    /**
     * 获取用户的体侧数据、接待数据
     */
    private void fetchCustomerRecord() {
        BaseRequestParam param = new BaseRequestParam();
        param.addUrlParams(mPageParam.memberId);
        startRequest(XService.MemberRecordInfo, param);
    }

    private void initUI() {
        llCard.setOnClickListener(this);
        llCourse.setOnClickListener(this);
        llRecord.setOnClickListener(this);
        llPhysical.setOnClickListener(this);
        llAddRecord.setOnClickListener(this);
        llAddPhysical.setOnClickListener(this);
        llPhone.setOnClickListener(this);

        ivHeader.setImageUri(mPageParam.headerUrl);
        tvName.setText(mPageParam.name);
        ivTop.setImageUri(mPageParam.headerUrl);
    }

    @Override
    protected boolean isTitleBarOverlay() {
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == llCard) {
            gotoCustomerCardsPage(0);
        } else if (v == llCourse) {
            gotoCustomerCardsPage(1);
        } else if(v == llPhone){
            XXUtils.callTelephone(tvPhone.getText().toString());
        } else if (v == llRecord) {
            xStartActivity(CoachReceiveRecordListActivity.class, mPageParam);
        } else if (v == llPhysical) {
            xStartActivity(UserPhysicalRecordActivity.class, mPageParam);
        } else if (v == llAddRecord) {
            xStartActivity(AddReceiveRecordActivity.class, mPageParam);
        } else if (v == llAddPhysical) {
            xStartActivity(AddPhysicalRecordMenuActivity.class, mPageParam);
        }
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        if(isSameUrl(XService.MemberRecordInfo, request)) {
            isMemberRecordInfoRequestFinished = true;
        } else if(isSameUrl(XService.MemberDetailInfo, request)) {
            isMemberDetailInfoRequestFinished = true;
        }

        if(isMemberDetailInfoRequestFinished && isMemberRecordInfoRequestFinished) {
            dismissLoadingDialog(500);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        if(isSameUrl(XService.MemberRecordInfo, request)) {
            isMemberRecordInfoRequestFinished = true;
        } else if(isSameUrl(XService.MemberDetailInfo, request)) {
            isMemberDetailInfoRequestFinished = true;
        }

        if(isMemberDetailInfoRequestFinished && isMemberRecordInfoRequestFinished) {
            dismissLoadingDialog(500);
        }
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        if(isSameUrl(XService.MemberRecordInfo, request)) {
            isMemberRecordInfoRequestFinished = true;
            MemberRecordInfoResponse response = (MemberRecordInfoResponse) data;
            freshMemberRecord(response);
        } else if(isSameUrl(XService.MemberDetailInfo, request)) {
            isMemberDetailInfoRequestFinished = true;
            MemberDetailInfoResponse response = (MemberDetailInfoResponse) data;
            freshMemberInfo(response);
        }

        if(isMemberDetailInfoRequestFinished && isMemberRecordInfoRequestFinished) {
            dismissLoadingDialog(500);
        }
    }

    /**
     * 刷新用户的会员卡数据
     *
     * @param response 用户信息返回结果
     */
    private void freshMemberInfo(MemberDetailInfoResponse response) {
        if(response == null) {
            llCard.setClickable(false);
            return;
        }

        tvPhone.setText(response.phone);

        tvCard.setText(String.format("会员卡（%s个）", response.membershipCardCount));

        String date;
        switch (response.status) {
            case UNACTIVE_STATUE:
                tvCardEndTime.setText("有效期至：---");
                break;
            case STOP_STATUS:
            case ACTIVE_STATUS:
                tvCardEndTime.setText("有效期至：" + LangUtils.formatAlldayTime(response.membershipCardExpiredTime));
                break;
            case EXPIRED_STATUS:
                date =  LangUtils.formatAlldayTime(response.membershipCardExpiredTime);
                tvCardEndTime.setText("已于" + date + "过期");
                break;
            case REFUND_STATUS:
                date = "";
                if(response.card != null) {
                    date = LangUtils.formatAlldayTime(response.card.refundTime);
                }
                tvCardEndTime.setText("已于" + date + "退卡");
                break;
            default:
                tvCardEndTime.setText("有效期至：" + LangUtils.formatAlldayTime(response.membershipCardExpiredTime));
                break;

        }

        if(response.membershipCardCount == 0) {
            llCard.setClickable(false);
            tvCardEndTime.setText("暂无");
        } else {
            llCard.setClickable(true);
        }

        mPageParam.gender = response.gender;
    }

    /**
     * 刷新用户的体侧记录、接待记录数据
     *
     * @param response 用户记录返回结果
     */
    private void freshMemberRecord(MemberRecordInfoResponse response) {
        if(response == null) {
            llCourse.setClickable(false);
            return;
        }

        if(response.individualCourse != null) {
            String totalText = String.format("私教课（%s节）", response.individualCourse.totalCount);
            tvPersonalCardTotalCount.setText(totalText);

            if(response.individualCourse.totalCount == 0) {
                llCourse.setClickable(false);
                tvPersonalCardRemainCount.setText("暂无");
            } else {
                String remainText = String.format("剩余%s节", response.individualCourse.remainCount);
                tvPersonalCardRemainCount.setText(remainText);
                llCourse.setClickable(true);
            }
        } else {
            llCourse.setClickable(false);
        }

        if(response.receiveRecordInfo != null) {
            tvRecordNum.setText(String.format("%s条", response.receiveRecordInfo.totalCount));

            if(response.receiveRecordInfo.totalCount == 0) {
                llRecord.setClickable(false);
            } else {
                llRecord.setClickable(true);
            }

            if(response.receiveRecordInfo.receiveRecord == null) {
                llRecordContent.setVisibility(View.GONE);
                tvRecordNoData.setVisibility(View.VISIBLE);
            } else {
                llRecordContent.setVisibility(View.VISIBLE);
                tvRecordNoData.setVisibility(View.GONE);
                if(response.receiveRecordInfo.receiveRecord.user != null) {
                    tvCoachName.setText(response.receiveRecordInfo.receiveRecord.user.name);
                }

                tvRecordDate.setText(LangUtils.formatAlldayTime(response.receiveRecordInfo.receiveRecord.date));
                tvRecordDesc.setText(response.receiveRecordInfo.receiveRecord.desc);
            }
        } else {
            llRecord.setClickable(false);
        }

        if(response.physicalExamInfo != null) {
            tvPhysicalNum.setText(String.format("%s条", response.physicalExamInfo.totalCount));

            if(response.physicalExamInfo.totalCount == 0) {
                llPhysical.setClickable(false);
            } else {
                llPhysical.setClickable(true);
            }

            if(response.physicalExamInfo.physicalExam == null) {
                llPhysicalContent.setVisibility(View.GONE);
                tvPhysicalNoData.setVisibility(View.VISIBLE);
            } else {
                llPhysicalContent.setVisibility(View.VISIBLE);
                tvPhysicalNoData.setVisibility(View.GONE);
                tvDate.setText(response.physicalExamInfo.physicalExam.date);
                tvWeight.setText("" + response.physicalExamInfo.physicalExam.weight + "kg");
                tvMuscle.setText(response.physicalExamInfo.physicalExam.muscle + "kg");
                tvFat.setText(response.physicalExamInfo.physicalExam.getShowBodyFatRate());
            }
        } else {
            llPhysical.setClickable(false);
        }
    }

    /**
     * 跳转到顾客场馆卡、私教课列表页面
     *
     * @param type 是什么类型的列表
     */
    private void gotoCustomerCardsPage(int type) {
        CustomerCardsActivity.CustomerCardPageParam pageParam = new CustomerCardsActivity.CustomerCardPageParam();
        pageParam.type = type;
        pageParam.gymId = mPageParam.gymId;
        pageParam.userId = mPageParam.userId;
        pageParam.memberId = mPageParam.memberId;
        xStartActivity(CustomerCardsActivity.class, pageParam);
    }
}
