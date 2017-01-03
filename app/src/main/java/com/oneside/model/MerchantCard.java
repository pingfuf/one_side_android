package com.oneside.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.model.response.UnactivedCard;
import com.oneside.utils.LangUtils;
import com.oneside.utils.WebUtils;

import java.util.ArrayList;
import java.util.Date;

public class MerchantCard {


    public static final String KEY_CARD_ID = "id";
    public static final String KEY_MERCHANT_ID = "gym_id";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DAYS_LEFT = "days_left";
    public static final String KEY_TITLE = "title";
    public static final String KEY_FEATURES = "features";//[1(在线人数), 2(扫码开门), 3(锁柜解锁)]
    private static final int FEATURE_ON_LINE = 1;
    private static final int FEATURE_OPEN_DOOR = 2;
    private static final int FEATURE_OPEN_BOX = 3;
    private static final int FEATURE_SHOWER = 4;
    public static final String KEY_ONLINE_NUM = "online_num";
    //该字段被弃用 for v3.3
    public static final String KEY_SUPPORT_BUY = "vip_service";
    //用户是否是该场馆的ERP会员
    public static final String KEY_IS_TEDDY_VIP = "is_erp_vip";
    //判断是否支持ERP场馆
    public static final String KEY_IS_ERP = "is_erp";
    public static final String KEY_XXPASS_SUPPORTED = "xxpass_supported";
    public static final String KEY_HAS_ORDER_HISTORY = "has_order_history";
    /*v3.5 added*/
    private static final String KEY_CIRCLE_GYM_COACHES = "gym_coaches";
    private static final String KEY_UNACTIVED_CARDS = "unactived_cards";
//    private static final String KEY_SHOWER_FREE_MINUTES = "shower_free_minutes";
//    private static final String KEY_SHOWER_UNIT_PRICE = "shower_unit_price";

    private int cardID = -1;
    private long merchantID = -1l;
    private String location;
    private int daysLeft;
    private String title;
    /**
     * 1  在线人数  2  开门进店  3  锁柜解锁  4  淋浴
     */
    private int[] features;

    //    private MCS[] mcs;
    private int onLineNum;
    private boolean isSupportBuy;
    //判断是否是ERP场馆会员
    private boolean isTeddyMerchant;
    //判断是否该卡是小熊通卡
    private boolean isXXMerchantCard = false;
    //判断是否是该卡是有氧空间卡
    private boolean isSpecialMerchantCard = false;
    //判断是否支持通卡购买
    private boolean isSupportXX = false;
    private boolean isErp = false;
    private boolean hasOrderHistory = false;
    private boolean isSurpportShower = false;
    private int freeShowerMinute = 0;
    private int unitShowerMinute = 0;

    //非场馆字段，为了展示未读消息数
    private int unreadNum;
    //展示场馆教练
    private ArrayList<PersonalCoach> coaches;
    private ArrayList<UnactivedCard> unactivedCards;

