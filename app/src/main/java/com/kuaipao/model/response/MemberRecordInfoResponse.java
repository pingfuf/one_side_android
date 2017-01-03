package com.kuaipao.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.XReceiveRecord;
import com.kuaipao.model.beans.XUserPhysicalRecord;

/**
 * Created by pingfu on 16-6-23.
 */
public class MemberRecordInfoResponse extends BaseResult {
    private static long serialVersionUID = 42L;

    @JSONField(name = "follow_up")
    public ReceiveRecordInfo receiveRecordInfo;

    @JSONField(name = "individual_course")
    public IndividualCourse individualCourse;

    @JSONField(name = "physical_exam")
    public PhysicalExamInfo physicalExamInfo;

    public static class ReceiveRecordInfo {
        @JSONField(name = "latest_item")
        public XReceiveRecord receiveRecord;

        @JSONField(name = "total_count")
        public int totalCount;
    }

    public static class IndividualCourse {
        @JSONField(name = "total_count")
        public int totalCount;

        @JSONField(name = "remain_count")
        public int remainCount;
    }

    public static class PhysicalExamInfo {
        @JSONField(name = "latest_item")
        public XUserPhysicalRecord physicalExam;

        @JSONField(name = "total_count")
        public int totalCount;
    }
}
