package com.oneside.base.utils;

import android.content.Intent;
import android.net.Uri;

import com.oneside.base.CardApplication;
import com.oneside.utils.LangUtils;

/**
 * Created by pingfu on 16-9-29.
 */
public class XXUtils {

    public static void callTelephone(String telephone) {
        if(LangUtils.isEmpty(telephone)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CardApplication.getApplication().startActivity(intent);
    }
}
