package com.kuaipao.model;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.manager.CardManager;
import com.kuaipao.utils.WebUtils;
import com.kuaipao.manager.R;

/**
 * Created by MVEN on 16/1/21.
 */
public class FitDataToday {

    /**
     * agoing_calories : 0   //正在进行的卡路里数
     * total_calories : 0    //今日健身总卡路里数
     * agoing_id : 0         //正在进行的健身记录id
     * total_minutes : 0     //今日健身总时间
     * finished : 无         //完成项目的文本描述
     * checkin_count : 0     //本月签到次数
     * agoing_minutes : 0    //正在进行的分钟数
     * desc : 还没有数据哦，赶快开始锻炼吧  //描述信息
     */

    public static final String FIT_DATA_AGOING_CALORI = "agoing_calories";
    public static final String FIT_DATA_TOTAL_CALORI = "total_calories";
    public static final String FIT_DATA_AGOING_ID = "agoing_id";
    public static final String FIT_DATA_TOTAL_MINUTES = "total_minutes";
    public static final String FIT_DATA_FINISHED = "finished";
    public static final String FIT_DATA_CHECKIN_COUNT = "checkin_count";
    public static final String FIT_DATA_AGOING_MINUTES = "agoing_minutes";
    public static final String FIT_DATA_DESC = "desc";
    private long agoing_calories = 0;
    private long total_calories = 0;
    private int agoing_id = 0;
    private int total_minutes = 0;
    private String finished = CardManager.getApplicationContext().getString(R.string.order_no_care_desc);
    private int checkin_count;
    private int agoing_minutes = 0;
    private String desc;

    public static FitDataToday fromJson(JSONObject json) {
        long agoing_calories = WebUtils.getJsonLong(json, FIT_DATA_AGOING_CALORI, 0l);
        long total_calories = WebUtils.getJsonLong(json, FIT_DATA_TOTAL_CALORI, 0l);
        int agoing_id = WebUtils.getJsonInt(json, FIT_DATA_AGOING_ID, 0);
        int total_minutes = WebUtils.getJsonInt(json, FIT_DATA_TOTAL_MINUTES, 0);
        String finished = WebUtils.getJsonString(json, FIT_DATA_FINISHED, CardManager.getApplicationContext().getString(R.string.order_no_care_desc));
        int checkin_count = WebUtils.getJsonInt(json, FIT_DATA_CHECKIN_COUNT, 0);
        int agoing_minutes = WebUtils.getJsonInt(json, FIT_DATA_AGOING_MINUTES, 0);
        String desc = WebUtils.getJsonString(json, FIT_DATA_DESC, "");
        FitDataToday fitD = new FitDataToday();
        fitD.setAgoing_calories(agoing_calories);
        fitD.setAgoing_id(agoing_id);
        fitD.setAgoing_minutes(agoing_minutes);
        fitD.setCheckin_count(checkin_count);
        fitD.setDesc(desc);
        fitD.setFinished(finished);
        fitD.setTotal_calories(total_calories);
        fitD.setTotal_minutes(total_minutes);
        return fitD;
    }

    public void setAgoing_calories(long agoing_calories) {
        this.agoing_calories = agoing_calories;
    }

    public void setTotal_calories(long total_calories) {
        this.total_calories = total_calories;
    }

    public void setAgoing_id(int agoing_id) {
        this.agoing_id = agoing_id;
    }

    public void setTotal_minutes(int total_minutes) {
        this.total_minutes = total_minutes;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public void setCheckin_count(int checkin_count) {
        this.checkin_count = checkin_count;
    }

    public void setAgoing_minutes(int agoing_minutes) {
        this.agoing_minutes = agoing_minutes;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getAgoing_calories() {
        return agoing_calories;
    }

    public long getTotal_calories() {
        return total_calories;
    }

    public int getAgoing_id() {
        return agoing_id;
    }

    public int getTotal_minutes() {
        return total_minutes;
    }

    public String getFinished() {
        return finished;
    }

    public int getCheckin_count() {
        return checkin_count;
    }

    public int getAgoing_minutes() {
        return agoing_minutes;
    }

    public String getDesc() {
        return desc;
    }
}
