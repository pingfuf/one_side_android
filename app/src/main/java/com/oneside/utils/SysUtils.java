package com.oneside.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.oneside.manager.CardManager;

import java.io.File;
import java.util.List;
import java.util.UUID;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

import static com.oneside.utils.LogUtils.D;
import static com.oneside.utils.LogUtils.d;
import static com.oneside.utils.LogUtils.e;
import static com.oneside.utils.LogUtils.w;

/**
 * Created by Guo Ming on 3/31/15.
 */
@SuppressLint("NewApi")
public class SysUtils {
    private static final long MIN_STORAGE_SIZE = 1024L * 1024 * 2;

    public static String mUniquePsuedoID;

    public static String TELE_DEVICE_ID;
    public static String ANDROID_ID;
    public static String DEVICE_NAME;
    public static String MODEL_NAME;
    public static String VERSION_NAME;
    public static String APP_VERSION_NAME;
    public static String BRAND_NAME;
    public static String MANIFACTURE_NAME;
    public static int    APP_VERSION_CODE;

    public static String WIFI_MAC;
    public static String PHONE_NUMBER;

    public static int WIDTH;
    public static int HEIGHT;
    public static int DENSITY;
    public static boolean EXTERNAL_STORAGE_WRITABLE = false;

    public static final int NETWORK_TYPE_WIFI = 1;
    public static final int NETWORK_TYPE_WAP = 2;
    public static final int NETWORK_TYPE_NET = 3;
    public static final int NETWORK_TYPE_NULL = 0;

    public static String MARKET_PACKAGE_NAME;
    public static String MARKET_LAUNCH_ACTIVITY;

    public static int API_LEVEL = 3;

    public static String SOURCE = null;

    private SysUtils() {
    }

    /**
     * Initialize class from telephone configuration.
     *
     * @param ctx
     */
    public static void initialize(@NonNull Context ctx) {
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            TELE_DEVICE_ID = tm.getDeviceId();
            DEVICE_NAME = tm.getSubscriberId();
        } catch (Exception e) {
            if (D)
                d("Failed to get tele info: " + e);
        }

        try {
            ANDROID_ID = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            w(e, "Failed to get ANDROID_ID");
        }

        if (LangUtils.isEmpty(ANDROID_ID))
            ANDROID_ID = "emulator";

        if (DEVICE_NAME == null)
            DEVICE_NAME = "";

