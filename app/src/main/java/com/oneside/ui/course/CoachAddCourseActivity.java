package com.oneside.ui.course;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.InjectUtils;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.view.numpicker.XSimpleChosenTimeView;
import com.oneside.manager.CardSessionManager;
import com.oneside.R;
import com.oneside.model.beans.CoachMember;
import com.oneside.model.beans.CoachPersonalCard;
import com.oneside.model.response.CoachMemberListResponse;
import com.oneside.model.response.CoachPersonalCardsResponse;
import com.oneside.ui.CustomDialog;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 添加课程页面
 * <p/>
 * created by pingfu on 16-10-12.
 */
public class CoachAddCourseActivity extends BaseActivity {
    private static final int ITEM_HEIGHT = ViewUtils.rp(40);
    private static final int MAX_ITEM_SIZE = 7;

    public static final int ADD_TYPE = 1;

    public static final int UPDATE_TYPE = 2;

    @From(R.id.ll_time)
    private LinearLayout llTime;

    @From(R.id.tv_time)
    private TextView tvTime;

    @From(R.id.ll_name)
    private LinearLayout llName;

    @From(R.id.tv_name)
    private TextView tvName;

    @From(R.id.ll_type)
    private LinearLayout llType;

    @From(R.id.tv_type)
    private TextView tvType;

    @From(R.id.add_course_commit_btn)
    private Button commitBtn;

    private List<CoachMember> mMembers;
    private CoachMember mChosenMember;

    private List<CoachPersonalCard> mCourses;
    private CoachPersonalCard mChosenCourse;
    private Date mTime;

    private int mDay;
    private int mHour;
    private int mMinute;
    private Date mChosenDate;

