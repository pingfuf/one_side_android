package com.kuaipao.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResponseData;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.XCoach;
import com.kuaipao.model.beans.XGym;

/**
 * ERP课程详情接口返回结果
 *
 * Created by pingfu on 16-6-1.
 */
public class OpenDoorQrPicResponse extends BaseResult {
    @JSONField(name = "expires_in")
    public int expiredIn;

    public String qrcode;
}
