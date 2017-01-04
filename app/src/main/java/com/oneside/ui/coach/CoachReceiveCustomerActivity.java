package com.oneside.ui.coach;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.base.CardConfig;
import com.oneside.base.inject.From;
import com.oneside.base.BaseActivity;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.manager.CardSessionManager;
import com.oneside.R;
import com.oneside.model.beans.Gender;
import com.oneside.model.beans.XUser;
import com.oneside.model.response.CoachReceiveCustomerListResponse;
import com.oneside.ui.CustomDialog;
import com.oneside.ui.user.CustomerSourceActivity;
import com.oneside.ui.user.CustomerTypeActivity;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import static com.oneside.utils.ViewUtils.find;

/**
 * 顾客接待页面
 * <p/>
 * Created by pingfu
 */
public class CoachReceiveCustomerActivity extends BaseActivity {
    public static final int CUSTOMER_TYPE_PAGE_REQUEST = 100;
    public static final int CUSTOMER_SOURCE_PAGE_REQUEST = 101;
    public static final int CUSTOMER_SUCCESS_CODE = 102;

    private static final int ITEM_HEIGHT = ViewUtils.rp(40);
    private static final int MAX_ITEM_SIZE = 7;

    @From(R.id.iv_header)
    private ImageView ivHeader;

    @From(R.id.ll_receiver)
    private LinearLayout llReceiver;

    @From(R.id.tv_customer)
    private TextView tvCustomer;

    @From(R.id.edt_name)
    private EditText edtName;

    @From(R.id.ll_male)
    private LinearLayout llMale;

    @From(R.id.iv_male)
    private ImageView ivMale;

    @From(R.id.ll_female)
    private LinearLayout llFemale;

    @From(R.id.iv_female)
    private ImageView ivFemale;

    @From(R.id.edt_phone)
    private EditText edtPhone;

    @From(R.id.ll_customer_type)
    private LinearLayout llCustomerType;

    @From(R.id.tv_type)
    private TextView tvType;

    @From(R.id.edt_type_desc)
    private EditText edtTypeDesc;

    @From(R.id.ll_customer_source)
    private LinearLayout llCustomerSource;

    @From(R.id.tv_source)
    private TextView tvSource;

    @From(R.id.edt_source_desc)
    private EditText edtSourceDesc;

    @From(R.id.edt_desc)
    private EditText edtDesc;

    @From(R.id.btn_submit)
    private Button btnSubmit;

    private TextView tvRight;

    private List<XUser> mCustomers;

