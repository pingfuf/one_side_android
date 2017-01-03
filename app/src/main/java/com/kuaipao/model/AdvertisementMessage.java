package com.kuaipao.model;

import com.alibaba.fastjson.JSONObject;

import static com.kuaipao.utils.LangUtils.formatTypeNormalDate;
import static com.kuaipao.utils.WebUtils.getJsonBoolean;
import static com.kuaipao.utils.WebUtils.*;

import java.io.Serializable;
import java.util.Date;

public class AdvertisementMessage implements Serializable, Comparable<AdvertisementMessage> {
    private static final long serialVersionUID = 1L;
    private static final String ID = "id";
    private static final String KEY_BITMAP = "logo_url";
    private static final String TITLE_BITMAP = "title";
    private static final String START_BITMAP = "start";
    private static final String END_BITMAP = "end";
    private static final String URL_BITMAP = "url";
    private static final String IS_NEW = "is_new";
    private static final String SHOW_LEVEL = "priority";
    private boolean isNew;
    private String mBitmapKey;
    private String mAdTitle;
    private String mAdStart;
    private String mAdEnd;
    private String mAdUrl;
    private int mAdPrio;
    private int id;

    public AdvertisementMessage(int id) {
        this.id = id;
    }

    public AdvertisementMessage(int id, String mBitmapKey, String mAdTitle, String mAdStart,
                                String mAdEnd, String mAdUrl, boolean isNew, int mAdPrio) {
        this.id = id;
        this.mBitmapKey = mBitmapKey;
        this.mAdTitle = mAdTitle;
        this.mAdStart = mAdStart;
        this.mAdEnd = mAdEnd;
        this.mAdUrl = mAdUrl;
        this.isNew = isNew;
        this.mAdPrio = mAdPrio;
    }

    public static AdvertisementMessage fromJson(JSONObject obj) {
        String mBitmapKey = getJsonString(obj, KEY_BITMAP, "");
        String mAdTitle = getJsonString(obj, TITLE_BITMAP, "");
        String mAdStart = getJsonString(obj, START_BITMAP, "");
        String mAdEnd = getJsonString(obj, END_BITMAP, "");
        String mAdUrl = getJsonString(obj, URL_BITMAP, "");
        int mAdPrio = Integer.parseInt(getJsonString(obj, SHOW_LEVEL, "0"));
        int mAdId = getJsonInt(obj, ID, 0);
        boolean mIsNew = getJsonBoolean(obj, IS_NEW, false);
        AdvertisementMessage ad =
                new AdvertisementMessage(mAdId, mBitmapKey, mAdTitle, mAdStart, mAdEnd, mAdUrl, mIsNew,
                        mAdPrio);
        return ad;
    }

    public void setAdPrio(int p) {
        this.mAdPrio = p;
    }

    public int getAdPrio() {
        return mAdPrio;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setBitmapKey(String mBitmapKey) {
        this.mBitmapKey = mBitmapKey;
    }

    public void setAdStart(String mAdStart) {
        this.mAdStart = mAdStart;
    }

    public void setAdEnd(String mAdEnd) {
        this.mAdEnd = mAdEnd;
    }

    public void setAdUrl(String mAdUri) {
        this.mAdUrl = mAdUrl;
    }

    public void setAdTitle(String mAdTitle) {
        this.mAdTitle = mAdTitle;
    }

    public String getBitmapKey() {
        return mBitmapKey;
    }

    public int getAdID() {
        return id;
    }

    public String getAdStart() {
        return mAdStart;
    }

    public String getAdEnd() {
        return mAdEnd;
    }

    public String getAdUrl() {
        return mAdUrl;
    }

    public String getAdTitle() {
        return mAdTitle;
    }

    public boolean getNew() {
        return isNew;
    }

    public String toString() {
        return "mBitmapKey:" + mBitmapKey + /*
                                         * "\tmAdTitle" + mAdTitle + "\tmAdStart:" + mAdStart +
                                         * "\tmAdEnd" + mAdEnd + "\tmAdUri:" + mAdUrl +
                                         */"\tmAdNew" + isNew;
    }

    @Override
    public int compareTo(AdvertisementMessage another) {
        Date mDate = formatTypeNormalDate(this.getAdStart());
        Date aDate = formatTypeNormalDate(another.getAdStart());
        if (mDate.after(aDate))
            return -1;
        else if (mDate.before(aDate))
            return 1;
        else
            return 0;
    }
}
