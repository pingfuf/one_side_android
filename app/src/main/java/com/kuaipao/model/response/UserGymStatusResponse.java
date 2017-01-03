package com.kuaipao.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResponseData;
import com.kuaipao.base.net.model.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MVEN on 16/8/8.
 * <p/>
 * email: magiwen@126.com.
 */


public class UserGymStatusResponse extends BaseResult {
    @JSONField(name = "in_gym")
    public boolean isInGym;

}
