package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.XActionNote;
import com.oneside.model.beans.XMember;
import com.oneside.model.beans.XOrder;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by MVEN on 16/8/2.
 * <p/>
 * email: magiwen@126.com.
 */
public class CoachCourseDetailResponse extends BaseResult {

    public long id;
    public XMember member;

    public XActionNote note;

    public XOrder order;

    public Record record;

    public boolean recordable;

    public CoachCourseDetailResponse(){
        member = new XMember();
        note = new XActionNote();
        order = new XOrder();
    }

    @JSONField(name = "start_time")
    public Date startTime;


    @Override
    public void arrangeResponseData(JSONObject jsonObject) {
        super.arrangeResponseData(jsonObject);
    }

    public static class Record implements Serializable {
        public int status;
        public long id;
        @JSONField(name = "order_id")
        public long orderId;

        @JSONField(name = "insert_time")
        public Date insertTime;
    }
}
