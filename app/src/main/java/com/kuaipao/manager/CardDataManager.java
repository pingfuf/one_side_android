package com.kuaipao.manager;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.model.event.FollowUserEvent;
import com.kuaipao.model.event.MerchantAllCourses;
import com.kuaipao.model.event.WebBaseEvent;
import com.kuaipao.model.AddressMessage;
import com.kuaipao.model.AdvertisementMessage;
import com.kuaipao.model.CardMessage;
import com.kuaipao.model.CardUser;
import com.kuaipao.model.LocationCoordinate2D;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.SysUtils;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.base.net.RequestDelegate;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.kuaipao.utils.IOUtils.getPreferenceValue;
import static com.kuaipao.utils.IOUtils.savePreferenceValue;
import static com.kuaipao.utils.LangUtils.format;
import static com.kuaipao.utils.LangUtils.formatAlldayTime;
import static com.kuaipao.utils.LangUtils.isEmpty;
import static com.kuaipao.utils.LangUtils.isNotEmpty;
import static com.kuaipao.utils.LangUtils.parseLong;
import static com.kuaipao.utils.LogUtils.d;
import static com.kuaipao.utils.LogUtils.w;
import static com.kuaipao.utils.WebUtils.getJsonArray;
import static com.kuaipao.utils.WebUtils.getJsonInt;
import static com.kuaipao.utils.WebUtils.getJsonObject;
import static com.kuaipao.utils.WebUtils.getJsonString;
import static com.kuaipao.utils.WebUtils.parseJsonArray;

/**
 * @author Guo Ming
 */
public class CardDataManager {

    public interface DataResultListener {
        void onFinish(boolean ret, Object... params);
    }

    /**
     * update new interface at v3.0
     */
    private static final String MERCHANT_DETAILS_URL_FROM_30 = "client/gym/%s";
    private static final String FETCH_FIT_DATA_URL = "client/fitness/today";
    private static final String FETCH_LAST_DIALOG_RECORD = "client/fitness/last-record";

    private static final String FOLLOW_STATE_UPLOAD_URL = "/xxquan/follow";

    /**
     * 关注场馆
     */
    private static final String USER_INFO_ME_URL = "client/me";
    public static final String USER_INFO_UPDATE_URL = "client/update_user_info";

    private static final String MSG_URL = "client/messages";

    private static final String COURSES_OF_MERCHANT = "client/gym/%s/courses";
    private static final String PERSONAL_COURSES_IN_GYM_URL = "client/gym-coach-single-courses";

    private static final String SCAN_QR_CODE_CHECK_IN = "client/user/orders/affirm";
    private static final String SUPPORT_CITY_URL = "client/city_list";
    private static final String SUPPORT_CITY_LAST_DATA = "support_city_last_data";
    private static final String SUPPORT_CITY_LAST_SAVE_DATE = "support_city_last_save_date";

    private static final String MERCHANT_MAP_DATA_URL = "gym/get_gyms_in_map_rectangle";


    private static final String USER_INFO_BY_ID_URL = "client/user_info/%s";

