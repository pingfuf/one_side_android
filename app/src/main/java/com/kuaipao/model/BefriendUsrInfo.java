package com.kuaipao.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.model.event.WebBaseEvent;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/2/17.
 */
public class BefriendUsrInfo extends WebBaseEvent {
    private ArrayList<Fans> usrList;
    private boolean hasMore = false;
    private int nextOffset = 0;

    public BefriendUsrInfo(boolean bResult) {
        super(bResult);
        usrList = new ArrayList<>();
    }

    public static BefriendUsrInfo fromJson(JSONObject json) {
        if (json == null || json.size() == 0) {
            return null;
        }
        boolean hasMore = WebUtils.getJsonBoolean(json, "has_more", false);
        int nextOffset = WebUtils.getJsonInt(json, "next_offset", 0);
        JSONArray array = WebUtils.getJsonArray(json, "user");
        List<Fans> usrList = new ArrayList<>();
        int l = array.size();
        for (int i = 0; i < l; i++) {
            JSONObject obj = WebUtils.getJsonObject(array, i);
            Fans f = Fans.fromJson(obj);
            usrList.add(f);
        }
        BefriendUsrInfo searchResult = new BefriendUsrInfo(false);
        searchResult.setHasMore(hasMore);
        searchResult.setNextOffset(nextOffset);
        searchResult.setUsrList(usrList);
        return searchResult;
    }

    public boolean hasMore() {
        return hasMore;
    }

    private void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getNextOffset() {
        return nextOffset;
    }

    private void setNextOffset(int nextOffset) {
        this.nextOffset = nextOffset;
    }

    private void setUsrList(List<Fans> usrList) {
        this.usrList.clear();
        if (LangUtils.isNotEmpty(usrList))
            this.usrList.addAll(usrList);
    }

    public ArrayList<Fans> getUsrList() {
        return usrList;
    }

}
