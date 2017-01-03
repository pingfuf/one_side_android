package com.kuaipao.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuaipao.base.view.XProcessView;
import com.kuaipao.manager.R;


public class CustomProgressDialog extends Dialog {
    //  private Context context = null;
    private static CustomProgressDialog customProgressDialog = null;

    public CustomProgressDialog(Context context) {
        super(context);
//      this.context = context;  
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static CustomProgressDialog createDialog(Context context) {
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.custom_progressdialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }

        XProcessView view = (XProcessView) customProgressDialog.findViewById(R.id.v_process);
        view.startAnim();
    }

    /**
     * [Summary]
     * setTitile 标题
     *
     * @param strTitle
     * @return
     */
    public CustomProgressDialog setTitile(String strTitle) {
        return customProgressDialog;
    }

    /**
     * [Summary]
     * setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.tv_loading_msg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);

            if (tvMsg.getVisibility() == View.GONE) {
                tvMsg.setVisibility(View.VISIBLE);
            }
        }

        return customProgressDialog;
    }
}  