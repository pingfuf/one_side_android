package com.oneside.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.R;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.utils.WebUtils;

import java.util.ArrayList;
import java.util.Date;

public class CardUser {

    private static final String KEY_PHONE_NUMBER = "phone";
    private static final String KEY_IS_NEW_USER = "is_new_user";
    private static final String KEY_REMAIN_DAYS = "remain_days";
    private static final String KEY_CARD_PERIODS = "card_periods";
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_NEW_BALANCE = "new_balance";
    private static final String KEY_LOGO_URL = "headimg";
    private static final String KEY_MERCHANT_CARD_IMG = "cardimg";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_GENDER = "sex";
    private static final String KEY_AGE_TYPE = "age";
    private static final String KEY_PRIVILEGES = "privileges";//???
    private static final String KEY_USER_ID = "uid";
    private static final String KEY_FAVOR_COUNT = "favorite_gyms_count";


    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_FOLLOWING_COUNT = "following_count";
    private static final String KEY_FANS_COUNT = "fans_count";
    private static final String KEY_BIO = "bio";
    private static final String KEY_IS_FOLLOWED = "is_followed";
    private static final String KEY_IS_ME = "is_me";
    private static final String KEY_MY_POST_COUNT = "my_post_count";
    private static final String KEY_BACK_IMG = "backimg";
    private static final String KEY_IS_ADMIN = "is_admin";
    private static final String KEY_HAS_ORDER = "has_order";

    //    public static final String EXPIRED_DATE = "expired_date";
    public static final String JSON_DATA = "card_user_json_data";

    private static final String KEY_YOUYANG_REMAIN_DAYS = "youyang_remain_days";
    private static final String KEY_YOUYANG_CURRENT_REMAIN_DAYS = "youyang_current_remain_days";
    private static final String KEY_YOUYANG_HAD_PERIOD = "youyang_had_period";


    private static final int MAX_ORDER_COUNT = 10;

    private String phoneNumber;
    private int remainDays = 0;
    private Date expiredDate, O2ExpiredDate;
    private ArrayList<BuyCardOrder> buyOrders;
    private double remainMoney = .0f;
    private boolean isNewUser = true;

    private String logoUrl;
    private String nickname;
    private int gender;//0 unknown; 1 man; 2 woman
    private int ageType;//-1: unknown; 0: 50s; 1: 60s; 2: 70s; 3: 80s; 4: 90s; 5: 00s
    private ArrayList<Integer> privileges;
    private int userID;
    private int favorCount;

    private int weight = 0;// 体重
    private int height = 0;// 身高
    private int followingCount = 0;// 关注数
    private int fansCount = 0; // 粉丝数
    private String bio; // 个人签名
    private boolean isFollowed = false;
    private boolean isMe = true;
    private int myPostCount = 0;
    private String backImg;
    private String cardImg;

    private boolean isAdmin = false;
    private boolean hasOrder = false;
    private int O2TotalRemainDays = 0;
    private int O2CurrRemailDays = 0;
    private boolean hasBuyedO2Space;

//  private ArrayList<AddressMessage> addressMessage;

