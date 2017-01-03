package com.oneside.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.XUser;

import java.util.List;

/**
 * Created by MVEN on 16/6/28.
 * <p/>
 * email: magiwen@126.com.
 */


public class CoachReceiveCustomerListResponse extends BaseResult {
    public List<XUser> items;
}
