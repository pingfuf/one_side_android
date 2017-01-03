package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.CoachCourse;
import com.kuaipao.model.beans.Pagination;
import com.kuaipao.model.beans.XMember;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pingfu on 16-9-28.
 */
public class CoachCourseListResponse extends BaseResult {
    public List<CoachCourseItem> items;

    public boolean hasMore;

    public Date nextDay;

    public Pagination pagination;

    @Override
    public void arrangeResponseData(JSONObject dataObj) {
        super.arrangeResponseData(dataObj);

        if(pagination != null) {
            hasMore = pagination.nextDay != null;
            nextDay = pagination.nextDay;
        }

        JSONArray jsonArray = WebUtils.getJsonArray(dataObj, "items");
        if(items != null && jsonArray != null && items.size() == jsonArray.size()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            for(int i = 0; i < items.size(); i++) {
                CoachCourseItem item = items.get(i);

                if(item == null) {
                    continue;
                }

                if(item.date != null) {
                    item.time = dateFormat.format(item.date);
                }

                JSONObject itemObj = WebUtils.getJsonObject(jsonArray, i);
                JSONObject orderObj = WebUtils.getJsonObject(itemObj, "order");
                JSONObject courseObj = WebUtils.getJsonObject(orderObj, "individual_course");
                item.courseName = WebUtils.getJsonString(courseObj, "name");
            }
        }
    }

    public static class CoachCourseItem {
        public long id;

        @JSONField(name = "start_time")
        public Date date;

        public String time;

        public XMember member;

        public String courseName;

        public boolean showDate;

        @JSONField(name = "order_id")
        public long orderId;

        public boolean isUploadFailed;
    }
}
