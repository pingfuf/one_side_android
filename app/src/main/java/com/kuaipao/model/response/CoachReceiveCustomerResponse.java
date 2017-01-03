package com.kuaipao.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResult;

/**
 * Created by pingfu on 16-8-18.
 */
public class CoachReceiveCustomerResponse extends BaseResult {
    private static long serialVersionUID = 42L;
    public long id;

    public String gender;

    public String phone;

    public String remark;

    public String source;

    @JSONField(name = "source_remark")
    public String sourceRemark;

    public int status;

    public int type;

    @JSONField(name = "type_remark")
    public String typeRemark;

    @JSONField(name = "user_id")
    public long userId;
}