    @XAnnotation
    private AddCoursePageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_course);
        InjectUtils.autoInject(this);
        setTitle("添加课程", true);

        initUI();
        fetchMembers();
        showLoadingDialog();
    }

    /**
     * 初始化每个View的UI
     */
    private void initUI() {
        llTime.setOnClickListener(this);
        llName.setOnClickListener(this);
        llType.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

        if(mPageParam.date != null) {
            mTime = mPageParam.date;
            mChosenDate = mTime;
            setMTime(mChosenDate);
            tvTime.setText(LangUtils.formatDate(mTime, "yyyy-MM-dd HH:mm"));
            tvTime.setTextColor(getResources().getColor(R.color.text_color_gray_66));
        }

        if(mPageParam.mChosenMember != null) {
            mChosenMember = mPageParam.mChosenMember;
            tvName.setText(mPageParam.mChosenMember.getUser().name);
            tvName.setTextColor(getResources().getColor(R.color.text_color_gray_66));
        }

        if(mPageParam.mChosenOrder != null) {
            mChosenCourse = mPageParam.mChosenOrder;
            tvType.setText(mChosenCourse.name);
            tvType.setTextColor(getResources().getColor(R.color.text_color_gray_66));
            fetchMemberCourses();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == commitBtn) {
            addCourse();
        } else if (v == llTime) {
            showChooseTimeDialog();
        } else if (v == llName) {
            showChooseMemberDialog(getString(R.string.item_course_member_default_desc));
        } else if (v == llType) {
            if (mChosenMember == null) {
                showNoCourseDialog("请先选择上课会员");
            } else {
                showChooseCourseDialog(getString(R.string.item_course_type_default_desc));
            }
        }
    }

    /**
     * 获取会员列表
     */
    private void fetchMembers() {
        BaseRequestParam param = new BaseRequestParam();
        param.addUrlParams(CardSessionManager.getInstance().getUser().id);
        param.addParam("has_remaining", true);
        param.addParam("page_size", 100);

        startRequest(XService.COACH_MEMBERS, param);
    }

    /**
     * 获取选择会员的课程列表
     */
    private void fetchMemberCourses() {
        BaseRequestParam param = new BaseRequestParam();
        param.addParam("user_id", CardSessionManager.getInstance().getUser().id);
        param.addParam("member_id", mChosenMember.getUser().id);
        param.addParam("has_remaining", "True");

        startRequest(XService.CoachPersonalCardList, param);
    }

    private void addCourse() {
        if (mChosenMember == null || mChosenCourse == null || mTime == null) {
            final CustomDialog.Builder b = new CustomDialog.Builder(this);
            final CustomDialog d = b.create();
            d.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    d.dismiss();
                }
            });
            d.setMessage("请填写所有项目后提交");
            d.show();
            return;
        }

        BaseRequestParam param = new BaseRequestParam();
        String time = LangUtils.formatDate(mTime, "yyyy-MM-dd HH:mm:ss");
        param.addParam("start_time", time);
        param.addParam("order_id", mChosenCourse.id);
        param.addParam("coach_id", CardSessionManager.getInstance().getUser().id);

        showLoadingDialog();

        if(mPageParam.type == ADD_TYPE) {
            startRequest(XService.AddCourse, param);
        } else {
            param.addUrlParams(mPageParam.arrangementId);
            startRequest(XService.UpdateCourse, param);
        }
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);

        dismissLoadingDialog(500);
        if (isSameUrl(XService.COACH_MEMBERS, request)) {
            showNoMemberDialog("网络异常");
        } else if (isSameUrl(XService.CoachPersonalCardList, request)) {
            showNoCourseDialog("您还没有可选会员");
        } else if (isSameUrl(XService.AddCourse, request)) {
            dealWithAddCourseFailed();
        } else if(isSameUrl(XService.UpdateCourse, request)) {
            ViewUtils.showToast("更新课程失败", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);

        dismissLoadingDialog(500);
        if (isSameUrl(XService.COACH_MEMBERS, request)) {
            showNoMemberDialog(message);
        } else if (isSameUrl(XService.CoachPersonalCardList, request)) {
            showNoCourseDialog("请先选择上课会员");
        } else if (isSameUrl(XService.AddCourse, request)) {
            dealWithAddCourseFailed();
        }else if(isSameUrl(XService.UpdateCourse, request)) {
            ViewUtils.showToast(message, Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);

        dismissLoadingDialog(500);
        if (isSameUrl(XService.COACH_MEMBERS, request)) {
            CoachMemberListResponse response = (CoachMemberListResponse) data;
            if (response == null || LangUtils.isEmpty(response.getItems())) {
                showNoMemberDialog("您还没有可选会员");
            } else {
                mMembers = response.getItems();
            }
        } else if (isSameUrl(XService.CoachPersonalCardList, request)) {
            CoachPersonalCardsResponse response = (CoachPersonalCardsResponse) data;
            if (response == null || LangUtils.isEmpty(response.items)) {
                showNoCourseDialog("您还没有可选会员");
            } else {
                mCourses = response.items;
            }
        } else if (isSameUrl(XService.AddCourse, request)) {
            ViewUtils.showToast("添加课程成功", Toast.LENGTH_LONG);
            setResult(RESULT_OK);
            onBackPressed();
        }else if(isSameUrl(XService.UpdateCourse, request)) {
            ViewUtils.showToast("更新课程成功", Toast.LENGTH_LONG);
            setResult(RESULT_OK);
            onBackPressed();
        }
    }

    /**
     * 没有会员，提示没有会员，弹出对话框，点击确认按钮后自动返回
     */
    private void showNoMemberDialog(String message) {
        CustomDialog.Builder b = new CustomDialog.Builder(this)
                .setPositiveButton(R.string.scan_confirm, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
        CustomDialog d = b.create();
        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.setMessage(message);
        d.show();
    }

    /**
     * 选中的会员没有课程信息，提示没有课程信息
     */
    private void showNoCourseDialog(String message) {
        CustomDialog.Builder b = new CustomDialog.Builder(this)
                .setPositiveButton(R.string.scan_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        CustomDialog d = b.create();
        d.setMessage(message);
        d.show();

    }

    /**
     * 添加课程失败
     */
    private void dealWithAddCourseFailed() {
        ViewUtils.showToast("添加课程失败", Toast.LENGTH_LONG);
    }


    /**
     * 选择时间弹框
     */
    private void showChooseTimeDialog() {
        final Dialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.shareDiaLogWindowAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setContentView(R.layout.ui_chosen_simple_time_dialog);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final XSimpleChosenTimeView vChosenDate = (XSimpleChosenTimeView) dialog.findViewById(R.id.v_chosen_date);


        if (mChosenDate == null) {
            mChosenDate = new Date();
        }
        setMTime(mChosenDate);

        vChosenDate.setSelectedDate(mDay, mHour, mMinute);

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenDate = vChosenDate.getValue();
                mTime = mChosenDate;
                tvTime.setText(LangUtils.formatDate(mChosenDate, "yyyy-MM-dd HH:mm"));
                tvTime.setTextColor(getResources().getColor(R.color.text_color_gray_66));
                dialog.dismiss();
            }
        });
    }

    /**
     * 选择会员弹框
     *
     * @param text 弹框标题
     */
    private void showChooseMemberDialog(String text) {
        if (LangUtils.isEmpty(mMembers)) {
            showNoMemberDialog("没有会员，不能添加课程");
            return;
        }

        List<String> items = new ArrayList<>();
        for (int i = 0; i < mMembers.size(); i++) {
            CoachMember member = mMembers.get(i);
            if (member != null) {
                items.add(member.getUser().name);
            }
        }

        showDialog(items, text, 0);
    }

    /**
     * 选择课程弹框
     *
     * @param text 弹框标题
     */
    private void showChooseCourseDialog(String text) {
        if(LangUtils.isEmpty(mCourses)) {
            showNoCourseDialog("您还没有可选会员");
            return;
        }
        List<String> items = new ArrayList<>();
        for (int i = 0; i < mCourses.size(); i++) {
            CoachPersonalCard coachPersonalCard = mCourses.get(i);
            if (coachPersonalCard != null) {
                items.add(coachPersonalCard.name);
            }
        }
        showDialog(items, text, 1);
    }

    /**
     * 底部弹框
     *
     * @param items 弹框列表数据
     * @param text  弹框标题
     * @param type  弹框类型
     */
    private void showDialog(List<String> items, String text, final int type) {
        final Dialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.shareDiaLogWindowAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setContentView(R.layout.ui_choose_gym);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView llContent = (ListView) dialog.findViewById(R.id.ll_content);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        tvTitle.setText(text);
        tvTitle.setVisibility(View.VISIBLE);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        addItems(dialog, items, llContent, type);
    }

    private void addItems(final Dialog dialog, final List<String> items, ListView llContent, final int type) {
        if (LangUtils.isEmpty(items)) {
            return;
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llContent.getLayoutParams();
        if (items.size() > MAX_ITEM_SIZE) {
            params.height = MAX_ITEM_SIZE * ITEM_HEIGHT + ViewUtils.rp(MAX_ITEM_SIZE / 2);
        }
        llContent.setLayoutParams(params);
        ArrayList<String> gymNames = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            gymNames.add(items.get(i));
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_gym_name, gymNames);
        llContent.setAdapter(adapter);
        llContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFinishing()) {
                    return;
                }

                dialog.dismiss();
                if (type == 0) {
                    boolean flag = false;
                    if(mChosenMember != null && mChosenMember.getUser().name.equals(items.get(position))) {
                        flag = true;
                    }
                    tvName.setText(items.get(position));
                    tvName.setTextColor(getResources().getColor(R.color.text_color_gray_66));
                    mChosenMember = mMembers.get(position);

                    if(!flag) {
                        showLoadingDialog();
                        mChosenCourse = null;
                        tvType.setText(getResources().getString(R.string.item_course_type_default_desc));
                        tvType.setTextColor(getResources().getColor(R.color.dark_shadow));
                        fetchMemberCourses();
                    }

                } else {
                    tvType.setText(items.get(position));
                    tvType.setTextColor(getResources().getColor(R.color.text_color_gray_66));
                    mChosenCourse = mCourses.get(position);
                }
            }
        });
    }

    private void setMTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        mDay = LangUtils.getDiscrepantDays(new Date(), date);
        if(mDay < 0) {
            mDay = 0;
        }

        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
    }

    public static class AddCoursePageParam extends BasePageParam {
        public long arrangementId;
        public int type;
        public Date date;
        public CoachMember mChosenMember;
        public CoachPersonalCard mChosenOrder;
    }
}