    public static MerchantCard fromJson(JSONObject json) {
        int cardID = WebUtils.getJsonInt(json, KEY_CARD_ID, 0);
        long merchantID = WebUtils.getJsonLong(json, KEY_MERCHANT_ID, -1l);
        String location = WebUtils.getJsonString(json, KEY_LOCATION);
        int daysLeft = WebUtils.getJsonInt(json, KEY_DAYS_LEFT, -1);
        String title = WebUtils.getJsonString(json, KEY_TITLE, "");
        int onLineNum = WebUtils.getJsonInt(json, KEY_ONLINE_NUM, -1);
        boolean isSupportBuy = WebUtils.getJsonBoolean(json, KEY_SUPPORT_BUY, false);
        boolean isTeddyMerchant = WebUtils.getJsonBoolean(json, KEY_IS_TEDDY_VIP, false);
        boolean isSupportXX = WebUtils.getJsonBoolean(json, KEY_XXPASS_SUPPORTED, false);
        boolean isErp = WebUtils.getJsonBoolean(json, KEY_IS_ERP, false);
        boolean hasOrderHistory = WebUtils.getJsonBoolean(json, KEY_HAS_ORDER_HISTORY, false);
//        int freeMinute = WebUtils.getJsonInt(json, KEY_SHOWER_FREE_MINUTES, 0);
//        int unitMinute = WebUtils.getJsonInt(json, KEY_SHOWER_UNIT_PRICE, 0);
//        boolean shower = WebUtils.getJsonBoolean(json, KEY_SHOWER_SUPPORTED, false);
        MerchantCard mc = new MerchantCard();
        mc.setCardID(cardID);
        mc.setMerchantID(merchantID);
        mc.setLocation(location);
        mc.setDaysLeft(daysLeft);
        mc.setTitle(title);
        mc.setTeddyGYM(isTeddyMerchant);
        mc.setOnLineNum(onLineNum);
        mc.setIsSupportBuy(isSupportBuy);
        mc.setSupportXX(isSupportXX);
        mc.setErp(isErp);
        mc.setOrderHistory(hasOrderHistory);
//        mc.setUnitShowerMinute(unitMinute);
//        mc.setFreeShowerMinute(freeMinute);

        JSONArray unactivedCards = WebUtils.getJsonArray(json, KEY_UNACTIVED_CARDS);
        if (unactivedCards != null && unactivedCards.size() > 0) {
            ArrayList<UnactivedCard> unactivedCard = new ArrayList<>();
            for (int i = 0; i < unactivedCards.size(); i++) {
                JSONObject jsonObject = WebUtils.getJsonObject(unactivedCards, i);
                if (jsonObject == null)
                    continue;
                UnactivedCard card = UnactivedCard.fromJson(jsonObject);
                unactivedCard.add(card);
            }
            mc.setUnactivedCards(unactivedCard);
        }

        JSONArray featuresArray = WebUtils.getJsonArray(json, KEY_FEATURES);
        if (featuresArray != null && featuresArray.size() > 0) {
            int[] features = new int[featuresArray.size()];
            for (int i = 0; i < featuresArray.size(); i++) {
                int f = WebUtils.getJsonInt(featuresArray, i);
                features[i] = f;
            }
            mc.setFeatures(features);
        }


        JSONArray coachesArray = WebUtils.getJsonArray(json, KEY_CIRCLE_GYM_COACHES);
        if (LangUtils.isNotEmpty(coachesArray)) {
            ArrayList<PersonalCoach> coaches = new ArrayList<>();
            for (int i = 0; i < coachesArray.size(); i++) {
                JSONObject coachObject = WebUtils.getJsonObject(coachesArray, i);
                if (coachObject != null)
                    coaches.add(PersonalCoach.fromJson(coachObject));
            }

            mc.setCoaches(coaches);
        }
        return mc;
    }


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_CARD_ID, cardID);
            json.put(KEY_MERCHANT_ID, merchantID);
            json.put(KEY_LOCATION, location);
            json.put(KEY_DAYS_LEFT, daysLeft);
            json.put(KEY_TITLE, title);
            json.put(KEY_ONLINE_NUM, onLineNum);
            json.put(KEY_SUPPORT_BUY, isSupportBuy);
            json.put(KEY_IS_TEDDY_VIP, isTeddyMerchant);
            json.put(KEY_XXPASS_SUPPORTED, isSupportXX);
            json.put(KEY_IS_ERP, isErp);
            json.put(KEY_HAS_ORDER_HISTORY, hasOrderHistory);
