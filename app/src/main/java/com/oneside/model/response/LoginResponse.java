package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.XUser;
import com.oneside.utils.WebUtils;

/**
 * Created by pingfu on 16-10-25.
 */
public class LoginResponse extends BaseResult {
    @JSONField(name = "is_admin")
    public boolean isAdmin;

    public String name;

    public String token;

    @Override
    public void arrangeResponseData(JSONObject dataStr) {
        super.arrangeResponseData(dataStr);
    }
}
