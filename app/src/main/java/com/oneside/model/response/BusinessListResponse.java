package com.oneside.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.net.model.BaseResponseData;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.beans.XBusinessArea;
import com.oneside.model.beans.XBusinessDistrict;

import java.util.List;

/**
 * 商圈列表接口返回结果
 * <p/>
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-17
 * Time: 17:05
 * Author: pingfu
 * FIXME
 */
public class BusinessListResponse extends BaseResult {
    private static long serialVersionUID = 42L;

    public BusinessListResponseData data;

    public static class BusinessListResponseData extends BaseResponseData {
        /**
         * 热门商业区
         */
        @JSONField(name = "hot_blocks")
        public List<XBusinessArea> hotDistrict;

        /**
         * 商业区列表
         */
        @JSONField(name = "district_blocks")
        public List<XBusinessDistrict> businessDistricts;
    }
}