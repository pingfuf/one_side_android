package com.kuaipao.model;

import java.util.ArrayList;

import android.content.Context;

public class CityCombo {
    private Context mContext;
    private ArrayList<CityComboItem> mItems;

    public CityCombo(Context context) {
        mContext = context;
        mItems = new ArrayList<CityComboItem>();
    }

    public Context getContext() {
        return mContext;
    }

    public ArrayList<CityComboItem> getCity() {
        return mItems;
    }

    public void addItem(CityComboItem item) {
        mItems.add(item);
    }

    public void removeItem(CityComboItem item) {
        mItems.remove(item);
    }


}