    public CardUser(JSONObject json) {
        userID = WebUtils.getJsonInt(json, KEY_USER_ID, -1);
        phoneNumber = WebUtils.getJsonString(json, KEY_PHONE_NUMBER, "");
        isNewUser = WebUtils.getJsonBoolean(json, KEY_IS_NEW_USER, true);

        logoUrl = WebUtils.getJsonString(json, KEY_LOGO_URL, "");
        nickname = WebUtils.getJsonString(json, KEY_NICKNAME, "");
        gender = WebUtils.getJsonInt(json, KEY_GENDER, 0);
        O2TotalRemainDays = WebUtils.getJsonInt(json, KEY_YOUYANG_REMAIN_DAYS, 0);
        caculateO2Endtime();
        O2CurrRemailDays = WebUtils.getJsonInt(json, KEY_YOUYANG_CURRENT_REMAIN_DAYS, 0);
        hasBuyedO2Space = WebUtils.getJsonBoolean(json, KEY_YOUYANG_HAD_PERIOD);
        ageType = WebUtils.getJsonInt(json, KEY_AGE_TYPE, -1);
        favorCount = WebUtils.getJsonInt(json, KEY_FAVOR_COUNT, 0);
        hasOrder = WebUtils.getJsonBoolean(json, KEY_HAS_ORDER, false);
        JSONArray privilegesJson = WebUtils.getJsonArray(json, KEY_PRIVILEGES);
        privileges = new ArrayList<Integer>();
        if (privilegesJson != null && privilegesJson.size() > 0) {
            for (int i = 0; i < privilegesJson.size(); i++) {
                privileges.add(WebUtils.getJsonInt(privilegesJson, i));
            }
        } else {
            privileges.add(0);
        }

        remainDays = WebUtils.getJsonInt(json, KEY_REMAIN_DAYS, 0);
        caculateEndtime();
//        }
        LogUtils.d("caculateEndtime json = %s +++++++++++++++++ expiredDate = %s remainDays = %s",
                json, expiredDate, remainDays);
        JSONArray array = WebUtils.getJsonArray(json, KEY_CARD_PERIODS);
        if (array != null && array.size() > 0) {
            buyOrders = new ArrayList<BuyCardOrder>();
            int maxLenth = Math.min(MAX_ORDER_COUNT, array.size());
            for (int i = 0; i < maxLenth; i++) {
                JSONObject orderJson = WebUtils.getJsonObject(array, i);
                BuyCardOrder order = BuyCardOrder.getBuyOrderByJson(orderJson);
                if (order != null) {
                    buyOrders.add(order);
                }
            }
        }
        if (json.containsKey(KEY_NEW_BALANCE)) {
            LogUtils.d("#### KEY_NEW_BALANCE = %s", WebUtils.getJsonDouble(json, KEY_NEW_BALANCE, .0f));
            remainMoney = WebUtils.getJsonDouble(json, KEY_NEW_BALANCE, .0f) / 100;
        } else {
            remainMoney = WebUtils.getJsonDouble(json, KEY_BALANCE, .0f);
        }

        weight = WebUtils.getJsonInt(json, KEY_WEIGHT, 0);
        height = WebUtils.getJsonInt(json, KEY_HEIGHT, 0);
        followingCount = WebUtils.getJsonInt(json, KEY_FOLLOWING_COUNT, 0);
        fansCount = WebUtils.getJsonInt(json, KEY_FANS_COUNT, 0);
        bio = WebUtils.getJsonString(json, KEY_BIO, "");
        isFollowed = WebUtils.getJsonBoolean(json, KEY_IS_FOLLOWED, false);
        isMe = WebUtils.getJsonBoolean(json, KEY_IS_ME, true);
        myPostCount = WebUtils.getJsonInt(json, KEY_MY_POST_COUNT, 0);
        backImg = WebUtils.getJsonString(json, KEY_BACK_IMG, "");
        cardImg = WebUtils.getJsonString(json, KEY_MERCHANT_CARD_IMG, "");
        isAdmin = WebUtils.getJsonBoolean(json, KEY_IS_ADMIN, false);
    }

