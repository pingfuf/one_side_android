package com.kuaipao.model;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.model.event.WebBaseEvent;
import com.kuaipao.utils.WebUtils;

import java.io.Serializable;
import java.text.DecimalFormat;

public final class CardYear extends WebBaseEvent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1l;

    private String card_id;
    private long gym_id;
    private String title;
    private String desc;
    private String short_desc;
    private String process_desc;
    private int period;
    private long org_price;
    private long sell_price;
    private String phone;
    private String gym_name;
    private String period_desc;
    private String buy_desc;
    private boolean is_erp;

    public CardYear(boolean bResult) {
        super(bResult);
    }

    public static CardYear fromJson(JSONObject json) {
        String card_id = WebUtils.getJsonString(json, "id", "");
        long gym_id = WebUtils.getJsonLong(json, "gid", -1l);
        String title = WebUtils.getJsonString(json, "title", "");
        String desc = WebUtils.getJsonString(json, "desc", "");
        String process_desc = WebUtils.getJsonString(json, "process_desc", "");
        String period_desc = WebUtils.getJsonString(json, "period_desc", "");
        String short_desc = WebUtils.getJsonString(json, "short_desc", "");
        String phone = WebUtils.getJsonString(json, "phone", "");
        long org_price = WebUtils.getJsonLong(json, "org_price", -1l);
        long sell_price = WebUtils.getJsonLong(json, "sell_price", -1l);
        String buy_desc = WebUtils.getJsonString(json, "buy_desc", "");
        boolean is_erp = WebUtils.getJsonBoolean(json, "is_erp", false);
        CardYear y = new CardYear(true);
        y.setBuyDesc(buy_desc);
        y.setPhone(phone);
        y.setCardID(card_id);
        y.setDesc(desc);
        y.setGID(gym_id);
        y.setOrgPrice(org_price);
        y.setPeriodDesc(period_desc);
        y.setProcessDesc(process_desc);
        y.setSellPrice(sell_price);
        y.setShortDesc(short_desc);
        y.setTitle(title);
        y.setErp(is_erp);
        return y;

    }

    public JSONObject jsonDict() {
        JSONObject dict = new JSONObject();
        try {
            dict.put("id", card_id);
            dict.put("gid", gym_id);
            dict.put("title", title);
            dict.put("desc", desc);
            dict.put("phone", phone);
            dict.put("process_desc", process_desc);
            dict.put("short_desc", short_desc);
            dict.put("period_desc", period_desc);
            dict.put("org_price", org_price);
            dict.put("sell_price", sell_price);
            dict.put("buy_desc", buy_desc);
            dict.put("is_erp",is_erp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dict;
    }

    public void setGID(long gid) {
        gym_id = gid;
    }

    public long getGID() {
        return gym_id;
    }

    public void setCardID(String card_id) {
        this.card_id = card_id;
    }

    public void setGYMName(String name) {
        gym_name = name;
    }

    public String getGYMName() {
        return gym_name;
    }

    public String getBuyDesc() {
        return buy_desc;
    }

    public void setBuyDesc(String buy_desc) {
        this.buy_desc = buy_desc;
    }

    public String getCardID() {
        return card_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setErp(boolean is_erp) {
        this.is_erp = is_erp;
    }

    public boolean isERP() {
        return is_erp;
    }

    public String getTitle() {
        return title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setShortDesc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getShortDesc() {
        return short_desc;
    }

    public void setProcessDesc(String process_desc) {
        this.process_desc = process_desc;
    }

    public String getProcessDesc() {
        return process_desc;
    }

    public void setOrgPrice(long org_price) {
        this.org_price = org_price;
    }

    public long getOrgPrice() {
        return org_price;
    }

    public String getOrgPriceDesc() {
        return new DecimalFormat("0.##").format(((double) org_price) / 100);
    }

    public void setSellPrice(long sell_price) {
        this.sell_price = sell_price;
    }

    public long getSellPrice() {
        return sell_price;
    }

    public String getSellPriceDesc() {
        return new DecimalFormat("0.##").format(((double) sell_price) / 100);
    }

    // public void setPeriod(int period) {
    // this.period = period;
    // }
    //
    // public int getPeriod() {
    // return period;
    // }

    public void setPeriodDesc(String periodDesc) {
        this.period_desc = periodDesc;
    }

    public String getPeriodDesc() {
        return period_desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof CardYear) {
            if (((CardYear) o).getGID() != -1l)
                return getGID() == ((CardYear) o).getGID();
            else
                return false;
        }
        return false;
    }

}
