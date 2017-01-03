package com.kuaipao.model;

import java.util.Calendar;

/**
 * Created by magi on 16/1/5.
 */
public class CalendarEvents {
    private String title;
    private String description;
    private static long startTime;

    private static long endTime;

    static {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 11);
        mCalendar.set(Calendar.MINUTE, 45);
        long start = mCalendar.getTime().getTime();
        mCalendar.set(Calendar.HOUR_OF_DAY, 12);
        long end = mCalendar.getTime().getTime();
        startTime = start;
        endTime = end;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    private void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return description;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
