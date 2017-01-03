package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResponseData;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.MembershipCard;
import com.oneside.model.beans.XCoupon;
import com.oneside.model.beans.XMember;

import java.util.Date;
import java.util.List;

/**
 * 用户优惠券列表返回结果
 * <p/>
 * Created by pingfu on 16-6-27.
 */
public class MemberDetailInfoResponse extends BaseResult {
    private static long serialVersionUID = 42L;

    /**
     * 头像
     */
    public String avatar;

    public String name;

    public String phone;

    public String gender;

    public int status;

    /**
     * 生日
     */
    public String birthday;

    public long id;

    @JSONField(name = "mc_count")
    public int membershipCardCount;

    @JSONField(name = "mc_end_date")
    public Date membershipCardExpiredTime;

    @JSONField(name = "refund_date")
    public Date refundDate;

    @JSONField(name = "membership_card")
    public MembershipCard card;

    @Override
    public void arrangeResponseData(JSONObject jsonObject) {
        super.arrangeResponseData(jsonObject);
        if(card != null) {
            status = card.status;
        }
    }
}