        MODEL_NAME = Build.DEVICE;
        VERSION_NAME = Build.VERSION.RELEASE;
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            APP_VERSION_NAME = pInfo.versionName;
            APP_VERSION_CODE = pInfo.versionCode;
        } catch (NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
//          APP_VERSION_NAME = BuildConfig.VERSION_NAME;
        }

        BRAND_NAME = Build.BRAND;
        MANIFACTURE_NAME = Build.MANUFACTURER;

        try {
            WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
                WIDTH = display.getWidth();
                HEIGHT = display.getHeight();
            } else {
                Point size = new Point();
                display.getSize(size);
                WIDTH = size.x;
                HEIGHT = size.y;
            }
            DisplayMetrics metric = new DisplayMetrics();
            display.getMetrics(metric);
            DENSITY = metric.densityDpi;
        } catch (Exception e) {
            e(e, "Failed to get display info");
        }

        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File tmp = new File(Environment.getExternalStorageDirectory(), "__ppy_tmp");
                if (tmp.exists())
                    tmp.delete();
                if (tmp.createNewFile()) {
                    SysUtils.EXTERNAL_STORAGE_WRITABLE = true;
                    tmp.delete();
                }
            }
        } catch (Exception e) {
            if (D)
                d("Failed to test external storage writable: " + e);
        }
        try {// get MAC of WIFI by GM
            WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (info.getMacAddress() != null) {
                WIFI_MAC = info.getMacAddress();
            } else {
                WIFI_MAC = "";
            }
        } catch (Exception e) {
            if (D)
                d("Failed to get WIFI MAC: " + e);
        }

        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getLine1Number() != null) {
                PHONE_NUMBER = tm.getLine1Number();
            } else {
                PHONE_NUMBER = "";
            }
        } catch (Exception e) {
            if (D)
                d("Failed to get phone number: " + e);
        }

        API_LEVEL = LangUtils.parseInt(Build.VERSION.SDK, 3);
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                SOURCE = appInfo.metaData.getString("UMENG_CHANNEL");
                if (LangUtils.isEmpty(SOURCE)) {
                    int valueInt = appInfo.metaData.getInt("UMENG_CHANNEL", 0);
                    SOURCE = valueInt > 0 ? String.valueOf(valueInt) : "";
                }
            } else {
                SOURCE = "";
            }

        } catch (NameNotFoundException e) {
            SOURCE = "";
        }

        mUniquePsuedoID = getUniquePsuedoID();
    }

    /**
     * Pseudo-Unique ID - Software (for all Android devices)
     * Return pseudo unique ID
     *
     * @return ID
     * <p/>
     * http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/
     */
    public static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) +
                (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * Check if a Activity supports a specific Intent object.
     *
     * @param i
     * @return boolean
     */
    public static boolean supportIntent(Intent i) {
        try {
            if (i == null)
                return false;
            List<ResolveInfo> activities = CardManager.getApplicationContext().getPackageManager()
                    .queryIntentActivities(i, 0);
            return activities != null && activities.size() > 0;
        } catch (Exception e) {
            e(e, "Failed in supportIntent");
        }

        return false;
    }

    /**
     * Get the amount of free space in filesystem.
     *
     * @param path
     * @return long
     */
    public static long getFreeSpaceSize(@CheckForNull String path) {
        long free = 0;
        if (path != null) {
            try {
                StatFs fs = new StatFs(path);
                free = 1L * fs.getAvailableBlocks() * fs.getBlockSize();
                LogUtils.i("freespace %d at %s", free, path);
            } catch (Exception e) {
                w(e, "Failed to get free space size at " + path);
            }
        }

        return free;
    }

    /**
     * Check if it as free space in filesystem.
     *
     * @param ctx
     */
    public static void checkFreeSpace(@CheckForNull Context ctx) {
        try {
            if (ctx != null) {
                if (getFreeSpaceSize(ctx.getFilesDir().getAbsolutePath()) < MIN_STORAGE_SIZE)
                    // ViewUtils.showToast(Papaya.getString("sys_nospace"),
                    // Toast.LENGTH_LONG);
                    if (EXTERNAL_STORAGE_WRITABLE
                            && getFreeSpaceSize(Environment.getExternalStorageDirectory().getAbsolutePath()) < MIN_STORAGE_SIZE) {
                        // ViewUtils.showToast(Papaya.getString("sys_nospace"),
                        // Toast.LENGTH_LONG);
                    }
            }
        } catch (Exception e) {
            w(e, "Failed to checkFreeSpace");
        }
    }

    /**
     * Check the connectivity of network.
     *
     * @return boolean
     */
    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(null);
    }

    /**
     * Check the connectivity of network, can be false positive
     *
     * @param ctx
     * @return boolean
     */
    public static boolean isNetworkAvailable(Context ctx) {
        try {
            if (ctx == null)
                ctx = CardManager.getApplicationContext();
            if (ctx == null)
                return false;
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return false;
            }

            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        } catch (Exception e) {
            LogUtils.w("Failed to check network status: " + e); //$NON-NLS-1$
            return true; // return a false-positive result
        }
    }

    /**
     * Get the type of network
     *
     * @return NETWORK_TYPE
     */
    public static int getNetworkType() {
        Context con = CardManager.getApplicationContext();
        ConnectivityManager conn = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn == null)
            return NETWORK_TYPE_NULL;
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info == null)
            return NETWORK_TYPE_NULL;
        String type = info.getTypeName();// MOBILE��GPRS��;WIFI

        LogUtils.d("get net type NetworkType= %s", type);

        if (type.equals("WIFI") || type.equals("wifi")) {
            return NETWORK_TYPE_WIFI;
        } else if (type.equals("MOBILE") || type.equals("mobile")) {
            String apn = info.getExtraInfo();// getAPN(this);
            LogUtils.v("tag", "APN=" + apn);
            if (apn != null && apn.contains("wap"))
                return NETWORK_TYPE_WAP;
            else
                return NETWORK_TYPE_NET;
        }
        return NETWORK_TYPE_NULL;
    }
}
