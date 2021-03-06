package com.oneside.ui.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.CardApplication;
import com.oneside.manager.CardManager;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

import edu.umd.cs.findbugs.annotations.CheckForNull;

import static com.oneside.utils.LangUtils.isNotEmpty;
import static com.oneside.utils.ViewUtils.find;
import static com.oneside.utils.ViewUtils.getString;

public class LoginInputView extends RelativeLayout implements View.OnClickListener {
    public interface LoginStatusListener {
        /**
         * @param status 0 : start login; 1 : login succ; 2 : login failed;
         */
        void onStatusChanged(int status);
    }

    private TextView mPhoneType, mUserAttention;
    private Button mSendButton, mLoginButton;
    private EditText mPhoneEdit, mInputEdit;
    private LoginStatusListener mStatusChangedListener;
    private boolean mGetVerify = false;
    private TimeCountTimer mTimeCountTimer;

    public LoginInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public LoginInputView(Context context) {
        super(context);
        initViews(context);
    }

    public void setOnStatusChangedListener(LoginStatusListener l) {
        mStatusChangedListener = l;
    }

    public void setLoginButtonText(CharSequence s) {
        mLoginButton.setText(s);
    }

    public void setLoginButtonText(int rid) {
        mLoginButton.setText(rid);
    }

    public void onClick(View v) {
    }

    private void showLoginToast(String msg, int duration) {

        TextView tv =
                (TextView) View.inflate(CardManager.getApplicationContext(), R.layout.view_toast_center_tv,
                        null);
        tv.setText(msg);
        Toast t = new Toast(tv.getContext());
        t.setView(tv);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.setDuration(Toast.LENGTH_SHORT); // hard coded duration
        t.show();

    }

    private void showToast(@CheckForNull final String msg, final int duration) {
        if (LangUtils.isEmpty(msg))
            return;
        if (ViewUtils.isMainThread())
            showLoginToast(msg, duration);
        else {
            ViewUtils.runInHandlerThread(new Runnable() {
                public void run() {
                    showLoginToast(msg, duration);
                }
            });
        }
    }

    private void initViews(Context context) {
        View.inflate(context, R.layout.ui_phone_confirm, this);
        mTimeCountTimer = new TimeCountTimer(60000, 1000);
        mSendButton = find(this, R.id.tv_confirm);
        mUserAttention = find(this, R.id.tv_attention_2);
        mUserAttention.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
        mPhoneEdit = find(this, R.id.et_phone);
        mPhoneType = find(this, R.id.phone_type);
        mInputEdit = find(this, R.id.et_confirm_code);
        mLoginButton = find(this, R.id.tv_confirm_login);
        mLoginButton.setOnClickListener(this);
        SpannableString spanStr = new SpannableString(getString(R.string.register_content));
        ForegroundColorSpan spanColor =
                new ForegroundColorSpan(getResources().getColor(R.color.blue_user_protocol));
        ForegroundColorSpan spanColor1 =
                new ForegroundColorSpan(getResources().getColor(R.color.blue_user_protocol));
        spanStr.setSpan(spanColor, 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(spanColor1, 9, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mUserAttention.setText(spanStr);
        mPhoneEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (mPhoneEdit.length() >= 1) {
                    // mPhoneEdit.setBackgroundColor(color);
                    mPhoneType.setBackgroundResource(R.drawable.corners_left_login_pressed_bg);
                } else {
                    mPhoneType.setBackgroundResource(R.drawable.corners_left_login_bg);
                }
            }

        });
        mInputEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mInputEdit.length() >= 6) {
                    mLoginButton.setBackgroundResource(R.drawable.btn_orange_bg);
                    mLoginButton.setClickable(true);
                } else {
                    mLoginButton.setBackgroundResource(R.drawable.logi_btn_gray_bg);
                    mLoginButton.setClickable(false);
                }
            }

        });
    }


    class TimeCountTimer extends CountDownTimer {
        public TimeCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mSendButton.setText(CardApplication.getApplication().getString(R.string.verify_button_again));
            mSendButton.setBackgroundResource(R.drawable.btn_orange_login_bg);
            mSendButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mSendButton.setClickable(false);
            mSendButton.setBackgroundResource(R.drawable.logi_btn_gray_bg);
            mSendButton.setText(millisUntilFinished / 1000
                    + CardApplication.getApplication().getString(R.string.confirm_second));
        }
    }


}
