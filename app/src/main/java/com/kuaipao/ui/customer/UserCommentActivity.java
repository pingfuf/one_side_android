package com.kuaipao.ui.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.inject.From;
import com.kuaipao.model.beans.XEmoji;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.utils.XEmojiManager;
import com.kuaipao.manager.R;

/**
 * 用户评论页面，v_3.4版本添加了新的评论界面，添加小熊快跑表情包
 *
 * @author pingfu
 */
public class UserCommentActivity extends BaseActivity {
    private static final int MAX_COMMENT_SIZE = 500;
    private TextView tvSendComment;

    @From(R.id.edt_comment)
    private EditText edtComment;

    private SpannableStringBuilder mSpannableBuilder;

    private boolean isBeerExpressionShow;
    private boolean isKeyboardSystemShow = false;
    private boolean isKeyboardClickShow = false;
    private int mSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comment);

        setTitle();
        initUI();
    }

    /**
     * 初始化UI界面
     */
    private void initUI() {
        edtComment.addTextChangedListener(new TextWatcher() {
            boolean isSpanable = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtils.e("beforeTextChange");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null) {
                    return;
                }
                mSelection = start + count;
                LogUtils.e("onTextChanged start = %s, before = %s, count = %s", start, before, count);
                if (s.length() > MAX_COMMENT_SIZE) {
                    dealCommentTooLangCondition(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtils.e("afterTextChanged " + s.toString());
                int selection = edtComment.getSelectionEnd();
                LogUtils.e("edt selection %s", selection);
            }
        });
    }

    private void setTitle() {
        tvSendComment = ViewUtils.createTitleBarRightTextView(this, "发送");
        tvSendComment.setOnClickListener(this);
        setTitle("发表评论", true, tvSendComment);
    }

    @Override
    public void onClick(View v) {
        if(isFinishing() || v == null) {
            return;
        }

        if(v == tvSendComment) {
            sendComment();
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
    }

    private void addEmoji(XEmoji emoji) {
        if(emoji == null || LangUtils.isEmpty(emoji.serverCode)) {
            return;
        }

        String content = edtComment.getText().toString();
        int start = content.length();

        if(start + emoji.serverCode.length() > MAX_COMMENT_SIZE) {
            ViewUtils.showToast(getString(R.string.comments_500_beyond_warning), Toast.LENGTH_SHORT);
            return;
        }

        int selection = edtComment.getSelectionEnd();
        String startStr = content.substring(0, selection);
        String endStr = content.substring(selection, content.length());
        content = startStr + emoji.serverCode + endStr;

        mSpannableBuilder = new SpannableStringBuilder(content);
        XEmojiManager.parseIOSEmojis(mSpannableBuilder, 0);

        edtComment.setText(mSpannableBuilder);
        edtComment.setSelection(selection + emoji.serverCode.length());
    }

    private void deleteEmoji() {
        int selection = edtComment.getSelectionEnd();
        if(selection == 0) {
            return;
        }
        XEmoji emoji = getDeleteEmoji(selection);
        int length = 1;
        if(emoji != null && emoji.serverCode != null) {
            length = emoji.serverCode.length();
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(edtComment.getText());
        builder.delete(selection - length, selection);
        edtComment.setText(builder);
        edtComment.setSelection(selection - length);
    }

    private XEmoji getDeleteEmoji(int selection) {
        XEmoji emoji = null;
        if(selection >= 4) {
            CharSequence commentStr = edtComment.getText();
            char c = commentStr.charAt(selection - 1);
            if(c == ']') {
                //可能是小熊表情
                int i = selection - 2;
                while(i >= 0 && commentStr.charAt(i) != '[') {
                    i --;
                }
                if(i >= 0) {
                    String serverCode = commentStr.subSequence(i, selection).toString();
                    emoji = XEmojiManager.getEmoji(serverCode, XEmojiManager.BEER_EMOJI_TYPE);
                }
            } else {
                //可能是ios表情包
                if(selection >= 7) {
                    CharSequence iosEmojiCode = commentStr.subSequence(selection - 7, selection);
                    emoji = XEmojiManager.getEmoji(String.valueOf(iosEmojiCode), XEmojiManager.IOS_EMOJI_TYPE);
                }
            }
        }

        return emoji;
    }

    /**
     * 处理评论过长的情况
     *
     * @param text 评论内容
     */
    private void dealCommentTooLangCondition(String text) {
        if(text == null || text.length() < MAX_COMMENT_SIZE) {
            return;
        }
        if(mSpannableBuilder == null){
            mSpannableBuilder = new SpannableStringBuilder(text.substring(0, MAX_COMMENT_SIZE));
        } else if(mSpannableBuilder.length() < MAX_COMMENT_SIZE) {
            String temp = text.substring(mSpannableBuilder.length(), MAX_COMMENT_SIZE);
            mSpannableBuilder.append(temp);
        }
        edtComment.setText(mSpannableBuilder);
        edtComment.setSelection(mSpannableBuilder.length());
        ViewUtils.showToast(getString(R.string.comments_500_beyond_warning), Toast.LENGTH_SHORT);
    }

    private void sendComment() {
        Intent intent = new Intent();
        if(edtComment.getText() != null) {
            intent.putExtra("user_comment", edtComment.getText().toString());
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        XEmojiManager.clearEmojiCache();
        super.onDestroy();
    }
}
