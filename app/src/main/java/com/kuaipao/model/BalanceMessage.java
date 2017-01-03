package com.kuaipao.model;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.model.BaseResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kuaipao.utils.WebUtils.*;

public class BalanceMessage extends BaseResult{
    /**
     * remain : 84, 交易后的余额, 第一页第一条是最终余额
     * reason : 4,  交易类型, 可忽略
     * insert_time : 2015-08-25T21:21:27, 数据插入时间
     * income : 84, 收入金额
     * expense : 0, 支出金额
     * desc : 微信好友参与监督健身活动成功 + 0.84元
     */

    private float remain;
    private String reason;
    private String insert_time;
    private float income;
    private float expense;
    private String desc;

    public static BalanceMessage fromJson(JSONObject j) {
        int remain = getJsonInt(j, "remain", 0);
        String reason = getJsonString(j, "reason", "");
        String insert_titme = getJsonString(j, "insert_time", "");
        int income = getJsonInt(j, "income", 0);
        int expense = getJsonInt(j, "expense", 0);
        String desc = getJsonString(j, "desc", "");
        BalanceMessage bm = new BalanceMessage();
        bm.setReason(reason);
        bm.setDesc(desc);
        bm.setExpense(((float) expense) / 100);
        bm.setIncome(((float) income) / 100);
        bm.setInsert_time(insert_titme);
        bm.setRemain(((float) remain) / 100);
        return bm;
    }

    public void setRemain(float remain) {
        this.remain = remain;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public void setExpense(float expense) {
        this.expense = expense;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRemain() {
        return remain;
    }

    public String getReason() {
        return reason;
    }

    public Date getInsert_time() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(insert_time);
        } catch (ParseException e) {
            return null;
        }
    }

    public float getIncome() {
        return income;
    }

    public float getExpense() {
        return expense;
    }

    public String getDesc() {
        return desc;
    }

}
