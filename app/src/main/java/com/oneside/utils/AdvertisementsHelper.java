package com.oneside.utils;

import static com.oneside.utils.IOUtils.getPreferenceValue;
import static com.oneside.utils.LangUtils.isEmpty;
import static com.oneside.utils.WebUtils.*;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.model.AdvertisementMessage;

public class AdvertisementsHelper {
    static ArrayList<AdvertisementMessage> adListsOld = new ArrayList<AdvertisementMessage>();
    static ArrayList<AdvertisementMessage> adLists = new ArrayList<AdvertisementMessage>();

    public static ArrayList<AdvertisementMessage> fetchSDAdvertisements(JSONObject adData) {
        adListsOld.clear();
        JSONArray array;
        try {
            array = adData.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject j = (JSONObject) array.get(i);
                adListsOld.add(AdvertisementMessage.fromJson(j));
            }
            return adListsOld;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<AdvertisementMessage> fetchNetAdvertisements(JSONArray array)
            throws JSONException {
        adLists.clear();
        for (int i = 0; i < array.size(); i++) {
            JSONObject j = (JSONObject) array.get(i);
            String mBitmapKey = getJsonString(j, "logo_url", null);
            String mAdTitle = getJsonString(j, "title", null);
            String mAdStart = getJsonString(j, "start", null);
            String mAdEnd = getJsonString(j, "end", null);
            String mAdUrl = getJsonString(j, "url", null);
            int id = getJsonInt(j, "id", 0);
            int mAdPrio = LangUtils.parseInt(getJsonString(j, "priority", "0"));
            AdvertisementMessage ad =
                    new AdvertisementMessage(id, mBitmapKey, mAdTitle, mAdStart, mAdEnd, mAdUrl, true,
                            mAdPrio);
            adLists.add(ad);
        }
        return adLists;
    }

    /**
     * Fetch advertisement of last or new
     *
     * @return 2 both location and net have nothing
     */
    public int fetchFromSD() {
        String strADdata = getPreferenceValue(Constant.AD_SAVE_DATA);
        if (!isEmpty(strADdata)) {
            JSONObject adData = parseJsonObject(strADdata);
            if (adData != null && adData.size() >= 0) {
                return parseAdvertisements(adData);
            }
        }
        return Constant.AD_LOCATION_NOTHING;

    }

    private int hasNew;

    private int parseAdvertisements(JSONObject adData) {
        hasNew = Constant.AD_LOCATION_NOTHING;
        adListsOld.clear();
        adListsOld.addAll(AdvertisementsHelper.fetchSDAdvertisements(adData));
        if (isEmpty(adListsOld)) {
            hasNew = Constant.AD_LOCATION_NOTHING;
        }
        for (AdvertisementMessage ad1 : adListsOld) {
            if (ad1.getNew()) {
                hasNew = Constant.AD_LOCATION_CONTAINS_NEW;
                break;
            }
        }
        if (hasNew != Constant.AD_LOCATION_NOTHING && hasNew != Constant.AD_LOCATION_CONTAINS_NEW)
            hasNew = Constant.AD_LOCATION_CONTAINS_SKIMED;
        return hasNew;

    }

}
