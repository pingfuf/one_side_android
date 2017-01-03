package com.oneside.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oneside.pay.PaySelectAdapter;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.oneside.utils.LangUtils.isEmpty;
import static com.oneside.utils.ViewUtils.find;

/**
 * Created by Guo Ming on 05/05/15.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {
    View dialogView;
    LinearLayout layout;
    TextView titleView;
    ImageView imgView;
    LinearLayout content;
    ScrollView scrollView;
    TextView messageView;
    private static final int BUTTON_CLOSE = -4;
    FrameLayout customContent;
    View customView;

    LinearLayout buttonContent;
    Button positiveButton, neutralButton, negativeButton, imgCloseButton;
    OnClickListener positiveListener, neutralListener, negativeListener, imgCloseListener;

    ListView listView;
    LinearLayout bgLayout;
    AnimationSet mModalInAnim;
    // LinearLayout production_factory;

    public CustomDialog(Context context) {
        super(context, R.style.MyDialog);
        mModalInAnim = (AnimationSet) ViewUtils.loadAnimation(context, R.anim.dialog_model_in);
        initViews(context);
    }

    public Button getButton(int whichButton) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                return positiveButton;
            case BUTTON_NEUTRAL:
                return neutralButton;
            case BUTTON_NEGATIVE:
                return negativeButton;
            case BUTTON_CLOSE:
                return imgCloseButton;
        }
        return null;
    }

    @Override
    protected void onStart() {
        dialogView.startAnimation(mModalInAnim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogView = getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void setBackground(int resID) {
        layout.setBackgroundResource(resID);
    }

    public void setTitleImage(int imgID) {
        imgView.setBackgroundResource(imgID);
    }

    public void setBg(int bgId) {
        bgLayout.setBackgroundResource(bgId);
    }

    public void setPostiveBtnBg(int id) {
        positiveButton.setBackgroundResource(id);
    }

    public void setBtnMarginBottom(int margin) {
        RelativeLayout.LayoutParams params =
                (android.widget.RelativeLayout.LayoutParams) buttonContent.getLayoutParams();
        params.bottomMargin = margin;
        buttonContent.setLayoutParams(params);
    }


    public void setCloseBtnMarginRight(int margin) {
        FrameLayout.LayoutParams params =
                (android.widget.FrameLayout.LayoutParams) imgCloseButton.getLayoutParams();
        params.rightMargin = margin;
        imgCloseButton.setLayoutParams(params);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        titleView.setText(title);
    }

    public void setMessage(CharSequence message) {
        messageView.setText(message);
    }

    public void setMsgTextSize(int size) {
        messageView.setTextSize(size);
    }

    public void setMesgGravity(int gravity) {
        messageView.setGravity(gravity);
    }

    public void setView(View view) {
        customView = view;
        customContent.removeAllViews();
        if (customView != null) {
            customContent.addView(customView);
        }
    }

    public View getCustomView() {
        return customView;
    }

    /**
     * Just valid for list type
     *
     * @return
     */
    public int getSelectedPosition() {
        if (listView != null && listView.getAdapter() != null) {
            Adapter adapter = listView.getAdapter();
            if (adapter instanceof PaySelectAdapter) {
                return ((PaySelectAdapter) adapter).getSelectedPosition();
            }
        }
        return -1;
    }

    public void setPositiveButton(CharSequence text, OnClickListener listener) {
        setButton(BUTTON_POSITIVE, text, listener);
    }

    public void setNegativeButton(CharSequence text, OnClickListener listener) {
        setButton(BUTTON_NEGATIVE, text, listener);
    }

    public void setNeutralButton(CharSequence text, OnClickListener listener) {
        setButton(BUTTON_NEUTRAL, text, listener);
    }

    public void setCloseButton(OnClickListener listener) {
        setButton(BUTTON_CLOSE, null, listener);
    }

    public void setWidth(int width) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
        params.width = width;
        layout.setLayoutParams(params);
    }

    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        Button button = getButton(whichButton);
        if (button != null) {
            button.setText(text);
            switch (whichButton) {
                case BUTTON_POSITIVE:
                    positiveListener = listener;
                    break;
                case BUTTON_NEGATIVE:
                    negativeListener = listener;
                    break;
                case BUTTON_NEUTRAL:
                    neutralListener = listener;
                    break;
                case BUTTON_CLOSE:
                    button.setText(null);
                    if ("black".equals(text)) {
                        button.setBackgroundResource(R.drawable.ic_close_black);
                    } else if ("white".equals(text)) {
                        button.setBackgroundResource(R.drawable.ic_close_white);
                    }
                    imgCloseListener = listener;
                    break;
            }
        }
    }

    public void setTitleBgRes(int resid) {
        titleView.setBackgroundResource(resid);
    }

    public void setTitleColor(int color) {
        titleView.setTextColor(color);
    }

    void initListView(ListAdapter adapter, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        listView = (ListView) View.inflate(getContext(), R.layout.custom_dialog_list, null);
        listView.setAdapter(adapter);
        if (onItemSelectedListener != null) {
            listView.setOnItemSelectedListener(onItemSelectedListener);
        }
        content.removeAllViews();
        content.addView(listView);
    }

    public void onClick(View v) {
        dismiss();
        if (v == positiveButton) {
            if (positiveListener != null)
                positiveListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
        } else if (v == negativeButton) {
            if (negativeListener != null)
                negativeListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
        } else if (v == neutralButton) {
            if (neutralListener != null)
                neutralListener.onClick(this, DialogInterface.BUTTON_NEUTRAL);
        } else if (v == imgCloseButton) {
            if (imgCloseListener != null)
                imgCloseListener.onClick(this, BUTTON_CLOSE);
        }
    }

    void updateVisibility() {
        if (isEmpty(titleView.getText())) {
            titleView.setVisibility(GONE);
        } else {
            titleView.setVisibility(VISIBLE);
        }
        if (imgView.getDrawable() == null) {
            imgView.setVisibility(VISIBLE);
        } else {
            imgView.setVisibility(GONE);
        }
        if (customView != null) {
            customContent.setVisibility(VISIBLE);
            // content.setVisibility(GONE);
            scrollView.setVisibility(GONE);
        } else {
            customContent.setVisibility(GONE);

            if (isEmpty(messageView.getText()) && listView == null)
                // content.setVisibility(GONE);
                scrollView.setVisibility(GONE);
            else
                // content.setVisibility(VISIBLE);
                scrollView.setVisibility(VISIBLE);
        }

        if (isEmpty(positiveButton.getText()) && isEmpty(neutralButton.getText())
                && isEmpty(negativeButton.getText()))
            buttonContent.setVisibility(GONE);
        else {
            buttonContent.setVisibility(VISIBLE);
            positiveButton.setVisibility(isEmpty(positiveButton.getText()) ? GONE : VISIBLE);
            negativeButton.setVisibility(isEmpty(negativeButton.getText()) ? GONE : VISIBLE);
            neutralButton.setVisibility(isEmpty(neutralButton.getText()) ? GONE : VISIBLE);
        }
        if (negativeButton.getVisibility() == GONE && neutralButton.getVisibility() == GONE
                && positiveButton.getVisibility() == VISIBLE) {
            View v = getWindow().getDecorView();
            LinearLayout production_factory = find(v, R.id.dialog_button_content);
            LayoutParams params = (LayoutParams) production_factory.getLayoutParams();
            params.width = SysUtils.WIDTH / 2;
            buttonContent.setLayoutParams(params);
        } else if (negativeButton.getVisibility() == VISIBLE && neutralButton.getVisibility() == GONE
                && positiveButton.getVisibility() == GONE) {
            View v = getWindow().getDecorView();
            LinearLayout production_factory = find(v, R.id.dialog_button_content);
            LayoutParams params = (LayoutParams) production_factory.getLayoutParams();
            params.width = SysUtils.WIDTH / 2;
            buttonContent.setLayoutParams(params);
        }
    }

    @Override
    public void show() {
        updateVisibility();
        super.show();
    }


    /***************
     * Private method
     ******************/
    private void initViews(Context context) {
        setContentView(R.layout.custom_dialog);

        View v = getWindow().getDecorView();
        layout = find(v, R.id.bg);
        titleView = find(v, R.id.dialog_title);// ("dialog_title_content");
        imgView = find(v, R.id.dialog_img_title);
        imgCloseButton = find(v, R.id.close_img_btn);
        content = find(v, R.id.dialog_content);
        messageView = find(v, R.id.dialog_message);
        scrollView = find(v, R.id.dialog_scroll);
        bgLayout = find(v, R.id.bg);

        scrollView.setFocusable(false);

        customContent = find(v, R.id.dialog_custom_content);

        buttonContent = find(v, R.id.dialog_button_content);
        positiveButton = find(v, R.id.dialog_button_positive);
        neutralButton = find(v, R.id.dialog_button_neutral);
        negativeButton = find(v, R.id.dialog_button_negative);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
        params.width = SysUtils.WIDTH * 3 / 4;
        layout.setLayoutParams(params);

        positiveButton.setOnClickListener(this);
        neutralButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
    }

    public static class Builder {
        private int width = -1;
        protected Context ctx;
        CharSequence title;
        int titleColor = -1;
        CharSequence message;
        private int background = -1;
        int titleImg;
        boolean visible;
        CharSequence positiveButtonText, negativeButtonText, neutralButtonText, closeBtnIconID;
        OnClickListener positiveButtonListener, negativeButtonListener, neutralButtonListener,
                closeButtonListener;
        CustomDialog dialog;
        boolean cancelable = true;
        OnCancelListener onCancelListener;
        OnKeyListener onKeyListener;

        CharSequence[] items;
        // OnClickListener onClickListener;

        ListAdapter listAdapter;

        AdapterView.OnItemSelectedListener onItemSelectedListener;
        int gravity = Gravity.CENTER_HORIZONTAL;
        View customView;

        public Builder(Context context) {
            ctx = context;
        }

        public Builder setTitle(int titleId) {
            title = ctx.getText(titleId);
            return this;
        }

        public Builder setBackground(int resID) {
            background = resID;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setTitleColor(int color) {
            this.titleColor = color;
            return this;
        }

        public Builder setMessage(int messageId) {
            message = ctx.getText(messageId);
            return this;
        }

        public Builder setTitleImg(int imgID) {
            titleImg = imgID;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }


        public Builder setPositiveButton(int textId, final OnClickListener listener) {
            positiveButtonText = ctx.getText(textId);
            positiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            positiveButtonText = text;
            positiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, final OnClickListener listener) {
            negativeButtonText = ctx.getText(textId);
            negativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            negativeButtonText = text;
            negativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, final OnClickListener listener) {
            neutralButtonText = ctx.getText(textId);
            neutralButtonListener = listener;
            return this;
        }

        public Builder setCloseButton(CharSequence iconID, final OnClickListener listener) {
            this.visible = true;
            closeBtnIconID = iconID;
            closeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, final OnClickListener listener) {
            neutralButtonText = text;
            neutralButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.onKeyListener = onKeyListener;
            return this;
        }

        public Builder setItems(int itemsId) {
            items = ctx.getResources().getTextArray(itemsId);
            // onClickListener = listener;
            return this;
        }

        public Builder setItems(String[] items) {
            this.items = items;
            // onClickListener = listener;
            return this;
        }

        public Builder setAdapter(final ListAdapter adapter) {
            listAdapter = adapter;
            // onClickListener = listener;
            return this;
        }

        public Builder setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
            onItemSelectedListener = listener;
            return this;
        }

        public Builder setView(View view) {
            customView = view;
            return this;
        }

        public Builder setMesgGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public CustomDialog create() {
            final CustomDialog d = new CustomDialog(ctx);
            d.titleView.setText(title);
            d.setView(customView);
            d.setTitleImage(titleImg);
            d.setMessage(message);
            d.setMesgGravity(gravity);
            if (-1 != width)
                d.setWidth(width);
            if (-1 != this.titleColor)
                d.titleView.setTextColor(this.titleColor);
            if (-1 != background)
                d.setBackground(background);
            if (positiveButtonText != "")
                d.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonText, positiveButtonListener);
            else
                d.positiveButton.setVisibility(View.GONE);

            if (negativeButtonText != "")
                d.setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonText, negativeButtonListener);
            else
                d.negativeButton.setVisibility(View.GONE);

            if (neutralButtonText != "")
                d.setButton(DialogInterface.BUTTON_NEUTRAL, neutralButtonText, neutralButtonListener);
            else
                d.neutralButton.setVisibility(View.GONE);
            if (visible) {
                d.setButton(BUTTON_CLOSE, closeBtnIconID, closeButtonListener);
            } else
                d.imgCloseButton.setVisibility(View.GONE);
            d.setCancelable(cancelable);
            d.setOnCancelListener(onCancelListener);

            if (onKeyListener != null) {
                d.setOnKeyListener(onKeyListener);
            }

            if (listAdapter == null && items != null)
                listAdapter =
                        new ArrayAdapter<CharSequence>(ctx, android.R.layout.select_dialog_item,
                                android.R.id.text1, items);

            if (listAdapter != null) {
                d.initListView(listAdapter, onItemSelectedListener);// , onItemSelectedListener);
            }
            return d;
        }

        public CustomDialog show() {
            dialog = create();
            dialog.show();
            return dialog;
        }

        public void dismiss() {
            dialog.dismiss();
        }
    }


}
