package com.kuaipao.ui.customer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.kuaipao.base.view.numpicker.XChosenDateView;
import com.kuaipao.base.view.numpicker.XSimpleChosenDateView;
import com.kuaipao.manager.R;
import com.kuaipao.manager.XSoftKeyboardManager;
import com.kuaipao.model.beans.Gender;
import com.kuaipao.ui.CustomDialog;
import com.kuaipao.ui.customer.CustomerDetailActivity;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kuaipao.utils.ViewUtils.find;

/**
 * Created by ZhanTao on 2/16/16.
 */
public class AddPhysicalRecordDetailActivity extends BaseActivity {
    private TextView tvAdd;

    @From(R.id.tv_name)
    private TextView tvName;

    @From(R.id.edt_age)
    private EditText edtAge;

    @From(R.id.ll_male)
    private LinearLayout llMale;

    @From(R.id.iv_male)
    private ImageView ivMale;

    @From(R.id.ll_female)
    private LinearLayout llFemale;

    @From(R.id.iv_female)
    private ImageView ivFemale;

    @From(R.id.tv_phone)
    private TextView tvPhone;

    @From(R.id.tv_time)
    private TextView tvTime;

    @From(R.id.edt_height)
    private EditText edtHeight;

    @From(R.id.edt_weight)
    private EditText edtWeight;

    @From(R.id.edt_skeleton_weight)
    private EditText edtSkeleton;

    @From(R.id.edt_fat_weight)
    private EditText edtFat;

    @From(R.id.edt_water_weight)
    private EditText edtWater;

    @From(R.id.tv_weight_without_fat)
    private TextView tvWeightWithoutFat;

    @From(R.id.edt_bust)
    private EditText edtBust;

    @From(R.id.edt_waistline)
    private EditText edtWaistline;

    @From(R.id.edt_hipline)
    private EditText edtHipline;

    @From(R.id.edt_heart_rate)
    private EditText edtHeartRate;

    @From(R.id.edt_systolic_pressure)
    private EditText edtSystolicPressure;

    @From(R.id.edt_diastolic_pressure)
    private EditText edtDiastolicPressure;

    @From(R.id.tv_fat_index)
    private TextView tvFatIndex;

    @From(R.id.tv_fat_rate)
    private TextView tvFatRate;

    @From(R.id.tv_bust_rate)
    private TextView tvBustRate;

    @From(R.id.edt_score)
    private EditText edtScore;

    @From(R.id.edt_side_assessment)
    private EditText edtSideAssessment;

    @From(R.id.edt_back_assessment)
    private EditText edtBackAssessment;

    @From(R.id.edt_suggestion)
    private EditText edtSuggestion;

    @XAnnotation
    private CustomerPageParam mPageParam;

    private String mSex;
    private String mTime;

