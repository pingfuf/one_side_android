package com.kuaipao.pay;

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

import com.kuaipao.model.LocationCoordinate2D;
import com.kuaipao.ui.CustomDialog;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.SysUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.ui.view.DialogSelectItemView;
import com.kuaipao.manager.R;

import java.util.List;

import static com.kuaipao.utils.ViewUtils.createScaledBitmap;
import static com.kuaipao.utils.ViewUtils.getString;
import static com.kuaipao.utils.ViewUtils.rp;

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

    public static void goToOtherMap(final LocationCoordinate2D locationCoordinate2D,
                                    final String strGymLocation, final String strGymName, Activity a) {
        if (a == null || locationCoordinate2D == null) {
            return;
        }

        // http://developer.android.com/guide/components/intents-common.html#Maps
        StringBuilder geoLocation = new StringBuilder();
        geoLocation.append("geo:0,0?q=");
        geoLocation.append(strGymLocation == null ? "" : strGymLocation);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse(geoLocation.toString()));
        List<ResolveInfo> activities = a.getPackageManager().queryIntentActivities(intent, 0);
        if (LangUtils.isEmpty(activities)) {
            ViewUtils.showToast(a.getString(R.string.no_map_app), Toast.LENGTH_LONG);
        } else {
            showSelectMapDialog(a, activities, intent, locationCoordinate2D, strGymLocation, strGymName);
        }
        LogUtils.d("print open map activity %s", activities);

    }

//  private static void showSelectMapDialog(final Activity a, List<ResolveInfo> activities,
//      final Intent intent, final CardMerchant cardMerchant) {
//    if(cardMerchant == null)
//      return;
//
//    showSelectMapDialog(a, activities, intent,
//            cardMerchant.getLocationCoordinate2D(), cardMerchant.getLocation(), cardMerchant.getName());
//  }

    private static void showSelectMapDialog(final Activity a, List<ResolveInfo> activities,
                                            final Intent intent, final LocationCoordinate2D locationCoordinate2D,
                                            final String strGymLocation, final String strGymName) {
        if (LangUtils.isEmpty(activities)) {
            return;
        }
        final AlertDialog dlg = new AlertDialog.Builder(a, R.style.BkgAlphaBlackDialog).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);
        int screenWidth = SysUtils.WIDTH;
        if (screenWidth > 0) {
            dlg.getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_select_map);
        window.setGravity(Gravity.BOTTOM);
        LinearLayout mapLayout = (LinearLayout) window.findViewById(R.id.dialog_select_map_layout);
        for (final ResolveInfo activity : activities) {
            DialogSelectItemView view = new DialogSelectItemView(a);
            try {
                ApplicationInfo ai =
                        a.getPackageManager().getApplicationInfo(activity.activityInfo.packageName, 0);
                CharSequence name = ai.loadLabel(a.getPackageManager());
                Drawable d = ai.loadIcon(a.getPackageManager());
                view.setName(name);
                view.setImage(d);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            int type = 0;
            if (activity.activityInfo.packageName.contains("com.autonavi")) {
                Intent ii = new Intent(Intent.ACTION_VIEW);
                String amapUri =
                        LangUtils
                                .format(
                                        "androidamap://route?sourceApplication=%s&slat=0.0&slon=0.0&sname=&dlat=%s&dlon=%s&dname=%s&dev=0&m=0&t=2",
                                        a.getPackageName(), 0.0, 0.0, strGymLocation);
                ii.setData(Uri.parse(amapUri));
                List<ResolveInfo> aas = a.getPackageManager().queryIntentActivities(ii, 0);
                for (final ResolveInfo aa : aas) {
                    if (aa.activityInfo.packageName.equals(activity.activityInfo.packageName)) {
                        type = 1;
                    }
                }
                // type = 1;
            } else if (activity.activityInfo.packageName.contains("com.baidu")) {
                Intent ii = new Intent(Intent.ACTION_VIEW);
                String baiduUri =
                        "bdapp://map/direction?origin=&destination=" /*+ strGymName + " "*/
                                + strGymLocation + "&mode=driving&src=小熊快跑";
                ii.setData(Uri.parse(baiduUri));
                List<ResolveInfo> aas = a.getPackageManager().queryIntentActivities(ii, 0);
                for (final ResolveInfo aa : aas) {
                    if (aa.activityInfo.packageName.equals(activity.activityInfo.packageName)) {
                        type = 2;
                    }
                }
                // type = 2;
            } else if (activity.activityInfo.packageName.contains("com.sogou")) {
                type = 3;
            } else if (activity.activityInfo.packageName.contains("com.mapbar")) {
                type = 4;
            }
            view.setTag(type);
            view.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    dlg.cancel();
                    int type = (Integer) v.getTag();
                    switch (type) {
                        case 0:// others
                            String uri =
                                    LangUtils.format("geo:0,0?q=%s,%s(%s)", locationCoordinate2D.getLatitude(),
                                            locationCoordinate2D.getLongitude(), strGymLocation);
                            intent.setData(Uri.parse(uri));
                            break;
                        case 1:// amap
                            // http://lbs.amap.com/api/uri-api/android-uri-explain/
                            String amapUri =
                                    LangUtils
                                            .format(
                                                    "androidamap://route?sourceApplication=%s&slat=0.0&slon=0.0&sname=&dlat=%s&dlon=%s&dname=%s&dev=0&m=0&t=2",
                                                    a.getPackageName(), locationCoordinate2D.getLatitude(), locationCoordinate2D.getLongitude(),
                                                    strGymLocation);
                            intent.setData(Uri.parse(amapUri));
                            break;
                        case 2:// baidu
                            // http://developer.baidu.com/map/uri-introandroid.htm
                            String baiduUri =
                                    "bdapp://map/direction?origin=&destination=" + strGymName + " "
                                            + strGymLocation + "&mode=driving&src=小熊快跑";
                            intent.setData(Uri.parse(baiduUri));

                            break;

                        case 3:// sogou
                        case 4:// mapbar
                            break;

                        default:
                            break;
                    }
                    LogUtils.d("start map data = %s", intent.getData());
                    intent.setPackage(activity.activityInfo.packageName);
                    try {
                        a.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            LayoutParams params = new LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            mapLayout.addView(view, params);
        }
        Button cancelButton = (Button) window.findViewById(R.id.dialog_select_map_cancel_share);


        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        ((ViewGroup) mapLayout.getParent()).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                LogUtils.w("dddddddddd  %s", v);
            }
        });
    }


}
