package com.oneside.base.rn;

import android.text.TextUtils;

import com.oneside.base.model.BasePageParam;

import java.util.Map;

/**
 * Created by fupingfu on 2017/4/25.
 */
public class RNPageParam extends BasePageParam {
    public String scheme;

    public Map<String, String> params;

    public String getRNParams() {
        String json = "{}";
        if (params != null && params.size() > 0) {
            for (String key: params.keySet()) {
                json = "{";
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(params.get(key))) {
                    json += "\"" + key + "\":" + params.get(key);
                }
            }
        }

        return json;
    }
}