    public JSONObject toJson() {
        JSONObject result = new JSONObject();

        try {
            result.put(KEY_USER_ID, this.userID);
            result.put(KEY_PHONE_NUMBER, this.phoneNumber);
            result.put(KEY_IS_NEW_USER, this.isNewUser);
            result.put(KEY_REMAIN_DAYS, this.remainDays);
            result.put(KEY_BALANCE, this.remainMoney);
            result.put(KEY_NEW_BALANCE, this.remainMoney * 100);
            result.put(KEY_HAS_ORDER, hasOrder);
            if (LangUtils.isNotEmpty(this.logoUrl))
                result.put(KEY_LOGO_URL, this.logoUrl);
            result.put(KEY_NICKNAME, this.nickname);
            result.put(KEY_GENDER, this.gender);
            result.put(KEY_AGE_TYPE, this.ageType);
            result.put(KEY_FAVOR_COUNT, this.favorCount);

            if (LangUtils.isNotEmpty(this.privileges)) {
                JSONArray privilegesJson = new JSONArray();
                for (int privilege : this.privileges) {
                    privilegesJson.add(privilege);
                }
                result.put(KEY_PRIVILEGES, privilegesJson);
            }

            if (LangUtils.isNotEmpty(this.buyOrders)) {
                JSONArray buyOrdersArray = new JSONArray();
                for (BuyCardOrder bco : this.buyOrders) {
                    buyOrdersArray.add(bco.jsonDict());
                }
                result.put(KEY_CARD_PERIODS, buyOrdersArray);
            }

//            result.put(EXPIRED_DATE, expiredDate == null ? 0l : expiredDate.getTime());

            result.put(KEY_WEIGHT, this.weight);
            result.put(KEY_HEIGHT, this.height);
            result.put(KEY_FOLLOWING_COUNT, this.followingCount);
            result.put(KEY_FANS_COUNT, this.fansCount);
            result.put(KEY_IS_FOLLOWED, this.isFollowed);
            result.put(KEY_BIO, this.bio);
            result.put(KEY_IS_ME, this.isMe);
            result.put(KEY_MY_POST_COUNT, this.myPostCount);
            result.put(KEY_BACK_IMG, this.backImg);
            result.put(KEY_MERCHANT_CARD_IMG, this.cardImg);
            result.put(KEY_IS_ADMIN, this.isAdmin);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public void saveJsonData() {
        IOUtils.savePreferenceValue(JSON_DATA, toJson().toString());
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public static CardUser getCardUserByJson(JSONObject json) {
        if (json == null || json.size() <= 0) {
            return null;
        }
        return new CardUser(json);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRamainDays() {
        return remainDays;
    }

    public void setRamainDays(int ramainDays) {
        this.remainDays = ramainDays;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public Date getO2ExpiredDate() {
        return O2ExpiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public ArrayList<BuyCardOrder> getBuyOrders() {
        return buyOrders;
    }

    public void setBuyOrders(ArrayList<BuyCardOrder> buyOrders) {
        this.buyOrders = buyOrders;
    }

    private void caculateEndtime() {
        Date now = new Date();
        if (remainDays > 0) {
            expiredDate = LangUtils.dateByAddingTimeDay(now, remainDays - 1);
            expiredDate = LangUtils.cc_dateByMovingToEndOfDay(expiredDate);
        } else {
            expiredDate = null;
        }

    }

    private void caculateO2Endtime() {
        Date now = new Date();
        if (O2TotalRemainDays > 0) {
            O2ExpiredDate = LangUtils.dateByAddingTimeDay(now, O2TotalRemainDays - 1);
            O2ExpiredDate = LangUtils.cc_dateByMovingToEndOfDay(O2ExpiredDate);
        } else {
            O2ExpiredDate = null;
        }
    }

    public double getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(double remainMoney) {
        this.remainMoney = remainMoney;
    }

    public boolean isNewUser() {
//    boolean res = true;
//      if(!LangUtils.isEmpty(buyOrders))
//        res = false;
//    return res;
        return this.isNewUser;
    }

    public void setNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public boolean hasOrder() {
        return hasOrder;
    }

    public static String getGenderDesc(int gender) {
        String strGender = ViewUtils.getString(R.string.usr_info_unknown);
        switch (gender) {
            case 0:
                strGender = /*ViewUtils.getString(R.string.usr_info_unknown)*/"";
                break;
            case 1:
                strGender = ViewUtils.getString(R.string.usr_info_man);
                break;
            case 2:
                strGender = ViewUtils.getString(R.string.usr_info_woman);
                break;
            default:
                strGender = /*ViewUtils.getString(R.string.usr_info_unknown)*/"";
                break;
        }
        return strGender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAgeType() {
        return ageType;
    }

    public int getO2SpaceRemainDays() {
        return O2TotalRemainDays;

    }

    public int getO2CurrRemainDays() {
        return O2CurrRemailDays;
    }

    public boolean isHasBuyedO2Space() {
        return hasBuyedO2Space;
    }

    public int getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(int remainDays) {
        this.remainDays = remainDays;
    }

    public void setIsNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setIsMe(boolean isMe) {
        this.isMe = isMe;
    }

    public static final String[] AGE_CONTENT = ViewUtils.getStringArray(R.array.user_info_age_types);

    public static String getAgeTypeDesc(int age) {
        if (age >= 0 && age < AGE_CONTENT.length)
            return AGE_CONTENT[age];
        else
            return "";
    }

    public static String getHeightTypeDesc(int height) {
        //TODO need to confirm with web
        if (height > 0 && height < HEIGHT_CONTENT.length)
            return HEIGHT_CONTENT[height];
        else
            return "";
    }

    public static String getWeightTypeDesc(int weight) {
        if (weight > 0 && weight < WEIGHT_CONTENT.length)
            return WEIGHT_CONTENT[weight];
        else
            return "";
    }

    public static final String[] HEIGHT_CONTENT = getHeightContent();

    private static String[] getHeightContent() {
        String[] array = new String[121];
        String unit = ViewUtils.getString(R.string.user_height_tip_unit);
        for (int i = 100; i < 221; i++) {
            array[i - 100] = i + unit;
        }
        return array;
    }

    public static final String[] WEIGHT_CONTENT = getWeightContent();

    private static String[] getWeightContent() {
        String[] array = new String[131];
        String unit = ViewUtils.getString(R.string.user_weight_tip_unit);
        for (int i = 20; i < 151; i++) {
            array[i - 20] = i + unit;
        }
        return array;
    }

    public void setAgeType(int ageType) {
        this.ageType = ageType;
    }

    public ArrayList<Integer> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(ArrayList<Integer> privileges) {
        this.privileges = privileges;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFavorCount() {
        return favorCount;
    }

    public void setFavorCount(int favorCount) {
        this.favorCount = favorCount;
    }

    public int getMyPostCount() {
        return myPostCount;
    }

    public void setMyPostCount(int myPostCount) {
        this.myPostCount = myPostCount;
    }

    public String getBackImg() {
        return backImg;
    }

    public void setBackImg(String backImg) {
        this.backImg = backImg;
    }

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
