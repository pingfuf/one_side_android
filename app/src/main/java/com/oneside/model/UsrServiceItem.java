package com.oneside.model;

import android.content.Intent;

/**
 * Created by magi on 15/12/25.
 */
public class UsrServiceItem {
    private int iconResID;
    private String name;
    private Intent intent;
    private int requestCode;
    private int unreadNum;


    public UsrServiceItem(int iconResID, String name, Intent intent, int requestCode) {
        this.iconResID = iconResID;
        this.name = name;
        this.intent = intent;
        this.requestCode = requestCode;
    }

    public void setIconResID(int iconResID) {
        this.iconResID = iconResID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSrvIntent(Intent intent) {
        this.intent = intent;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getIconResourceID() {
        return iconResID;
    }

    public String getName() {
        return name;
    }

    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }

    public int getUnreadNum() {
        return unreadNum;
    }

    public Intent getSrvIntent() {
        return intent;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
