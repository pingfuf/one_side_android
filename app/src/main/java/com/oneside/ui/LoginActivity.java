package com.oneside.ui;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.manager.CardManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.manager.XSoftKeyboardManager;
import com.oneside.model.beans.XUser;
import com.oneside.model.event.LoginStatusChangedEvent;
import com.oneside.model.response.LoginResponse;
import com.oneside.utils.Constant;
import com.oneside.R;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements XSoftKeyboardManager.OnSoftKeyboardShowOrHideListener {
    public static final int LOGIN_PAGE_CODE = 801;
    private static final String INPUT_NAME = "INPUT_NAME";
    private static final String SPLIT_CHAR = "#";

    @From(R.id.ll_container)
    private LinearLayout llContainer;

    @From
    private ImageView ivHeader;

    @From(R.id.edt_name)
    private EditText edtName;

    @From(R.id.iv_name)
    private ImageView ivName;

    @From(R.id.edt_password)
    private EditText edtPassword;

    @From(R.id.edt_password2)
    private EditText edtPassword2;

    @From(R.id.iv_password)
    private ImageView ivPassword;

    @From(R.id.btn_submit)
    private Button btnSubmit;

    private XSoftKeyboardManager softKeyboardManager;
    private List<String> mInputHistory;
    //    private int popUpWindowHeight = 0;
    private PopupWindow mPopupWindow;
//    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_login);
        CardManager.logUmengEvent(Constant.UMENG_EVENT_LOGIN_START);

        initUI();
    }

    private void initUI() {
        btnSubmit.setOnClickListener(this);
        llContainer.setOnClickListener(this);
        ivName.setOnClickListener(this);
        ivPassword.setOnClickListener(this);

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(edtPassword2.getText().toString())) {
                    edtPassword2.setText(s);
                    edtPassword2.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(edtPassword.getText().toString())) {
                    edtPassword.setText(s);
                    edtPassword.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        softKeyboardManager = XSoftKeyboardManager.build(this, this);

        mInputHistory = getInputHistory();
        if (LangUtils.isEmpty(mInputHistory)) {
            ivName.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (isFinishing()) {
            return;
        }

        if (v == btnSubmit) {
            login();
        } else if (v == llContainer) {
            if (softKeyboardManager.isSoftKeyboardShowing()) {
                softKeyboardManager.hideSoftKeyboard(llContainer);
            }
            hidePopupWindow();
        } else if (v == ivPassword) {
            if (edtPassword.getVisibility() == View.VISIBLE) {
                edtPassword2.setVisibility(View.VISIBLE);
                edtPassword.setVisibility(View.GONE);
                ivPassword.setImageResource(R.drawable.ic_close_eye);
                edtPassword2.requestFocus();
            } else {
                edtPassword2.setVisibility(View.GONE);
                edtPassword.setVisibility(View.VISIBLE);
                ivPassword.setImageResource(R.drawable.ic_open_eye_gray);
                edtPassword.requestFocus();
            }
        } else if (v == ivName) {
            softKeyboardManager.hideSoftKeyboard(llContainer);
            showPopupWindow();
            ivName.setImageResource(R.mipmap.ic_arrow_up);
        }
    }

    private void login() {
        String name = edtName.getText().toString();
        String password = edtPassword.getText().toString();
        if (LangUtils.isEmpty(name) || LangUtils.isEmpty(password)) {
            ViewUtils.showToast("请输入用户名和密码", Toast.LENGTH_LONG);
            return;
        }
        BaseRequestParam requestParam = new BaseRequestParam();
        requestParam.addParam("username", name);
        requestParam.addParam("password", password);
        requestParam.addParam("remeber", true);
        startRequest(XService.LOGIN, requestParam);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        dealWithResponseError(null);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        dealWithResponseError(message);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        LoginResponse response = (LoginResponse) data;
        if (response != null && !LangUtils.isEmpty(response.token)) {
            XUser user = new XUser();
            user.name = response.name;
            user.isAdmin = response.isAdmin;
            CardSessionManager.getInstance().restoreSession(user, response.token);
            EventBus.getDefault().post(new LoginStatusChangedEvent(true));

            if (mInputHistory == null) {
                mInputHistory = new ArrayList<>();
            }

            String userName = edtName.getText().toString();
            if (LangUtils.isNotEmpty(userName)) {
                if (!mInputHistory.contains(userName)) {
                    mInputHistory.add(userName);
                    saveInput(mInputHistory);
                }
            }

            setResult(RESULT_OK);
            finish();
        } else {
            dealWithResponseError(null);
        }
    }

    @Override
    public void onKeyboardShow() {
        hidePopupWindow();
    }

    @Override
    public void onKeyboardHide() {

    }

    private void dealWithResponseError(String message) {
        String str = LangUtils.isEmpty(message) ? "登陆失败" : message;
        ViewUtils.showToast(str, Toast.LENGTH_LONG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showPopupWindow() {
        int[] heights = new int[2];
        edtName.getLocationOnScreen(heights);
        int popUpWindowHeight = SysUtils.HEIGHT - heights[1] - edtName.getHeight();

        if (mPopupWindow == null) {
//            String[] historyArray = mInputHistory.split(SPLIT_CHAR);
            LogUtils.d("history %s %s", mInputHistory, mInputHistory.size());
            View view = View.inflate(this, R.layout.ui_login_pop_up, null);
            int w = SysUtils.WIDTH - ViewUtils.rp(40 - 6);
            int h = ViewUtils.rp(32) * mInputHistory.size();
            if (h > popUpWindowHeight) {
                h = popUpWindowHeight;
            }
            mPopupWindow = new PopupWindow(view, w, h);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            ListView lvContent = (ListView) view.findViewById(R.id.lv_content);

            final ArrayAdapter adapter;

            if (!LangUtils.isEmpty(mInputHistory)) {
                adapter = new ArrayAdapter(this, R.layout.ui_login_popupwindow_item, R.id.tv_item, mInputHistory);
                lvContent.setAdapter(adapter);
            }

            lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    edtName.setText(parent.getItemAtPosition(position).toString());
                    edtName.setSelection(parent.getItemAtPosition(position).toString().length());
                    hidePopupWindow();
                }
            });
        }

        mPopupWindow.showAsDropDown(edtName, -ViewUtils.rp(4), 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivName.setImageResource(R.mipmap.ic_arrow_down);
            }
        });
    }

    private void hidePopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private void saveInput(List<String> inputs) {
        StringBuilder sb = new StringBuilder();
        for (String input : inputs) {
            if (LangUtils.isNotEmpty(input)) {
                sb.append(input);
                sb.append(SPLIT_CHAR);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        IOUtils.savePreferenceValue(INPUT_NAME, sb.toString());
    }

    private List<String> getInputHistory() {
        String input = IOUtils.getPreferenceValue(INPUT_NAME);
        if (LangUtils.isNotEmpty(input)) {

            String[] inputsArray = input.split(SPLIT_CHAR);
            List<String> ret = new ArrayList<>(inputsArray.length);
            for (int i = 0; i < inputsArray.length; i++) {
                if (LangUtils.isNotEmpty(inputsArray[i]))
                    ret.add(inputsArray[i]);
            }
            return ret;
        }
        return null;
    }
}
