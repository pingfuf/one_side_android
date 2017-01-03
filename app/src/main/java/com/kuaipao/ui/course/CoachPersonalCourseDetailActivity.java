package com.kuaipao.ui.course;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.base.net.XService;
import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.base.view.XRoundImageView;
import com.kuaipao.manager.CardManager;
import com.kuaipao.manager.CardSessionManager;
import com.kuaipao.model.beans.CoachMember;
import com.kuaipao.model.beans.CoachPersonalCard;
import com.kuaipao.model.beans.XAction;
import com.kuaipao.model.beans.XMember;
import com.kuaipao.model.event.CourseDraftChangedEvent;
import com.kuaipao.model.response.CoachCourseDetailResponse;
import com.kuaipao.ui.CustomDialog;
import com.kuaipao.ui.customer.CustomerDetailActivity;
import com.kuaipao.ui.customer.CustomerPageParam;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

/**
 * Created by ZhanTao on 3/18/16.
 */
public class CoachPersonalCourseDetailActivity extends BaseActivity {
    private static final int ADD_COURSE_PAGE = 100;
    private static final int COURSE_DETAIL_PAGE = 101;

    private LinearLayout llRightTitle;
    private ImageView ivRightTitle;

    @From(R.id.iv_coach_header)
    private XRoundImageView ivCoachHeader;

    @From(R.id.tv_coach_name)
    private TextView tvCoachName;

    @From(R.id.iv_student_header)
    private XRoundImageView ivStudentHeader;

    @From(R.id.tv_student_name)
    private TextView tvStudentName;

    @From(R.id.ll_date)
    private LinearLayout llDate;

    @From(R.id.tv_date)
    private TextView tvDate;

    @From(R.id.iv_date)
    private ImageView ivDate;

    @From(R.id.ll_content)
    private LinearLayout llContent;

    @From(R.id.ll_record_detail)
    private LinearLayout llRecordDetail;

    @From(R.id.ll_warn)
    private LinearLayout llWarn;

    @From(R.id.ll_record)
    private LinearLayout llRecord;

    @From(R.id.tv_record_mark)
    private TextView tvRecordMark;

    @From(R.id.tv_aerobic_time)
    private TextView tvAerobicTime;

    @From(R.id.tv_power)
    private TextView tvPower;

    @From(R.id.tv_combined_train)
    private TextView tvCombinedTrain;

    @From(R.id.tv_no_data)
    private TextView tvNoData;

    @From(R.id.btn_submit)
    private Button btnSubmit;

