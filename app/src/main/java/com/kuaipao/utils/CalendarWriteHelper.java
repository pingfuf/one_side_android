package com.kuaipao.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.format.Time;
import android.widget.Toast;

import com.kuaipao.manager.CardManager;
import com.kuaipao.model.CardErpOrder;
import com.kuaipao.model.CardOrder;

/**
 * Created by magi on 16/1/5.
 */
public class CalendarWriteHelper {
    private static String calendarURL = "";
    private static String calendarEventURL = "";
    private static String calendarRemiderURL = "";
    private static int REMINDER_BEFORE_MINUTES = 60;
    private static int MAX_HOURS_ALL_DAY = 3;
    private static Context mContext = CardManager.getApplicationContext();

    //2.2以后url发生改变
    static {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            calendarURL = "content://com.android.calendar/calendars";
            calendarEventURL = "content://com.android.calendar/events";
            calendarRemiderURL = "content://com.android.calendar/reminders";

        } else {
            calendarURL = "content://calendar/calendars";
            calendarEventURL = "content://calendar/events";
            calendarRemiderURL = "content://calendar/reminders";
        }
    }

    public static void writeEvents(CardOrder events) {
        String calID = "";
        Cursor userCursor = mContext.getContentResolver().query(Uri.parse(calendarURL), null,
                null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            calID = userCursor.getString(userCursor.getColumnIndex("_id"));
        } else {
            ViewUtils.showToast("添加失败！", Toast.LENGTH_SHORT);
            return;
        }
//        delExists(events, mContext);
        ContentValues eventC = new ContentValues();
        eventC.put(Events.TITLE, events.getClassName());
        eventC.put(Events.DESCRIPTION, events.getNotice());
        eventC.put(Events.CALENDAR_ID, calID);
        eventC.put(Events.EVENT_TIMEZONE, Time.getCurrentTimezone());
        if (LangUtils.hoursBetweenDate(events.getStartTime(), events.getEndTime()) > MAX_HOURS_ALL_DAY) {

        }
        eventC.put(Events.DTSTART, events.getStartTime().getTime());
        eventC.put(Events.DTEND, events.getEndTime().getTime());

        eventC.put(Events.HAS_ALARM, 1);
        Uri newEvent = mContext.getContentResolver().insert(Uri.parse(calendarEventURL), eventC);
        long id = Long.parseLong(newEvent.getLastPathSegment());
        ContentValues reminderC = new ContentValues();
        reminderC.put(CalendarContract.Reminders.EVENT_ID, id);
        reminderC.put(CalendarContract.Reminders.MINUTES, REMINDER_BEFORE_MINUTES);
        mContext.getContentResolver().insert(Uri.parse(calendarRemiderURL), reminderC);
        ViewUtils.showToast("添加成功！", Toast.LENGTH_SHORT);
    }

    public static void writeEvents(CardErpOrder events) {
        String calID = "";
        Cursor userCursor = mContext.getContentResolver().query(Uri.parse(calendarURL), null,
                null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            calID = userCursor.getString(userCursor.getColumnIndex("_id"));
        } else {
            ViewUtils.showToast("添加失败！", Toast.LENGTH_SHORT);
            return;
        }
//        delExists(events, mContext);
        ContentValues eventC = new ContentValues();
        eventC.put(Events.TITLE, events.getClassName());
        eventC.put(Events.DESCRIPTION, "");
        eventC.put(Events.CALENDAR_ID, calID);
        eventC.put(Events.EVENT_TIMEZONE, Time.getCurrentTimezone());

        eventC.put(Events.DTSTART, events.getStartDate().getTime());
        eventC.put(Events.DTEND, events.getEndDate().getTime());

        eventC.put(Events.HAS_ALARM, 1);
        Uri newEvent = mContext.getContentResolver().insert(Uri.parse(calendarEventURL), eventC);
        long id = Long.parseLong(newEvent.getLastPathSegment());
        ContentValues reminderC = new ContentValues();
        reminderC.put(CalendarContract.Reminders.EVENT_ID, id);
        reminderC.put(CalendarContract.Reminders.MINUTES, REMINDER_BEFORE_MINUTES);
        mContext.getContentResolver().insert(Uri.parse(calendarRemiderURL), reminderC);
        ViewUtils.showToast("添加成功！", Toast.LENGTH_SHORT);
    }

    public static boolean isEventInCal(CardOrder events) {

        Cursor cursor = mContext.getContentResolver().query(Uri.parse(calendarEventURL), new String[]{"title", "description", "dtstart"}, "title=? and description=? and dtstart=?",
                new String[]{events.getClassName(), events.getNotice(), String.valueOf(events.getStartTime().getTime())}, null);

        if (cursor != null && cursor.moveToFirst()) {
            //Yes Event Exist...
            return true;
        }
        return false;
    }

    public static boolean isEventInCal(CardErpOrder events) {

        Cursor cursor = mContext.getContentResolver().query(Uri.parse(calendarEventURL), new String[]{"title", "description", "dtstart"}, "title=? and description=? and dtstart=?",
                new String[]{events.getClassName(), "", String.valueOf(events.getStartDate().getTime())}, null);

        if (cursor != null && cursor.moveToFirst()) {
            //Yes Event Exist...
            return true;
        }
        return false;
    }

    public static boolean delExists(CardOrder events, Context mContext) {
        Uri CALENDAR_URI = Uri.parse(calendarEventURL);
        ContentResolver resolver = mContext.getContentResolver();
        int delState = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            Cursor c = resolver.query(CALENDAR_URI, null, null, null, null);
            if (c.moveToFirst()) {
                while (c.moveToNext()) {
                    String desc = c.getString(c.getColumnIndex("description"));
                    String title = c.getString(c.getColumnIndex("title"));
                    String startTime = c.getString(c.getColumnIndex("dtstart"));

                    String id = c.getString(c.getColumnIndex("_id"));
                    if (!(desc == null && title == null && startTime == null)) {
                        if (desc.equals(events.getNotice()) && title.equals(events.getClassName()) && startTime.equals(String.valueOf(events.getStartTime().getTime()))) {
                            Uri uri = ContentUris.withAppendedId(CALENDAR_URI, Integer.parseInt(id));
                            delState = resolver.delete(uri, null, null);
                        }
                    }
                }
            }
        } else {
            delState = resolver.delete(CALENDAR_URI,
                    "title=? and description=? and dtstart=?",
                    new String[]{events.getClassName(), events.getNotice(), String.valueOf(events.getStartTime().getTime())})/* == -1 ? false : true*/;
        }
        return delState != -1;
    }

}
