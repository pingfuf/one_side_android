package com.oneside.model;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class CityComboItem {

    private Context context;
    private String title;
    private String warn;
    private ArrayList<String> cityLists;
    private Drawable icon;

    public CityComboItem(Context context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public String getWarn() {
        return warn;
    }

    public Context getContext() {
        return context;
    }

    public ArrayList<String> getCityLists() {
        return cityLists;
    }

    public Drawable getDrawable() {
        return icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public void setCityLists(ArrayList<String> cl) {
        if (cityLists == null) {
            cityLists.addAll(cl);
        }
    }

    public void setIcon(int resId) {
        this.icon = context.getResources().getDrawable(resId);
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        CityComboItem t = (CityComboItem) o;
        return this.title.equals(t.title) && this.warn.equals(t.warn)
                && this.cityLists.size() == t.cityLists.size();
    }


}