    @XAnnotation
    private CoachPersonalCourseDetailPageParam mPageParam;
    private CoachCourseDetailResponse mResponse;
    private boolean isCourseFinished;
    private boolean isCourseCanceled;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_coach_personal_course_detail);

        llRightTitle = new LinearLayout(this);
        ivRightTitle = new ImageView(this);
        int size = ViewUtils.rp(40);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llRightTitle.setPadding(0, 0, ViewUtils.rp(15), 0);
        llRightTitle.setLayoutParams(params);
        ivRightTitle.setImageResource(R.mipmap.ic_more);
        ivRightTitle.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewUtils.rp(22), ViewUtils.rp(22));
        llRightTitle.addView(ivRightTitle, params2);
        llRightTitle.setOnClickListener(this);

        setTitle("一对一私教", true, llRightTitle);
        initUI();

        isCourseFinished = false;
        isCourseCanceled = false;
        fetchCourseDetail();
        showLoadingDialog();
        EventBus.getDefault().register(this);
    }

    private void fetchCourseDetail() {
        BaseRequestParam param = new BaseRequestParam();
        param.addUrlParams(mPageParam.arrangementId);
        startRequest(XService.CoachCourseDetail, param);
    }

    private void initUI() {
        llRecord.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvNoData.setOnClickListener(this);
        llDate.setOnClickListener(this);
        ivCoachHeader.setImageUri(CardSessionManager.getInstance().getUser().avatar);
        tvCoachName.setText(CardSessionManager.getInstance().getUser().name);

        ivStudentHeader.setOnClickListener(this);

        if (mPageParam.member != null) {
            tvStudentName.setText(mPageParam.member.name);
            ivStudentHeader.setImageUri(mPageParam.member.avatar);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == llRecord) {
            if (hasDraft()) {
                gotoRecordDetailPage(CardManager.getFailedCourseDetail(), true);
            } else {
                gotoRecordDetailPage((CoachCourseDetailResponse) v.getTag(), false);
            }
        } else if (v == ivStudentHeader) {
            CustomerPageParam pageParam = new CustomerPageParam();
            if (mPageParam.member != null) {
                pageParam.headerUrl = mPageParam.member.avatar;
                pageParam.name = mPageParam.member.name;
                pageParam.memberId = mPageParam.member.id;
            }
            xStartActivity(CustomerDetailActivity.class, pageParam);
        } else if (v == tvNoData) {
            if (hasDraft()) {
                gotoRecordDetailPage(CardManager.getFailedCourseDetail(), true);
            } else {
                gotoRecordDetailPage((CoachCourseDetailResponse) v.getTag(), false);
            }
        } else if (v == llRightTitle) {
            showMoreDialog();
        } else if (v == llDate) {
            if (mResponse == null) {
                return;
            }

            CoachAddCourseActivity.AddCoursePageParam pageParam = new CoachAddCourseActivity.AddCoursePageParam();
            pageParam.type = CoachAddCourseActivity.UPDATE_TYPE;
            pageParam.arrangementId = mResponse.id;
            pageParam.date = mResponse.startTime;
            CoachMember member = new CoachMember();
            member.setUser(mResponse.member);
            pageParam.mChosenMember = member;
            CoachPersonalCard coachPersonalCard = new CoachPersonalCard();
            coachPersonalCard.id = mResponse.order.id;
            coachPersonalCard.name = mResponse.order.course.name;
            coachPersonalCard.course = mResponse.order.course;
            pageParam.mChosenOrder = coachPersonalCard;

            xStartActivity(CoachAddCourseActivity.class, pageParam, ADD_COURSE_PAGE);
        } else if (v == btnSubmit) {
            if (mResponse == null || mResponse.order == null) {
                return;
            }

            CustomDialog dialog = new CustomDialog(this);
            int remaining = mResponse.order.remaining;
            if (remaining > 0) {
                remaining = remaining - 1;
            }
            String message = String.format("完成上课后学员课程将扣除一节，剩余%s节。确认完成？", remaining);
            dialog.setMessage(message);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishCourse();
                    dialog.dismiss();
                }
            });

            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void finishCourse() {
        if (mResponse == null) {
            return;
        }

        BaseRequestParam param = new BaseRequestParam();
        param.addParam("arrangement_id", mResponse.id);
        param.addParam("order_id", mResponse.order.id);
        param.addParam("source", 2);

        startRequest(XService.FinishCourse, param);
    }

    private void showMoreDialog() {
        final Dialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.shareDiaLogWindowAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setContentView(R.layout.ui_coach_course_detail_dialog);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteCourseDialog();
                dialog.dismiss();
            }


        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDeleteCourseDialog() {
        if (mResponse == null) {
            return;
        }
        CustomDialog dialog = new CustomDialog(this);
        String message = isCourseFinished ? "课程已经完成，不能删除" : "确定删除课程";
        dialog.setMessage(message);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isCourseFinished) {
                    dialog.dismiss();
                } else {
                    deleteCourse();
                }
            }
        });

        if (!isCourseFinished) {
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    private void deleteCourse() {
        BaseRequestParam param = new BaseRequestParam();
        param.addUrlParams(mPageParam.arrangementId);

        startRequest(XService.DeleteCourse, param);
        showLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == COURSE_DETAIL_PAGE) {
                fetchCourseDetail();
                showLoadingDialog();
            } else if (requestCode == ADD_COURSE_PAGE) {
                fetchCourseDetail();
                showLoadingDialog();
            }
        }
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        dismissLoadingDialog(500);
        if (isSameUrl(XService.CoachCourseDetail, request)) {
            ViewUtils.showToast("获取课程详情失败", Toast.LENGTH_LONG);
        } else if (isSameUrl(XService.FinishCourse, request)) {
            ViewUtils.showToast("完成上课失败", Toast.LENGTH_LONG);
        } else if (isSameUrl(XService.DeleteCourse, request)) {
            ViewUtils.showToast("删除课程失败", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        dismissLoadingDialog(500);
        if (isSameUrl(XService.CoachCourseDetail, request)) {
            ViewUtils.showToast(message, Toast.LENGTH_LONG);
        } else if (isSameUrl(XService.FinishCourse, request)) {
            ViewUtils.showToast(message, Toast.LENGTH_LONG);
        } else if (isSameUrl(XService.DeleteCourse, request)) {
            CustomDialog dialog = new CustomDialog(this);
            dialog.setMessage(message);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        dismissLoadingDialog(500);
        if (isSameUrl(XService.CoachCourseDetail, request)) {
            mResponse = (CoachCourseDetailResponse) data;
            freshUI(mResponse);
        } else if (isSameUrl(XService.FinishCourse, request)) {
            btnSubmit.setClickable(false);
            btnSubmit.setText("已经完成");
            btnSubmit.setTextColor(getResources().getColor(R.color.gray));
            ViewUtils.showToast("已完成上课", Toast.LENGTH_LONG);
            setResult(RESULT_OK);
            onBackPressed();
        } else if (isSameUrl(XService.DeleteCourse, request)) {
            ViewUtils.showToast("已删除课程", Toast.LENGTH_LONG);
            setResult(RESULT_OK);
            onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachCourseUploadChanged(CourseDraftChangedEvent event) {
        freshUI(mResponse);
    }

    private void freshUI(CoachCourseDetailResponse response) {
        if (response == null || response.startTime == null) {
            return;
        }

        tvDate.setText(LangUtils.formatDate(response.startTime, "yyyy-MM-dd HH:mm"));
        Date nowDate = new Date();
        Date noteDate = response.startTime;
        if (noteDate == null) {
            llDate.setClickable(true);
            ivDate.setVisibility(View.VISIBLE);
            setSubmitButtonStyle(false);
        } else {
            if (LangUtils.isSameDay(nowDate, noteDate) || nowDate.before(noteDate)) {
                llDate.setClickable(true);
                ivDate.setVisibility(View.VISIBLE);
            } else {
                llDate.setClickable(false);
                ivDate.setVisibility(View.GONE);
            }
        }

        //判断note内容
        if (response.note == null) {
            llRecordDetail.setVisibility(View.GONE);
            if (hasDraft()) {
                llWarn.setVisibility(View.VISIBLE);
            }
            tvRecordMark.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            llRecord.setTag(response);

            llRecordDetail.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);

            //添加数据
            int x = 0;
            int y = 0;
            tvAerobicTime.setText(response.note.remainMinutes + "分钟");
            if (response.note.actions != null) {
                x = response.note.actions.size();
                for (int i = 0; i < response.note.actions.size(); i++) {
                    XAction action = response.note.actions.get(i);
                    if (action != null && !LangUtils.isEmpty(action.items)) {
                        for (int j = 0; j < action.items.size(); j++) {
                            y = y + action.items.get(j).group;
                        }
                    }
                }
            }
            tvPower.setText(String.format("%s项（%s组）", x, y));
            tvCombinedTrain.setText(response.note.desc);

            if (hasDraft()) {
                llWarn.setVisibility(View.VISIBLE);
                tvRecordMark.setVisibility(View.GONE);
            } else {
                llWarn.setVisibility(View.GONE);
                tvRecordMark.setVisibility(View.VISIBLE);
            }
        }

        if (noteDate == null) {
            setSubmitButtonStyle(false);
        } else {
            //判断时间
            long betweenHour = (nowDate.getTime() - noteDate.getTime()) / (1000 * 60 * 60);
            if (betweenHour < 1) {
                //上课时间未到，或者上课时间未超过一小时
                setSubmitButtonStyle(false);

                //是否有剩余节数
                if (response.order != null && response.order.remaining <= 0) {
                    btnSubmit.setText("无剩余节数");
                }

                //是否退课
                if (response.order != null && response.order.status == 2) {
                    btnSubmit.setText("已退课");
                    llDate.setClickable(false);
                    ivDate.setVisibility(View.GONE);
                } else {
                    //判断record内容
                    if (response.record != null) {
                        if (response.record.status == 1) {
                            btnSubmit.setText("已经完成");
                            isCourseFinished = true;
                            llDate.setClickable(false);
                            ivDate.setVisibility(View.GONE);
                        } else if (response.record.status == 2) {
                            btnSubmit.setText("已经取消");
                            isCourseFinished = true;
                            llDate.setClickable(false);
                            ivDate.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                //
                setSubmitButtonStyle(true);

                boolean flag = true;

                //是否有剩余节数
                if (response.order != null && response.order.remaining <= 0) {
                    btnSubmit.setText("无剩余节数");
                    setSubmitButtonStyle(false);
                    flag = false;
                }

                //是否退课
                if (response.order != null && response.order.status == 2) {
                    btnSubmit.setText("已退课");
                    setSubmitButtonStyle(false);
                    llDate.setClickable(false);
                    ivDate.setVisibility(View.GONE);
                    flag = false;
                } else {
                    //判断record内容
                    if (response.record != null) {
                        if (response.record.status == 1) {
                            btnSubmit.setText("已经完成");
                            isCourseFinished = true;
                            setSubmitButtonStyle(false);
                            llDate.setClickable(false);
                            ivDate.setVisibility(View.GONE);
                            flag = false;
                        } else if (response.record.status == 2) {
                            btnSubmit.setText("已经取消");
                            isCourseFinished = true;
                            setSubmitButtonStyle(false);
                            llDate.setClickable(false);
                            ivDate.setVisibility(View.GONE);
                            flag = false;
                        }
                    }
                }

                //当满足完成上课条件的时候，判断当前教练是否有完成上课权限
                if (flag) {
                    setSubmitButtonStyle(response.recordable);
                }
            }
        }
    }

    /**
     * 跳转到训练详情页面
     */
    private void gotoRecordDetailPage(CoachCourseDetailResponse response, boolean isDraft) {
        Bundle bundle = new Bundle();
        bundle.putLong("arrangement_id", mPageParam.arrangementId);
        bundle.putLong("user_id", mPageParam.member.id);
        if (response != null) {
            bundle.putSerializable("course_detail", response);
        }

        bundle.putBoolean("from_draft", isDraft);

        xStartActivity(CoachCourseRecordDetailActivity.class, bundle, COURSE_DETAIL_PAGE);
    }

    private boolean hasDraft() {
        boolean flag = false;
        if (CardManager.getFailedCourseDetail() != null) {
            flag = CardManager.getFailedCourseDetail().id == mPageParam.arrangementId;
        }

        return flag;
    }

    /**
     * 设置提交button样式
     *
     * @param clickable 是否可点击
     */
    private void setSubmitButtonStyle(boolean clickable) {
        if (clickable) {
            btnSubmit.setClickable(true);
            btnSubmit.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_btn_submit));
            btnSubmit.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnSubmit.setBackgroundColor(getResources().getColor(R.color.gray));
            btnSubmit.setClickable(false);
            btnSubmit.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public static class CoachPersonalCourseDetailPageParam extends BasePageParam {
        public XMember member;
        public long arrangementId;
    }
}
