package com.oneside.model.beans;

import com.alibaba.fastjson.annotation.JSONField;
import com.oneside.base.inject.From;

import java.io.Serializable;

/**
 * Created by pingfu on 16-8-31.
 */
public class XUserPhysicalRecord implements Serializable {
    private static long serialVersionUID = 42L;

    public long id;

    /**
     * 体检时间
     */
    @JSONField(name = "exam_date")
    public String date;

    /**
     * 体脂 不是脂肪率
     */
    @JSONField(name = "body_fat")
    public float fatRate;

    /**
     * 肌肉重量
     */
    public float muscle;

    /**
     * 体重
     */
    public float weight;

    /**
     * 体检身份标识
     */
    public String identity;

    @JSONField(name = "report_url")
    public String reportUrl;

    public float bust;

    public float dbp;

    public float sbp;

    public float hips;

    public int score;

    public float height;

    @JSONField(name = "heart_rate")
    public float heartRate;

    public int status;

    public float waistline;

    public int age;

    @JSONField(name = "body_water")
    public float water;

    @JSONField(name = "fitness_instruction")
    public String suggestion;

    @JSONField(name = "lateral_view_eval")
    public String sideAssessment;

    @JSONField(name = "dorsal_view_eval")
    public String backAssessment;

    public float getBodyFatRate(){
        return fatRate / weight;
    }

    public String getShowBodyFatRate(){
        return  String.format("%.1f", getBodyFatRate() * 100) + "%";
    }
}
