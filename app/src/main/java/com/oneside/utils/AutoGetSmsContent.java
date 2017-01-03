package com.oneside.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

import com.oneside.R;

public class AutoGetSmsContent extends ContentObserver {

    public static final String SMS_BOX_URI = "content://sms";
    private static final String SMS_INBOX_URI = "content://sms/inbox";

    private static final String[] PROJECTION = new String[]{"_id"/*Telephony.Sms._ID*/, "body"/*Telephony.Sms.BODY*/};

    private Context mContext;
    private EditText mEditText = null;
    private long lastSmsId = 0;

    public AutoGetSmsContent(Handler handler, Activity context, EditText editText) {
        super(handler);
        this.mContext = context;
        this.mEditText = editText;
        try {
            fetchLastSmsId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchLastSmsId() {
        Cursor cursor = null;
        try {
            cursor =
                    mContext.getContentResolver().query(Uri.parse(SMS_INBOX_URI), PROJECTION, null, null,
                            "_id"/*Telephony.Sms._ID*/ + " DESC limit 1");
            if (cursor != null && cursor.getCount() > 0 && !cursor.isClosed()) {
                cursor.moveToFirst();
                lastSmsId = cursor.getLong(cursor.getColumnIndex("_id"/*Telephony.Sms._ID*/));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Cursor cursor = null;
        try {
            cursor =
                    mContext.getContentResolver().query(Uri.parse(SMS_INBOX_URI), PROJECTION, null, null,
                            "_id"/*Telephony.Sms._ID*/ + " DESC limit 1");
            if (cursor != null && cursor.getCount() > 0 && !cursor.isClosed()) {
                cursor.moveToFirst();
                long currentId = cursor.getLong(cursor.getColumnIndex("_id"/*Telephony.Sms._ID*/));
                if (currentId <= lastSmsId)
                    return;
                lastSmsId = currentId;

                String smsContent = cursor.getString(cursor.getColumnIndex("body"/*Telephony.Sms.BODY*/));
//        LogUtils.d(">>>> smsContent=%s, smsDate=%s", smsContent, smsDate);

                if (smsContent.contains(mContext.getString(R.string.app_name))) {
                    String regEx = "(?<![0-9])([0-9]{" + 6 + "})(?![0-9])";// 6-bit
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(smsContent);
                    while (m.find()) {
                        final String smsCode = m.group();
                        mEditText.post(new Runnable() {
                            @Override
                            public void run() {
                                mEditText.setText(smsCode);
                                mEditText.setSelection(mEditText.getText().length());
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}
