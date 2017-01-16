package com.oneside.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.ui.CustomDialog;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.ui.view.DialogSelectItemView;
import com.oneside.R;

import java.util.List;

import static com.oneside.utils.ViewUtils.createScaledBitmap;
import static com.oneside.utils.ViewUtils.getString;
import static com.oneside.utils.ViewUtils.rp;

/**
 * Created by Guo Ming on 05/05/15.
 */
public class CustomDialogHelper {

    /**
     * @param a
     * @param l new DialogInterface.OnClickListener() {
     *          <p/>
     *          public void onClick(DialogInterface dialog, int which) { int selectedPos =
     *          ((CustomDialog)dialog).getSelectedPosition(); w("OK clicked item selected %s", which); }
     *          }
     */
    public static void showPaySelectDialog(Activity a, DialogInterface.OnClickListener l, double money) {
        CustomDialog.Builder b =
                new CustomDialog.Builder(a).setTitle(R.string.dialog_pay_title)
                        .setPositiveButton(R.string.dialog_pay_go, l).setNegativeButton(R.string.no, l);
        PaySelectAdapter adapter = new PaySelectAdapter(a);
        b.setAdapter(adapter);
        RelativeLayout r = new RelativeLayout(a);
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        r.setLayoutParams(p);


        View topLine = new View(a);
        RelativeLayout.LayoutParams paramsLine1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLine1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        paramsLine1.height = 1;
        topLine.setBackgroundColor(a.getResources().getColor(R.color.line_gray));
        topLine.setLayoutParams(paramsLine1);
        r.addView(topLine);

        TextView title = new TextView(a);
        title.setText(getString(R.string.total_price1));
        title.setTextSize(16);
        title.setId(R.id.class_info_dialog_buy_title);

        title.setGravity(Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.height = ViewUtils.rp(56);
        r.addView(title, params);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView price = new TextView(a);
        price.setTextSize(16);
        price.setGravity(Gravity.CENTER_VERTICAL);
        price.setText(String.format(getString(R.string.rest_money), money));
        params1.addRule(RelativeLayout.CENTER_VERTICAL);
        params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params1.height = ViewUtils.rp(56);
        price.setTextColor(a.getResources().getColor(R.color.papaya_primary_color));
        r.addView(price, params1);

        View bottomLine = new View(a);
        RelativeLayout.LayoutParams paramsLine2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLine2.addRule(RelativeLayout.BELOW, R.id.class_info_dialog_buy_title);
        paramsLine2.height = 1;
        bottomLine.setBackgroundColor(a.getResources().getColor(R.color.line_gray));
        bottomLine.setLayoutParams(paramsLine2);
        r.addView(bottomLine);

        b.setView(r);
        b.setWidth(SysUtils.WIDTH * 9 / 10);
        b.show();
    }

    /**
     * @param dialog   CustomDialog Object
     * @param bitmapId rid of bitmap
     * @param message  The message to received
     */
    public static void setIconDialogMessage(CustomDialog dialog, int bitmapId, String message) {
        if (dialog == null) {
            return;
        }
        String replaceKey = "----";
        String s = replaceKey + "\n\n" + message + "\n";
        SpannableString ss = new SpannableString(s);
        int index = s.indexOf(replaceKey);
        Options option = new Options();
        int size = rp(36);
        option.outWidth = size;
        option.outHeight = size;
        Bitmap bitmap = null;
        if (bitmapId != -1) {
            bitmap = BitmapFactory.decodeResource(dialog.getContext().getResources(), bitmapId, option);
            bitmap = createScaledBitmap(bitmap, rp(36), rp(36));
        }
        ss.setSpan(new ImageSpan(dialog.getContext(), bitmap), index, index + replaceKey.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dialog.setMessage(ss);

    }

    public static void goToOtherMap() {

    }
}
