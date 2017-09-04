package com.oneside.base.rn;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.oneside.base.model.BasePageParam;
import com.oneside.utils.LangUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fupingfu on 2017/4/25.
 */
public class RNPageParam extends BasePageParam {
    public String scheme;

    public Map<String, String> params;

    public void addParam(String key, String value) {
        if (!LangUtils.isEmpty(key)) {
            if (params == null) {
                params = new HashMap<>();
            }

            params.put(key, value);
        }
    }

    public String buildScheme() {
        if (LangUtils.isEmpty(scheme)) {
            return "rn://android/main";
        }

        String str = scheme;
        if (params != null && params.size() > 0) {
            str += "?" + JSON.toJSONString(params);
        }

        return str;
    }
}
