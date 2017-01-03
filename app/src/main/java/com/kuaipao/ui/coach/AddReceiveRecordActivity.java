package com.kuaipao.ui.coach;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.base.net.XService;
import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.base.view.numpicker.XSimpleChosenDateView;
import com.kuaipao.manager.CardSessionManager;
import com.kuaipao.model.beans.XUser;
import com.kuaipao.model.response.CoachReceiveCustomerListResponse;
import com.kuaipao.ui.CustomDialog;
import com.kuaipao.ui.customer.CustomerDetailActivity;
import com.kuaipao.ui.customer.CustomerPageParam;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

/**
 * 添加跟进记录
 *
 * @author pingfu
 */
public class AddReceiveRecordActivity extends BaseActivity {
    private static final int ITEM_HEIGHT = ViewUtils.rp(40);
    private static final int MAX_ITEM_SIZE = 7;

    @From(R.id.iv_header)
    private ImageView ivHeader;

    @From(R.id.tv_name)
    private TextView tvName;

    @From(R.id.tv_time)
    private TextView tvTime;

    @From(R.id.edt_desc)
    private EditText edtDesc;

    @From(R.id.btn_submit)
    private Button btnSubmit;

    private List<XUser> mCustomers;

    private XUser mChosenCustomer;

    @XAnnotation
    private CustomerPageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_record);

        setTitle("添加跟进", true);
        initUI();

        fetchCustomers();
        showLoadingDialog();
    }

    private void initUI() {
        tvName.setText(CardSessionManager.getInstance().getUser().name);
        mChosenCustomer = CardSessionManager.getInstance().getUser();

        tvName.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        try {
            ivHeader.setImageResource(R.mipmap.ic_add_receive_record_header);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
    }

    private void fetchCustomers() {
        BaseRequestParam param = new BaseRequestParam();
        param.addParam("page_size", 100);
        startRequest(XService.CoachReceiveCustomerList, param);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == tvTime) {
            showChooseTimeDialog();
        } else if (v == btnSubmit) {
            addRecords();
        } else if (v == tvName) {
            showDialog(mCustomers, tvName);
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
        } else if (isSameUrl(XService.AddReceiveRecord, request)) {
            ViewUtils.showToast("添加成功", Toast.LENGTH_LONG);
            xStartActivity(CustomerDetailActivity.class);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        dismissLoadingDialog(500);
        if (isSameUrl(XService.AddReceiveRecord, request)) {
            ViewUtils.showToast("添加失败", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        dismissLoadingDialog(500);
        if (isSameUrl(XService.AddReceiveRecord, request)) {
            ViewUtils.showToast("添加失败", Toast.LENGTH_LONG);
        }
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
                tvTime.setTextColor(getResources().getColor(R.color.text_color_gray_66));
                dialog.dismiss();
            }
        });
    }

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

    private void addRecords() {
        BaseRequestParam param = new BaseRequestParam();
        if (mChosenCustomer == null) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("member_id", mPageParam.memberId);
            param.addParam("user_id", mChosenCustomer.id);
        }

        if (LangUtils.isEmpty(tvTime.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("follow_up_date", tvTime.getText().toString());
        }

        if (LangUtils.isEmpty(edtDesc.getText())) {
            showParamsIllegalDialog();
            return;
        } else {
            param.addParam("remark", edtDesc.getText().toString());
        }

        startRequest(XService.AddReceiveRecord, param);
    }

    private void showParamsIllegalDialog() {
        CustomDialog dialog = new CustomDialog(this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("请填写所有项目后提交");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