//            json.put(KEY_SHOWER_FREE_MINUTES, freeShowerMinute);
//            json.put(KEY_SHOWER_UNIT_PRICE, unitShowerMinute);
//            json.put(KEY_SHOWER_SUPPORTED, isSurpportShower);
            if (features != null && features.length > 0) {
                JSONArray featuresArray = new JSONArray();
                for (int f : features) {
                    featuresArray.add(f);
                }
                json.put(KEY_FEATURES, featuresArray);
            }

            if (unactivedCards != null && unactivedCards.size() > 0) {
                JSONArray cardsArray = new JSONArray();
                for (UnactivedCard f : unactivedCards) {
                    cardsArray.add(f);
                }
                json.put(KEY_UNACTIVED_CARDS, cardsArray);
            }

            if (LangUtils.isNotEmpty(coaches)) {
                JSONArray coachesArray = new JSONArray();
                for (PersonalCoach ci : coaches) {
                    coachesArray.add(ci.toJson());
                }
                json.put(KEY_CIRCLE_GYM_COACHES, coachesArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }

    public int getUnreadNum() {
        return unreadNum;
    }

    public void setSpecialMerchantCard(boolean isSpecialMerchantCard) {
        this.isSpecialMerchantCard = isSpecialMerchantCard;
    }

    public boolean isSpecialMerchantCard() {
        return isSpecialMerchantCard;
    }

    public void setXXMerchantCard(boolean isXXMerchantCard) {
        this.isXXMerchantCard = isXXMerchantCard;
    }

    public void setSupportXX(boolean isSupportXX) {
        this.isSupportXX = isSupportXX;
    }

    public boolean isSupportXX() {
        return isSupportXX;
    }

    public boolean isXXMerchantCard() {
        return isXXMerchantCard;
    }

    private void setTeddyGYM(boolean isTeddyMerchant) {
        this.isTeddyMerchant = isTeddyMerchant;
    }

    public boolean isErpMerchant() {
        return isErp;
    }

    public void setErp(boolean isErp) {
        this.isErp = isErp;
    }

    public boolean isTeddyMerchant() {
        return isTeddyMerchant;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public long getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(long merchantID) {
        this.merchantID = merchantID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public String getExpiredTimeStr() {
        if (daysLeft == 0)
            return null;
        else
            return LangUtils.formatAlldayTime(LangUtils.dateByAddingTimeDay(new Date(), daysLeft));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getFeatures() {
        return features;
    }

    public void setFeatures(int[] features) {
        this.features = features;
    }

//    public void setMcs(MCS[] mcs) {
//        this.mcs = mcs;
//    }
//
//    public MCS[] getMcs(){
//        return mcs;
//    }

    public void setCoaches(ArrayList<PersonalCoach> coaches) {
        this.coaches = coaches;
    }

    public ArrayList<PersonalCoach> getCoaches() {
        return coaches;
    }

    public int getOnLineNum() {
        return onLineNum;
    }

    public void setOnLineNum(int onLineNum) {
        this.onLineNum = onLineNum;
    }

    public boolean isSupportOnlineNum() {
        if (features != null && features.length > 0) {
            for (int f : features) {
                if (f == FEATURE_ON_LINE)
                    return true;
            }
        }
        return false;
    }

    public boolean isSupportOpenDoor() {
        if (features != null && features.length > 0) {
            for (int f : features) {
                if (f == FEATURE_OPEN_DOOR)
                    return true;
            }
        }
        return false;
    }

    private void setFreeShowerMinute(int freeShowerMinute) {
        this.freeShowerMinute = freeShowerMinute;
    }

    public int getFreeShowerMinute() {
        return freeShowerMinute;
    }

    public int getUnitShowerMinute() {
        return unitShowerMinute;
    }

    private void setUnitShowerMinute(int unitShowerMinute) {
        this.unitShowerMinute = unitShowerMinute;
    }

    private void setOrderHistory(boolean hasOrderHistory) {
        this.hasOrderHistory = hasOrderHistory;
    }

    public boolean isHasOrderHistory() {
        return hasOrderHistory;
    }

    public boolean isSupportOpenBox() {
        if (features != null && features.length > 0) {
            for (int f : features) {
                if (f == FEATURE_OPEN_BOX)
                    return true;
            }
        }
        return false;
    }

    public boolean isSupportBuy() {
        return isSupportBuy;
    }

    public void setIsSupportBuy(boolean isSupportBuy) {
        this.isSupportBuy = isSupportBuy;
    }

    public boolean isSupportShower() {
        if (features != null && features.length > 0) {
            for (int f : features) {
                if (f == FEATURE_SHOWER) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setUnactivedCards(ArrayList<UnactivedCard> unactivedCards) {
        this.unactivedCards = unactivedCards;
    }

    public ArrayList<UnactivedCard> getUnactivedCards() {
        return this.unactivedCards;
    }
}
