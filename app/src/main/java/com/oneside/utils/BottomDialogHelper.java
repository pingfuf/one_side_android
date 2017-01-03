package com.oneside.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.oneside.manager.CardManager;
import com.oneside.R;

import java.util.List;

/**
 * Created by MVEN on 16/5/13.
 */
public class BottomDialogHelper {


    private Context context = CardManager.getApplicationContext();
    private AlertDialog dlg;

    private View top, mid, bottom;

    private void showDeleteDialog(List<String> title) {
        if (LangUtils.isEmpty(title))
            return;

        dlg = new AlertDialog.Builder(context, R.style.BkgAlphaBlackDialog).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);
        int screenWidth = SysUtils.WIDTH;
        if (screenWidth > 0) {
            dlg.getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        Window window = dlg.getWindow();
        window.setContentView(R.layout.ui_circle_delete_layout);
        window.setGravity(Gravity.BOTTOM);
        int len = title.size();
    }
}
