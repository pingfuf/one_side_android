package com.kuaipao.model.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.Banner;
import com.kuaipao.model.beans.XCoach;
import com.kuaipao.model.beans.XCourse;
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