    /**
     * @param usrLocation 用户位置, 非必填
     * @param leftTop
     * @param rightBottom
     */
    public static void fetchMerchantsInMapData(LocationCoordinate2D usrLocation, String strCity,
                                               LocationCoordinate2D leftTop, LocationCoordinate2D rightBottom, final DataResultListener l) {
        if (!CardSessionManager.getInstance().isOnLine()) {
            d("fetchMerchantsInMapData offline mode");
            fireDataResultListener(l, false);
            return;
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        if (usrLocation != null) {
            params.put("usr_lng", usrLocation.getLongitude());
            params.put("usr_lat", usrLocation.getLatitude());
        }

        if (isNotEmpty(strCity))
            params.put("city", strCity);

        if (leftTop != null) {
            params.put("lng0", leftTop.getLongitude());
            params.put("lat0", leftTop.getLatitude());
        }

        if (rightBottom != null) {
            params.put("lng1", rightBottom.getLongitude());
            params.put("lat1", rightBottom.getLatitude());
        }

        params.put("coordsys", "gaode");
        params.put("map_version", "gaode");

        UrlRequest r = new UrlRequest(MERCHANT_MAP_DATA_URL, params);
        LogUtils.d(">>>> merchants_in_map url=%s", r.getUrl());
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {
//                request.setModelClass(MapOfMerchantsActivity.MerchantMapModel.class);
//                List<MerchantMapModel> data = request.getModelList();

            }

            public void requestFailed(UrlRequest request, int statusCode, String errorString) {
                fireDataResultListener(l, false);
            }
        });
        r.start();
    }

    public static void fetchPersonalCourseOfMerchant(long merchantId, Date date, final DataResultListener l) {
        if (merchantId <= 0 || date == null) {
            fireDataResultListener(l, false);
            return;
        }

        if (!CardSessionManager.getInstance().isOnLine()) {
            fireDataResultListener(l, false);
            return;
        }

        String dateTime = formatAlldayTime(date);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("gym_id", merchantId);
        params.put("date", dateTime);
        UrlRequest r = new UrlRequest(PERSONAL_COURSES_IN_GYM_URL, params);

        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {

            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                fireDataResultListener(l, false);
            }
        });
        r.start();
    }


    public static void fetchCoursesOfMerchant(long merchantId, Date date) {
        if (merchantId <= 0 || date == null) {
            return;
        }

        if (!CardSessionManager.getInstance().isOnLine()) {
            return;
        }

        String dateTime = formatAlldayTime(date);
        HashMap<String, Object> params = new HashMap<>();
        params.put("date", dateTime);
        UrlRequest r = new UrlRequest(format(COURSES_OF_MERCHANT, merchantId), params);
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String message) {
                EventBus.getDefault().post(new MerchantAllCourses(false));
            }

            @Override
            public void requestFinished(UrlRequest request) {

            }
        });
        r.start();
    }

    /************* Message ****************/
    /**
     * Get message list from server
     *
     * @param l
     * @update true ： fetch from server
     */
    public static void fetchMessageList(final DataResultListener l, int page/* , boolean udpate */) {
        if (!CardSessionManager.getInstance().isLogin())
            return;

        HashMap<String, Object> params = new HashMap<String, Object>();
        // if (perpage > 0 && perpage != 10)
        params.put("perpage", 10);
        params.put("page", page);
        UrlRequest r = new UrlRequest(MSG_URL, params);
        d(">>>> fetchMessageList url = %s", r.getUrl());
        // HashMap<String, Object> params = new HashMap<String, Object>();
        // params.put("pages", "1");
        r.setDelegate(new RequestDelegate() {
            int pagers = 0;
            List<CardMessage> list = new ArrayList<CardMessage>();

            @Override
            public void requestFinished(UrlRequest request) {

            }

            @Override
            public void requestFailed(UrlRequest request, int statusCode, String message) {
                fireDataResultListener(l, false, list, pagers);
            }
        });
        r.start();
    }

    /*************************************/
    private static final String GET_USER_ADDRESS_URL = "client/get_freq_addr";
    private static ArrayList<AddressMessage> address;

    private static void fetchUserAddress() {
        // TODO
        if (address == null)
            address = new ArrayList<AddressMessage>();
        UrlRequest r = new UrlRequest(GET_USER_ADDRESS_URL);
        r.setDelegate(new RequestDelegate() {

            @Override
            public void requestFailed(UrlRequest request, int statusCode, String message) {

            }

            @Override
            public void requestFinished(final UrlRequest request) {

            }

        });
        r.start();
    }

    /**********************
     * upload follow
     **********************/
    public static void uploadFollowState(final int userID, final DataResultListener l) {
        UrlRequest r = new UrlRequest(FOLLOW_STATE_UPLOAD_URL);
        r.addPostParam("uid", userID);
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String message) {
                fireDataResultListener(l, false);
            }

            @Override
            public void requestFinished(UrlRequest request) {
                CardUser cu = CardManager.getCardUser();
                if (cu != null) {
                    cu.setFollowingCount(cu.getFollowingCount() + 1 >= 1 ? cu.getFollowingCount() + 1 : 1);
                    cu.saveJsonData();
                    CardSessionManager.getInstance().setUserCareGymCountChanged(true);


                    FollowUserEvent event = new FollowUserEvent(true);
                    event.setFollowState(true);
                    event.setUserID(userID);
                    EventBus.getDefault().post(event);
                }
                fireDataResultListener(l, true);

            }
        });
        r.start();
    }

    /****************
     * fit timing  start
     **************/
    public static void fetchTodayFitData(final DataResultListener l) {
        if (!CardSessionManager.getInstance().isLogin()) {
            return;
        }
        UrlRequest r = new UrlRequest(FETCH_FIT_DATA_URL);
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String message) {
                fireDataResultListener(l, false);
            }

            @Override
            public void requestFinished(UrlRequest request) {

            }
        });
        r.startImmediate();
    }


    public static void fetchLastEndRecord(final DataResultListener l) {
        UrlRequest r = new UrlRequest(FETCH_LAST_DIALOG_RECORD);
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String message) {
                fireDataResultListener(l, false);
            }

            @Override
            public void requestFinished(UrlRequest request) {

            }
        });
        r.startImmediate();
    }

    /**************
     * User Info
     *******************/
    public static void fetchUserInfo(final DataResultListener l) {
        UrlRequest r = new UrlRequest(USER_INFO_ME_URL);
        LogUtils.d("#### fetchUserInfo url=%s", r.getUrl());
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {

            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                fireDataResultListener(l, false);
            }
        });
        r.startImmediate();
        fetchUserAddress();
    }


    public static void fetchUserInfoByID(int uid, final DataResultListener l) {
        if (uid < 0) {
            w("fetchUserInfoByID invalid key, uid is Empty");
            fireDataResultListener(l, false);
            return;
        }

        if (!CardSessionManager.getInstance().isOnLine()) {
            d("fetchUserInfoByID offline mode, so stop request to server");
            fireDataResultListener(l, false);
            return;
        }


        UrlRequest r = new UrlRequest(format(USER_INFO_BY_ID_URL, uid));
        LogUtils.d("2424 fetchUserInfoByID url:%s", r.getUrl());
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String message) {
                if (message == null) {
                    fireDataResultListener(l, false, "not_exist");
                } else {
                    fireDataResultListener(l, false);
                }
            }

            @Override
            public void requestFinished(UrlRequest request) {

            }
        });
        r.start();
    }

    public static void checkinScanCode(String orderCode, String scanQRCode, final DataResultListener l) {
        UrlRequest r = new UrlRequest(SCAN_QR_CODE_CHECK_IN);
        r.addPostParam("order_code", orderCode);
        r.addPostParam("qrcode", scanQRCode);


        r.addPostParam("source", "android");
        if (LangUtils.isNotEmpty(SysUtils.mUniquePsuedoID))
            r.addPostParam("device_id", SysUtils.mUniquePsuedoID);

        if (CardLocationManager.getInstance().getLocation() != null) {
            r.addPostParam("lat", CardLocationManager.getInstance().getLocation().getLatitude());
            r.addPostParam("lon", CardLocationManager.getInstance().getLocation().getLongitude());
        }

        LogUtils.d(">>>> url=%s", r.getUrl());
        r.setDelegate(new RequestDelegate() {
            public void requestFinished(UrlRequest request) {

            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                fireDataResultListener(l, false);
            }
        });
        r.start();
    }

    public static void fetchSupportCityList(final DataResultListener l) {
        String strLastSaveDate = getPreferenceValue(SUPPORT_CITY_LAST_SAVE_DATE);
        if (isNotEmpty(strLastSaveDate)) {
            long lastSaveDate = parseLong(strLastSaveDate, 0l);

            if (3600 * 1000 * 24l > System.currentTimeMillis() - lastSaveDate) { // saved one day
                String strSupportCityListdata = getPreferenceValue(SUPPORT_CITY_LAST_DATA);
                if (!isEmpty(strSupportCityListdata)) {
                    JSONArray supportCityListData = parseJsonArray(strSupportCityListdata);
                    if (supportCityListData != null && supportCityListData.size() > 0) {
                        LogUtils.d(">>>>>> supportCityListData=%s", supportCityListData);
                        parseSupportCityList(supportCityListData, l);
                        return;
                    }
                }
            }
        }

        if (!CardSessionManager.getInstance().isOnLine()) {
            d("fetchSupportCityList offline mode, so stop request to server");
            fireDataResultListener(l, false);
            return;
        }


        UrlRequest r = new UrlRequest(SUPPORT_CITY_URL);
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {
            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                fireDataResultListener(l, false);
            }
        });
        r.start();
    }

    /***********************
     * Private Methods
     ****************************/
    private static void fireDataResultListener(DataResultListener l, boolean ret, Object... data) {
        if (l == null) {
            return;
        }
        // if (ret) {
        l.onFinish(ret, data);
        // } else {
        // l.onFinish(ret);
        // }
    }

    private static void parseMsgList(List<CardMessage> list, JSONArray msgArray/*, boolean needSave*/) {
        if (list == null) {
            return;
        }
        list.clear();
        if (msgArray != null) {
            // if (!needSave) {
            ArrayList<CardMessage> tmpList = new ArrayList<CardMessage>();
            for (int i = 0; i < msgArray.size(); i++) {
                JSONObject msgJson = getJsonObject(msgArray, i);
                CardMessage msg = CardMessage.getMessageByJson(msgJson);
                // TODO
                if (msg != null && !tmpList.contains(msg) && !list.contains(msg)) {
                    tmpList.add(msg);
                }
            }
            list.addAll(0, tmpList);
        }
    }

    private static void parseSupportCityList(JSONArray data, DataResultListener l) {
        List<String> supportCityList = new ArrayList<String>(data.size());

        for (int i = 0; i < data.size(); i++) {
            JSONObject cityObject = getJsonObject(data, i);
            String cityName = getJsonString(cityObject, "name");
            if (!LangUtils.isEmpty(cityName))
                supportCityList.add(cityName);
        }

        CardLocationManager.getInstance().setSupportCityList(supportCityList);
        fireDataResultListener(l, true, supportCityList);
    }

    public static void save(ArrayList<AdvertisementMessage> adLists) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray ja = new JSONArray();
            for (int f = 0; f < adLists.size(); f++) {
                AdvertisementMessage ad = adLists.get(f);
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("end", ad.getAdEnd());
                jsonItem.put("start", ad.getAdStart());
                jsonItem.put("logo_url", ad.getBitmapKey());
                jsonItem.put("url", ad.getAdUrl());
                jsonItem.put("title", ad.getAdTitle());
                jsonItem.put("is_new", ad.getNew());
                jsonItem.put("id", ad.getAdID());
                jsonItem.put("priority", ad.getAdPrio());
                ja.add(jsonItem);
            }
            jsonObject.put("data", ja);
            savePreferenceValue(Constant.AD_SAVE_DATA, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void uploadUserInfo(int ageType, int sexType, String nickname,
                                      String bio, int height, int weight, RequestDelegate rd) {
        UrlRequest r = new UrlRequest(CardDataManager.USER_INFO_UPDATE_URL);

        if (ageType >= 0)
            r.addPostParam("age", LangUtils.parseString(ageType));
        if (sexType >= 0)
            r.addPostParam("sex", LangUtils.parseString(sexType));

        if (LangUtils.isNotEmpty(nickname))
            r.addPostParam("nickname", nickname);

        if (LangUtils.isNotEmpty(bio))
            r.addPostParam("bio", bio);

        if (weight > 0)
            r.addPostParam("weight", weight);

        if (height > 0)
            r.addPostParam("height", height);

        LogUtils.d("2424 uploadUserInfo url=%s", r.getUrl());
        r.setDelegate(rd);
        r.startImmediate();
    }


    public static long getMerchantTopicsLastReadMsgID(long merchantID) {
        String lastID = IOUtils.getPreferenceValue(Constant.PREFERENCE_KEY_MERCHANT_TOPIC_LAST_READ_MSG_ID + String.valueOf(merchantID));
        return LangUtils.parseLong(LangUtils.isEmpty(lastID) ? "0" : lastID, 0l);
    }
}
