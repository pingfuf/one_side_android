package com.oneside.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.Banner;
import com.oneside.model.beans.XCoach;
import com.oneside.model.beans.XCourse;
import java.util.List;

/**
 * 三次通卡课程列表返回结果
 *
 * Created by pingfu on 16-5-27.
 */
public class BannerListResponse extends BaseResult {
    public List<Banner> items;

    @Override
    public void arrangeResponseData(JSONObject dataStr) {
        super.arrangeResponseData(dataStr);
    }
}