    private MTextWatcher edtAgeWatcher = new MTextWatcher(edtAge);
    private MTextWatcher edtHeightWatcher = new MTextWatcher(edtHeight);
    private MTextWatcher edtWeightWatcher = new MTextWatcher(edtWeight);
    private MTextWatcher edtFatWatcher = new MTextWatcher(edtFat);
    private MTextWatcher edtSkeletonWatcher = new MTextWatcher(edtSkeleton);
    private MTextWatcher edtWaterWatcher = new MTextWatcher(edtWater);
    private MTextWatcher edtBustWatcher = new MTextWatcher(edtBust);
    private MTextWatcher edtWaistlineWatcher = new MTextWatcher(edtWaistline);
    private MTextWatcher edtHipWatcher = new MTextWatcher(edtHipline);
    private MTextWatcher edtHeartWatcher = new MTextWatcher(edtHeartRate);
    private MTextWatcher edtSbpWatcher = new MTextWatcher(edtSystolicPressure);
    private MTextWatcher edtDbpWatcher = new MTextWatcher(edtDiastolicPressure);
    private MTextWatcher edtScoreWatcher = new MTextWatcher(edtScore);

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_physical_record_detail);

        tvAdd = ViewUtils.createTitleBarRightTextView(this, "提交 ");
        setTitle("手动添加体测数据", true, tvAdd);

        initUI();
    }

    /**
     * 这样
     */
    private void initUI() {
        if (mPageParam.gender != null) {
            mSex = mPageParam.gender;
            if(mSex.equals("m")) {
                ivMale.setImageResource(R.mipmap.ic_male);
                ivFemale.setImageResource(R.mipmap.ic_male_gray);
                llMale.setClickable(false);
                llFemale.setClickable(false);
            } else if(mSex.equals("f")) {
                ivMale.setImageResource(R.mipmap.ic_male_gray);
                ivFemale.setImageResource(R.mipmap.ic_female);
                llMale.setClickable(false);
                llFemale.setClickable(false);
            } else {
                mSex = "m";
                ivMale.setImageResource(R.mipmap.ic_male);
                ivFemale.setImageResource(R.mipmap.ic_male_gray);
                llFemale.setClickable(true);
                llMale.setClickable(true);
                llMale.setOnClickListener(this);
                llFemale.setOnClickListener(this);
            }
        } else {
            mSex = Gender.MALE.getSexName();
            llFemale.setClickable(true);
            llMale.setClickable(true);
            llMale.setOnClickListener(this);
            llFemale.setOnClickListener(this);
        }

        tvName.setText(mPageParam.name);
        tvPhone.setText(mPageParam.phone);

        tvAdd.setOnClickListener(this);
        tvTime.setOnClickListener(this);

        edtAge.addTextChangedListener(edtAgeWatcher);
        edtHeight.addTextChangedListener(edtHeightWatcher);
        edtWeight.addTextChangedListener(edtWeightWatcher);
        edtFat.addTextChangedListener(edtFatWatcher);
        edtSkeleton.addTextChangedListener(edtSkeletonWatcher);
        edtWater.addTextChangedListener(edtWaterWatcher);
        edtBust.addTextChangedListener(edtBustWatcher);
        edtWaistline.addTextChangedListener(edtWaistlineWatcher);
        edtHipline.addTextChangedListener(edtHipWatcher);
        edtHeartRate.addTextChangedListener(edtHeartWatcher);
        edtSystolicPressure.addTextChangedListener(edtSbpWatcher);
        edtDiastolicPressure.addTextChangedListener(edtDbpWatcher);
        edtScore.addTextChangedListener(edtScoreWatcher);

        if (mPageParam.record != null) {
            edtAge.setText("" + mPageParam.record.age);
            tvTime.setText(mPageParam.record.date);
            tvTime.setTextColor(getResources().getColor(R.color.text_color_gray_66));
            mTime = mPageParam.record.date;
            edtHeight.setText("" + mPageParam.record.height);
            edtWeight.setText(mPageParam.record.weight + "");
            edtFat.setText(mPageParam.record.fatRate + "");
            edtSkeleton.setText(mPageParam.record.muscle + "");
            edtWater.setText("" + mPageParam.record.water);
            setBustRate();
            setFatIndex();
            setBodyWeightWithoutFat();

            if (mPageParam.record.bust > 0) {
                edtBust.setText("" + mPageParam.record.bust);
            }

            if (mPageParam.record.waistline > 0) {
                edtWaistline.setText("" + mPageParam.record.waistline);
            }

            if (mPageParam.record.hips > 0) {
                edtHipline.setText("" + mPageParam.record.hips);
            }

            if (mPageParam.record.heartRate > 0) {
                edtHeartRate.setText("" + mPageParam.record.heartRate);
            }

            if (mPageParam.record.sbp > 0) {
                edtSystolicPressure.setText("" + mPageParam.record.sbp);
            }

            if (mPageParam.record.dbp > 0) {
                edtDiastolicPressure.setText("" + mPageParam.record.dbp);
            }

            edtScore.setText("" + mPageParam.record.score);

            if (!LangUtils.isEmpty(mPageParam.record.sideAssessment)) {
                edtSideAssessment.setText(mPageParam.record.sideAssessment);
            }

            if (!LangUtils.isEmpty(mPageParam.record.backAssessment)) {
                edtBackAssessment.setText(mPageParam.record.backAssessment);
            }

            edtSuggestion.setText(mPageParam.record.suggestion);
        } else {
            mTime = LangUtils.formatDate(new Date(), "yyyy-MM-dd");
            tvTime.setText(mTime);
            tvTime.setTextColor(getResources().getColor(R.color.text_color_gray_66));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == tvAdd) {
            addPhysicalRecord();
        } else if (v == llMale) {
            mSex = Gender.MALE.getSexName();
            ivMale.setImageResource(R.mipmap.ic_male);
            ivFemale.setImageResource(R.mipmap.ic_male_gray);
        } else if (v == llFemale) {
            mSex = Gender.FEMALE.getSexName();
            ivMale.setImageResource(R.mipmap.ic_male_gray);
            ivFemale.setImageResource(R.mipmap.ic_female);
        } else if (v == tvTime) {
            XSoftKeyboardManager.build(this, null).hideSoftKeyboard(tvTime);
            showChooseTimeDialog();
        }
    }

    private void addPhysicalRecord() {
        BaseRequestParam param = new BaseRequestParam();

        if (LangUtils.isEmpty(mSex)) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("sex", mSex);
        }

        if (LangUtils.isEmpty(edtAge.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("age", edtAge.getText().toString());
        }

        if (LangUtils.isEmpty(mTime)) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("exam_date", mTime);
        }

        if (LangUtils.isEmpty(edtHeight.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("height", edtHeight.getText().toString());
        }

        if (LangUtils.isEmpty(edtHeight.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("weight", edtWeight.getText().toString());
        }

        if (LangUtils.isEmpty(edtSkeleton.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("muscle", edtSkeleton.getText().toString());
        }

        if (LangUtils.isEmpty(edtFat.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("body_fat", edtFat.getText().toString());
        }

        if (LangUtils.isEmpty(edtWater.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("body_water", edtWater.getText().toString());
        }

        if (!LangUtils.isEmpty(edtBust.getText())) {
            param.addParam("bust", edtBust.getText());
        }

        if (!LangUtils.isEmpty(edtWaistline.getText())) {
            param.addParam("waistline", edtWaistline.getText().toString());
        }

        if (!LangUtils.isEmpty(edtHipline.getText())) {
            param.addParam("hips", edtHipline.getText().toString());
        }

        if (!LangUtils.isEmpty(edtHeartRate.getText())) {
            param.addParam("heart_rate", edtHeartRate.getText().toString());
        }

        if (!LangUtils.isEmpty(edtSystolicPressure.getText())) {
            param.addParam("sbp", edtSystolicPressure.getText().toString());
        }

        if (!LangUtils.isEmpty(edtDiastolicPressure.getText())) {
            param.addParam("dbp", edtDiastolicPressure.getText().toString());
        }

        if (LangUtils.isEmpty(edtScore.getText())) {
            ViewUtils.showToast("请给出评分", Toast.LENGTH_LONG);
            return;
        } else {
            param.addParam("score", edtScore.getText().toString());
        }

        if (!LangUtils.isEmpty(edtSideAssessment.getText())) {
            param.addParam("lateral_view_eval", edtSideAssessment.getText().toString());
        }

        if (!LangUtils.isEmpty(edtBackAssessment.getText())) {
            param.addParam("dorsal_view_eval", edtBackAssessment.getText().toString());
        }

        if (LangUtils.isEmpty(edtSuggestion.getText())) {
            ViewUtils.showToast("请给合理化建议", Toast.LENGTH_LONG);
            return;
        } else {
            param.addParam("fitness_instruction", edtSuggestion.getText().toString());
        }

        if (!LangUtils.isEmpty(mPageParam.photoUrl)) {
            param.addParam("report_url", mPageParam.photoUrl);
        }

        if (!LangUtils.isEmpty(mPageParam.photoThumbUrl)) {
            param.addParam("report_thumb_url", mPageParam.photoThumbUrl);
        }

        if (mPageParam.record == null) {
            param.addUrlParams(mPageParam.memberId);
            startRequest(XService.AddPhysicalRecord, param);
        } else {
            param.addUrlParams(mPageParam.memberId, mPageParam.record.id);
            startRequest(XService.UpdatePhysicalRecord, param);
        }
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        if (isSameUrl(XService.AddPhysicalRecord, request)) {
            ViewUtils.showToast("添加记录失败", Toast.LENGTH_LONG);
        } else if (isSameUrl(XService.UpdatePhysicalRecord, request)) {
            ViewUtils.showToast("更改记录失败", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        if (isSameUrl(XService.AddPhysicalRecord, request)) {
            ViewUtils.showToast("添加记录失败", Toast.LENGTH_LONG);
        } else if (isSameUrl(XService.UpdatePhysicalRecord, request)) {
            ViewUtils.showToast("更改记录失败", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        if (isSameUrl(XService.AddPhysicalRecord, request)) {
            ViewUtils.showToast("添加记录成功", Toast.LENGTH_LONG);
            xStartActivity(CustomerDetailActivity.class);
            finish();
        } else if (isSameUrl(XService.UpdatePhysicalRecord, request)) {
            ViewUtils.showToast("更改记录成功", Toast.LENGTH_LONG);
            xStartActivity(CustomerDetailActivity.class);
            finish();
        }
    }

    /**
     * 判断输入信息是否是数字
     *
     * @param s 输入信息
     * @return 是否是数字
     */
    private boolean isFloatNum(CharSequence s) {
        boolean flag;
        try {
            float f = Float.parseFloat(s.toString());
            flag = f > 0;
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    private void showParamsIllegalDialog() {
        CustomDialog dialog = new CustomDialog(this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("请将必填项填写后提交！");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showChooseTimeDialog() {
        final Dialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.shareDiaLogWindowAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setContentView(R.layout.ui_chosen_simple_date_dialog);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final XSimpleChosenDateView vChosenDate = (XSimpleChosenDateView) dialog.findViewById(R.id.v_chosen_date);
        Date currentDate = null;
        if (!LangUtils.isEmpty(tvTime.getText())) {
            currentDate = LangUtils.formatAllDayDate(tvTime.getText().toString());
        }

        if (currentDate == null) {
            currentDate = new Date();
        }
        vChosenDate.setSelectedDate(currentDate);

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTime.setText(LangUtils.formatDate(vChosenDate.getValue(), "yyyy-MM-dd"));
                mTime = tvTime.getText().toString();
                dialog.dismiss();
            }
        });
    }

    private void setBodyWeightWithoutFat() {
        float weight = 0.0f;
        float fat = 0.0f;

        if (!LangUtils.isEmpty(edtWeight.getText())) {
            weight = Float.parseFloat(edtWeight.getText().toString());
        }

        if (!LangUtils.isEmpty(edtFat.getText())) {
            fat = Float.parseFloat(edtFat.getText().toString());
        }

        if (weight > 0 && fat > 0 && weight > fat) {
            float f = weight - fat;
            f = (Math.round(f * 10.0)) / 10.0f;
            tvWeightWithoutFat.setText(String.valueOf(f));
            tvWeightWithoutFat.setTextColor(getResources().getColor(R.color.text_color_gray_66));

            f = 100.0f * fat / weight;
            f = (float) (Math.round(f * 10)) / 10;
            tvFatRate.setText(String.valueOf(f));
            tvFatRate.setTextColor(getResources().getColor(R.color.text_color_gray_66));
        }
    }

    private void setFatIndex() {
        float height = 0.0f;
        float fat = 0.0f;

        if (!LangUtils.isEmpty(edtHeight.getText())) {
            height = Float.parseFloat(edtHeight.getText().toString());
            height = height / 100.0f;
        }

        if (!LangUtils.isEmpty(edtWeight.getText())) {
            fat = Float.parseFloat(edtWeight.getText().toString());
        }

        if (height > 0 && fat > 0) {
            float f = (fat / (height * height));
            f = (float) (Math.round(f * 10)) / 10;
            tvFatIndex.setText(String.valueOf(f));
            tvFatIndex.setTextColor(getResources().getColor(R.color.text_color_gray_66));
        }
    }

    private void setBustRate() {
        float waistline = 0.0f;
        float hip = 0.0f;

        if (!LangUtils.isEmpty(edtWaistline.getText())) {
            waistline = Float.parseFloat(edtWaistline.getText().toString());
        }

        if (!LangUtils.isEmpty(edtHipline.getText())) {
            hip = Float.parseFloat(edtHipline.getText().toString());
        }

        if (waistline > 0 && hip > 0) {
            float f = waistline / hip;
            f = (float) (Math.round(f * 100)) / 100;
            tvBustRate.setText(String.valueOf(f));
            tvBustRate.setTextColor(getResources().getColor(R.color.text_color_gray_66));
        }
    }

    private void clearTextWatcher() {
        edtAgeWatcher = null;
        edtHeightWatcher = null;
        edtWeightWatcher = null;
        edtFatWatcher = null;
        edtSkeletonWatcher = null;
        edtWaterWatcher = null;
        edtBustWatcher = null;
        edtWaistlineWatcher = null;
        edtHipWatcher = null;
        edtHeartWatcher = null;
        edtSbpWatcher = null;
        edtDbpWatcher = null;
        edtScoreWatcher = null;
    }

    @Override
    protected void onDestroy() {
        clearTextWatcher();
        super.onDestroy();
    }

    private class MTextWatcher implements TextWatcher {
        private EditText mEdtText;

        public MTextWatcher(EditText editText) {
            mEdtText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isFinishing()) {
                return;
            }

            if (!isFloatNum(s) && s != null && s.length() > 0) {
                ViewUtils.showToast("请输入数字", Toast.LENGTH_LONG);
                mEdtText.setText("");
                return;
            }

            setBodyWeightWithoutFat();
            setFatIndex();
            setBustRate();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
