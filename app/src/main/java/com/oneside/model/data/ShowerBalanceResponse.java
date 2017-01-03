package com.oneside.model.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResponseData;
import com.oneside.base.net.model.BaseResult;

import java.io.Serializable;

/**
 * Created by MVEN on 16/8/1.
 * <p/>
 * email: magiwen@126.com.
 */


public class ShowerBalanceResponse extends BaseResult implements Serializable {
//    free_minutes: 剩余的免费时间
//    remain_minutes： 剩余的时间
//    "shower_free_minutes": 5,
//    "shower_unit_price": 1,
//    "id": 3491
    private static final long serialVersionUID = -5894744583863771986L;
    public ShowerBalanceResponseData data;

    public static class ShowerBalanceResponseData extends BaseResponseData {

        @JSONField(name = "remain_minutes")
        public long timeLeft;

        @JSONField(name = "free_minutes")
        public long freeTime;

        public ShowerBalanceGymShower gym;

    }

    public static  class ShowerBalanceGymShower implements Serializable{
        @JSONField(name = "shower_free_minutes")
        public int gymFreeTime;

        @JSONField(name = "shower_unit_price")
        public long unitPrice;
    }
}