    private XUser mChosenCustomer;
    private String mGender;
    private int mSourceCode = -1;
    private int mTypeCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_recieve_customer);

        if (CardConfig.isDevBuild()) {
            tvRight = ViewUtils.createTitleBarRightTextView(this, "测试");
            setTitle(getString(R.string.coach_receive_customer_title), true, tvRight);
            tvRight.setOnClickListener(this);
        } else {
            setTitle(getString(R.string.coach_receive_customer_title), true);
        }

        initUI();

        fetchCustomers();
        showLoadingDialog();
    }

    private void fetchCustomers() {
        BaseRequestParam param = new BaseRequestParam();
        param.addParam("page_size", 100);
        startRequest(XService.CoachReceiveCustomerList, param);
    }

    private void initUI() {
        tvCustomer.setText(CardSessionManager.getInstance().getUser().name);
        mChosenCustomer = CardSessionManager.getInstance().getUser();
        llReceiver.setOnClickListener(this);
        llCustomerType.setOnClickListener(this);
        llMale.setOnClickListener(this);
        llFemale.setOnClickListener(this);
        mGender = Gender.MALE.getSexName();
        llCustomerSource.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == llReceiver) {
            showDialog(mCustomers, tvCustomer);
        } else if (v == llMale) {
            choseCustomerSex(true);
        } else if (v == llFemale) {
            choseCustomerSex(false);
        } else if (v == llCustomerType) {
            BasePageParam pageParam = new BasePageParam();
            pageParam.gymId = mTypeCode;
            xStartActivity(CustomerTypeActivity.class, pageParam, CUSTOMER_TYPE_PAGE_REQUEST);
        } else if (v == llCustomerSource) {
            BasePageParam pageParam = new BasePageParam();
            pageParam.gymId = mSourceCode;
            xStartActivity(CustomerSourceActivity.class, pageParam, CUSTOMER_SOURCE_PAGE_REQUEST);
        } else if (v == btnSubmit) {
            submitCustomerInfo();
        } else if (v == tvRight) {
//            xStartActivity(TempActivity.class);
        }
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        dismissLoadingDialog(500);
        if (isSameUrl(XService.CoachReceiveCustomerList, request)) {
            CoachReceiveCustomerListResponse response = (CoachReceiveCustomerListResponse) data;
            if (response != null) {
                mCustomers = response.items;
            }
        } else if (isSameUrl(XService.CoachReceiveCustomer, request)) {
            xStartActivity(CoachReceiveSuccessActivity.class, CUSTOMER_SUCCESS_CODE);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        dismissLoadingDialog(500);
        if (isSameUrl(XService.CoachReceiveCustomer, request)) {
            ViewUtils.showToast(message, Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        dismissLoadingDialog(500);
        if (isSameUrl(XService.CoachReceiveCustomer, request)) {
            ViewUtils.showToast("网络错误", Toast.LENGTH_LONG);
        }
    }

    private void choseCustomerSex(boolean isMale) {
        mGender = isMale ? Gender.MALE.getSexName() : Gender.FEMALE.getSexName();

        if(isMale) {
            ivMale.setImageResource(R.mipmap.ic_male);
            ivFemale.setImageResource(R.mipmap.ic_male_gray);
        } else {
            ivMale.setImageResource(R.mipmap.ic_male_gray);
            ivFemale.setImageResource(R.mipmap.ic_female);
        }
    }

    /**
     * 上传接待客户信息
     */
    private void submitCustomerInfo() {
        BaseRequestParam param = new BaseRequestParam();
        if (mChosenCustomer == null) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("user_id", mChosenCustomer.id);
        }

        if (LangUtils.isEmpty(edtName.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("name", edtName.getText().toString());
        }

        if (!LangUtils.isEmpty(mGender)) {
            param.addParam("gender", mGender);
        } else {
            showParamsIllegalDialog();
            return;
        }

        if (LangUtils.isEmpty(edtPhone.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("phone", edtPhone.getText().toString());
        }

        if (mTypeCode < 0) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("type", mTypeCode + 1);
        }

        if (!LangUtils.isEmpty(edtTypeDesc.getText())) {
            param.addParam("type_remark", edtTypeDesc.getText().toString());
        }

        if (mSourceCode >= 0) {
            param.addParam("source", mSourceCode + 1);

            if (!LangUtils.isEmpty(edtSourceDesc.getText())) {
                param.addParam("source_remark", edtSourceDesc.getText().toString());
            }
        }

        if (!LangUtils.isEmpty(edtDesc.getText())) {
            param.addParam("remark", edtDesc.getText().toString());
        }

        startRequest(XService.CoachReceiveCustomer, param);
        showLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CUSTOMER_TYPE_PAGE_REQUEST) {
                if (data != null) {
                    String type = data.getStringExtra("type");
                    if (!LangUtils.isEmpty(type)) {
                        tvType.setText(type);
                        tvType.setTextColor(getResources().getColor(R.color.text_color_gray));
                        mTypeCode = data.getIntExtra("typeCode", 0);
                    }
                }
            } else if (requestCode == CUSTOMER_SOURCE_PAGE_REQUEST) {
                if (data != null) {
                    String source = data.getStringExtra("source");
                    if (!LangUtils.isEmpty(source)) {
                        tvSource.setText(source);
                        tvSource.setTextColor(getResources().getColor(R.color.text_color_gray));
                        mSourceCode = data.getIntExtra("sourceCode", 0);
                    }
                }
            } else if(requestCode == CUSTOMER_SUCCESS_CODE) {
                tvCustomer.setText(CardSessionManager.getInstance().getUser().name);
                edtName.setText("");
                edtName.setHint(getString(R.string.name_hint));
                edtName.requestFocus();

                choseCustomerSex(true);
                edtPhone.setText("");
                edtPhone.setHint(getString(R.string.phone_hint));

                mTypeCode = -1;
                tvType.setText("请选择访客类型");
                tvType.setTextColor(getResources().getColor(R.color.dark_shadow));

                mSourceCode = -1;
                tvSource.setText("请选择信息来源");
                tvSource.setTextColor(getResources().getColor(R.color.dark_shadow));

                edtTypeDesc.setText("");
                edtTypeDesc.setHint("备注");

                edtSourceDesc.setText("");
                edtSourceDesc.setHint("备注");

                edtDesc.setText("");
                edtDesc.setText("备注");
            }
        }
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

    /**
     * 显示选择顾客对话框
     *
     * @param items    顾客列表
     * @param textView textView
     */
    private void showDialog(List<XUser> items, TextView textView) {
        final Dialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.shareDiaLogWindowAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setContentView(R.layout.ui_choose_gym);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView llContent = (ListView) dialog.findViewById(R.id.ll_content);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        addItems(dialog, items, llContent, textView);
    }

    private void addItems(final Dialog dialog, final List<XUser> items, ListView llContent, final TextView textView) {
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
            gymNames.add(items.get(i).name);
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
                textView.setText(items.get(position).name);
                textView.setTextColor(getResources().getColor(R.color.text_color_gray));
                mChosenCustomer = items.get(position);
            }
        });
    }
}
