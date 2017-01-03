package com.oneside.utils;

public class Constant {
    /**
     * Alipay
     */
    // 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
    public static final String PARTNER = "2088801972852877";
    // 商户收款的支付宝账号
    public static final String SELLER = "contact@papayamobile.com";
    // 商户（RSA）私钥(注意一定要转PKCS8格式，否则在Android4.0及以上系统会支付失败)
    public static final String RSA_PRIVATE =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANxZdglPFUF4vNLRJ+GtNjLyeWZY3/V2f/VyMdxMS6W76m4SnAQ0Mfk4NdsG2zNEGXMX04xG16wn5mlNoFUaPnuiyku3MOdyU9GPENVhpOFsfOWA0nBS8fzuRfM1pzHId87ugTuvU/kH0ee5XNEdwixvpgyp/957Cuv1X9QPcC/lAgMBAAECgYA6j0fwV2UVvkmoWU+ZCVhzF7+ttIbojDKbf1rC6j/cbuFxmV5/O4PgcNDXQa41pK6CyN0+1YDxzrTMXYXzuALMiIv4SSXmiaRr1m3G4Z88wwLuuHKFL62MxhzygEyXd/ydCVZFCwk1I0nBkDc8kR1w8xjBZ930gsHJk0Bapc3NTQJBAO6XcxPaNjBzHnUHqBVKiVm2ySoPGVwE5xihhAXubfHhFnFj9EOMrogLookThcW7fKDMXseRSSSTz4uI2YGgbIMCQQDsbUY9zNeCTQiak/fg+u6M+toh3EI47N90slCvEPkJmvQTiDfWGihJ9zP6MxahcaUsWbVmn35iHduufxljfRV3AkEAjtZmk5UI6hqROlj6HL0B247dgeuGMBvTSmCvzGlAsxUhPYMsoiAgANyOUug4JvemlhGkEG//TQGcuBmWtc8YBwJBALZg+S1VJe2q9PchK7cOexSfscMrAJ6fAyUnJJxXkHR7ZsrmaoQre2bLXfokjNGPNCZJMWjvofOYKs2p1/DqHbMCQCu8Jthjmdad4wu1vTD/u8LhRk29Xo07mSVPt+YkM3pVd+GnlsooEDyBNCw+QR0Vxp05H+FTwavHn6GS0cyZIl0=";
    /**
     * Wechat
     */
    public static final String APP_ID = /* "wx445365abee4c4c01" */"wx98da3e01b7417486";// yum

    public static final String WE_APP_ID = /* "wx6869a6b6b83a54af" */"wx98da3e01b7417486";// shi
    public static final String QQ_APP_ID = "1104632019";// shi

    public static final String RMB = "￥";

    public static final String DEFAULT_BIRTHDAY = "1990-01-01";

    /**
     * 默认每次请求结果为10条
     */
    public static final int PAGE_SIZE = 10;

    public static final String APP_EN_NAME = "xxassistant";

    /**
     * 能在垃圾一点吗？为什么URL都要加上这个东西
     */
    public static final String HTTP = "https:";

    public static final String WEIBO_SHARE_MSG_KEY = "weibo_share_msg_key";// shi
    public static final String WEIBO_SHARE_BMP_KEY = "weibo_share_bmp_key";// shi
    public static final String WEIBO_SHARE_URL_KEY = "weibo_share_url_key";// shi
    public static final String WEIBO_APP_ID = "788858671";// shi
    public static final String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";// 应用的回调页
    public static final String SCOPE = // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String GET_INVITE_CODE_URL = "/client/invite";
    /**
     * need to confirm where you can got prepareId
     */
    public static final String BOOK_ORDER_URL = "/client/order-course";

    public static final String ADDRESS_MSG = "address_msg";
    /*
     * publish
     */
    public static final String PUBLISH_COMMENT_MERCHANT_ID = "publish_comment_merchant_id";
    public static final String PREFERENCE_KEY_COMMENT_PRE = "comment_content_merchant_";
    public static final String PUBLISH_COMMENT_LOCAL_MERCHANT_IDS = "local_comment_merchant_ids";

    public static final String BAIDU_API_KEY_STRING = "TOqi3KX2i0sOrYZtdF2olCcc";// "8FuAui8Ziss05GiUninPAS9Y";
    /***************
     * Intent key for Activity
     *********************/
    public static final String INTENT_OPEN_MERCHANT_DOOR_FROM_HOME = "intent_open_merchant_door_from_home";
    public static final String INTENT_OPEN_MERCHANT_DOOR_MERHCANT_ID = "merchant_open_door_merchant_id";
    public static final String INTENT_STATE_OF_IN_OR_OUT_MERCHANT = "state_in_or_out_merchant";
    public static final String INTENT_FANS_GYM_ID = "gym_id";
    public static final String INTENT_FANS_USER_ID = "user_id";
    public static final String SINGLE_CARD_MERCHANT_ID = "single_card_merchant_id";
    public static final String CARD_MERCHANT_BUY_FUNC = "card_merchant_buy_func";
    public static final String SINGLE_CARD_CLASS_ID = "single_card_class_id";
    public static final String CARD_MERCHANT_SELECT_DATA = "card_merchant_select_date";
    public static final String INTENT_FOLLOW_USER_ID = "follow_user_id";

    public static final String REGISTER_SUCC_CODE = "register_succ_code";

    public static final int ACTIVITY_TO_MESSAGE_CODE = 0;
    public static final int ACTIVITY_TO_AD_CODE = 2;
    public static final int ACTIVITY_TO_CIRCLE_UNREAD_CODE = 3;
    public static final int ACTIVITY_TO_CIRCLE_PUNCH_CODE = 4;
    public static final String EXTRA_KEY_NEED_JMP_TO_OTHER_ACTIVITY = "need_jump_other_activities";

    public static final int ACTIVITY_RESULT_LOGIN_CODE = 1;
    public static final int ACTIVITY_RESULT_LOGIN_SUCC = 1;
    public static final int ACTIVITY_RESULT_BUY_CARD_SUCC = 3;
    public static final int ACTIVITY_RESULT_BUY_CARD_FOR_FRIEND_CODE = 3;
    public static final int ACTIVITY_RESULT_BUY_CARD_CODE = 4;
    public static final int ACTIVITY_RESULT_ADVERTISEMENT = 7;
    public static final String GIFT_XX_COURSE_ALERT_HTML = "xx_course_alert_html";
    public static final String GIFT_XX_COURSE_ALERT_PROPORTION = "xx_course_aldert_proportion";
    public static final String ACTIVITY_RESULT_BUY_CARD_PHONE = "buy_card_phone";

    public static final String SCAN_QR_CODE_ORDER = "qr_code_order";
    public static final String SCAN_QR_CODE_RESULT = "qr_code_result";

    public static final String AD_DATA_URL = "/client/activities/";
    public static final String COUPON_TITLE_KEY = "coupon_title_key";
    public static final String COUPON_IS_NEW_USER = "coupon_is_new_user";
    public static final String SINGLE_COUPON_KEY = "single_coupon_key";
    public static final String IS_FROM_TAB_FRAGMENT_USER_JUMP_TO_COUPONS_ACTIVITY = "is_from_tab_user";
    public static final String SINGLE_COUPON_PRICE = "single_coupon_price";
    public static final int ACTIVITY_RESULT_COUPONS_CODE = 1;
    public static final int ACTIVITY_RESULT_COUPONS_SUCC = 2;
    public static final int ACTIVITY_RESULT_PHONE_NUM = 3;
    /*************
     * Umeng event
     **************/
    public static final String UMENG_EVENT_OPEN = "app_open";
    public static final String UMENG_EVENT_DATE_PICKER = "date_pick";
    public static final String UMENG_EVENT_LOGIN_START = "login_start";
    public static final String UMENG_EVENT_BUY_START = "buy_start";
    public static final String UMENG_EVENT_BUY_FINISH = "buy_finish";

    public static final String UMENG_EVENT_ACTIVITY_OPEN = "activities_open";
    public static final String UMENG_EVENT_SEARCH_CLICK = "search_click";
    public static final String UMENG_EVENT_MAP_CLICK = "map_click";
    public static final String UMENG_EVENT_ACTIVITY_CLICK = "activities_click";

    public static final String UMENG_CARD_BUY_MONTH_ALL_CLICK = "card_buy_month_all_click";
    public static final String UMENG_CARD_MY_ORDER_CLICK = "card_my_order_click";
    public static final String UMENG_CARD_ORDER_CLASS_CLICK = "card_order_class_click";
    public static final String UMENG_CARD_MY_FAVOR_CLICK = "card_my_favor_click";
    public static final String UMENG_CARD_BUY_MONTH_ONE_CLICK = "card_buy_month_one_click";
    public static final String UMENG_CARD_OPEN_DOOR_CLICK = "card_open_door_click";

    public static final String IM_BUY = "im_buy";
    public static final String TOTAL_PRICE = "total_price";

    public static final String ACTIVITY_SELECT_CITY = "select_city";
    public static final int ACTIVITY_SELECT_CITY_REQUEST_CODE = 777;

    public static final String AD_SAVE_DATA = "ad_save_data";

    

    /****************
     * Intent key
     ************************/
    public static final int ACTIVITY_RESULT_OUT_MERCHANT = 0x112;
    public static final int ACTIVITY_RESULT_END_MERCHANT = 0x123;
    public static final int ACTIVITY_RESULT_REOPEN_MERCHANT_DOOR = 0x111;
    public static final int ACTIVITY_ADDRESS_REQUEST_CODE = 9;
    public static final int ACTIVITY_ADDRESS_RESULT_CODE = 9;
    public static final int ACTIVITY_CHOOSE_CITY_REQUEST_CODE = 9;
    public static final int ACTIVITY_MESSAGE_TYPE_CARE = 3;
    public static final int ACTIVITY_MESSAGE_TYPE_LIKE = 1;
    public static final int ACTIVITY_MESSAGE_TYPE_COMMENTS = 2;
    public static final String DEFAULT_CITY_NAME = "default_city_name";
    public static final String ACTIVITY_ADDRESS_RESULT_KEY = "key_address_update";
    public static final String ACTIVITY_ADDRESS_LIST_RESULT_KEY = "key_address_update_list_position";
    //    public static final String ACTIVITY_ADDRESS_RESULT_DATA = "data_address_update";
    public static final String ACTIVITY_ADDRESS_RESULT_NAME_DATA = "data_address_name_update";
    public static final String ACTIVITY_ADDRESS_RESULT_ADDRESS_DATA = "data_address_address_update";
    public static final String ACTIVITY_ADDRESS_RESULT_LATITUDE_DATA = "data_address_latitude_update";
    public static final String ACTIVITY_ADDRESS_RESULT_LONGITUDE_DATA =
            "data_address_longitude_update";
    public static final String ACTIVITY_QUESTIONS_CHOSE_POSITION = "activity_questions_chose_pos";
    public static final String ACTIVITY_CARD_YEAR_ID = "activity_card_year_id";
    public static final String ACTIVITY_CARD_YEAR_CARD = "activity_year_card";
//    public static final String ACTIVITY_CARD_MERCHANT_ID = "activity_card_merchant_id";
    /****************
     * photo picker
     ************************/
    public final static String EXTRA_SELECTED_PHOTO = "SELECTED_PHOTO";
    public final static String EXTRA_SELECTED_PHOTO_INDEX = "SELECTED_PHOTO_INDEX";
    public final static String EXTRA_SELECTED_PHOTOS = "SELECTED_PHOTOS";
    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
    public final static int DEFAULT_MAX_COUNT = 9;

    public static final String PREFERENCE_KEY_GLANCE_POSITION = "ad_glance_position";
    public static final String PREFERENCE_KEY_AD_NUM = "ad_total_num";

    /************
     * address type
     ***************/
    public static final String ADDRESS_TYPE_HOME = "address_type_home";
    public static final String ADDRESS_TYPE_COMPANY = "address_type_company";
    public static final String ADDRESS_TYPE_NORMAL = "address_type_normal";

    /**
     * Fetch advertisement of last or new
     *
     * @return 0 location has nothing
     * @return 1 location has new ad
     * @return -1 location has ad but not new
     * @return -2 location has but net has new
     * @return 2 both location and net have nothing
     */
    public static final int AD_LOCATION_NOTHING = 0;
    public static final int AD_LOCATION_CONTAINS_NEW = 1;
    public static final int AD_LOCATION_CONTAINS_SKIMED = -1;

    public static final String HAD_SHOW_GUIDE_BEFORE = "has show guide_since_v_2_5";

    public static final String EXTRA_MSG_TYPE = "msy_type";
    public static final String EXTRA_MSG_GYM_ID = "msg_gym_id";
    public static final String EXTRA_MSG_GYM_NAME = "msg_gym_name";
    public static final String EXTRA_MSG_ORDER_ID = "msg_order_id";
    public static final String EXTRA_MSG_CIRCLE_DATA = "msg_cirle_data";
    public static final String EXTRA_MSG_COURSE_TYPE = "msg_course_type";
    public static final String EXTRA_MSG_GO_TO_SELECT_PICS = "msg_go_to_select_pics";
    public static final int ACTIVITY_REQUEST_POST_MSG_EMPTY = 1124;

    public static final String EXTRA_CIRCLE_UNREAD_MSG_ID = "unread_msg_id";

    public static final String PREFERENCE_KEY_MSG_PRE = "msg_content_";
    public static final String MSG_FAILED_LOCAL_IDS = "local_failed_msg_ids";


    public static final int ACTIVITY_REQUEST_CIRCLE_DETAIL = 1112;
    public static final String EXTRA_CIRCLE_DETAIL_CHANGED = "circle_detail_changed";
    public static final String EXTRA_CIRCLE_DETAIL_DATA = "circle_detail_data";
    public static final String EXTRA_CIRCLE_DETAIL_DELETE = "circle_detail_deleted";


    public static final String EXTRA_USER_HOME_DATA_CHANGED = "user_home_data_changed";

    public static final int ACTIVITY_REQUEST_USER_BACK_IMG = 11122;
    public static final String EXTRA_USER_BACK_IMG_DATA_CHANGED = "user_back_img_data_changed";

    public static final String EXTRA_COACH_ID = "coach_id";

    /************
     * requestCode
     ************/
    public static final int ACTIVITY_REQUEST_USERINFO_TO_SIGNATURE = 0x121;

    public static final String KEY_RESULT_SIGNATRUE = "key_signature";

    public static final String EXTRA_USER_ID = "current_user_id";


    public static final int USER_NICKNAME_MAX_LENGTH = 15;

    public static final String INTENT_RESULT_DATA_KEY = "last_tab_item";

    public static final String PREFERENCE_KEY_TODAY_FIT_DATA = "fit_data_today_json_string";
    public static final String PREFERENCE_KEY_JUST_LOGOUT = "just_logout";
    public static final String LAST_FETCH_FIT_DATA_TIME = "last_fetch_fit_data_time";

    public static final String PREFERENCE_KEY_MERCHANT_TOPIC_LAST_READ_MSG_ID = "merchant_topic_last_read_msg_id";


    public static final String PREFERENCE_KEY_GUIDE_MY_ORDERS = "has_show_guide_my_orders";
    public static final String PREFERENCE_KEY_GUIDE_GO_TO_ORDER = "has_show_guide_go_to_order";
    public static final String PREFERENCE_KEY_GUIDE_LEFT_RIGHT = "has_show_guide_left_right";

    public static final String PREFERENCE_KEY_GUIDE_ERP_ORDERS_V3_3 = "erp_order_list_guide_showed_v3.3";

    public static final String EXTRA_JUMP_FROM_SCAN_OR_ORDER_RESULT = "scan_or_order_result_jump_to_mainactivity";
    public static final String INTENT_KEY_MERCHANT_TIME_COUNT_FIRST = "merchant_time_count_first";

    public static final String REOPEN_DOOR_AFTER_IN_MERCHANT_TIME_COUNT = "reopen_door_in_merchant_time_count";
    public static final String INTENT_KEY_JUMP_TO_MERHCANT_CLASSLIST_FROM_SPECIAL_CARD = "jump_to_classlist_from_special_card";
    public static final String INTENT_KEY_JUMP_TO_LEFT_DAY_FROM_SPECIAL_CARD = "jump_to_left_day_from_special_card";

    //yearCardActivity
    public static final String JUMP_TO_YEAR_CARD_DESC_ACTIVITY_DESC = "jump_to_year_card_desc_activity_desc";

    public static final String JUMP_TO_YEAR_CARD_DESC_ACTIVITY_EXCHANGE = "jump_to_year_card_desc_activity_exchange";

    public static final String JUMP_TO_MY_ORDER_CARD_TYPE = "card_type";

    public static final String HAS_CHOOSED_CARD_TYPE = "has_choosed_card_type";

    public static final String INTENT_KEY_JUMP_TO_COUPONS_IS_SPECIAL = "is_special_merchant";

    public static final String INTENT_KEY_JUMP_TO_COUPONS_IS_YEAR_CARD = "is_year_card";
    public static final String INTENT_KEY_JUMP_TO_COUPONS_YEAR_CARD_ID = "year_card_id";

    /**
     * 场馆支持课程类型：仅仅支持三次通卡课程？支持ERP课程
     * 1 -> 场馆仅仅支持三次通卡课程，仅仅显示三次课程选项
     * 2 -> 场馆仅仅支持ERP课程，仅仅显示场馆会员选项
     * 3 -> 场馆支持三次通卡和ERP课程，显示三次通卡和场馆课程选项
     */
    public static final String GYM_SUPPORT_COURSE_TYPE = "gym_support_course_type";

    /**
     * 用户是该场馆什么类型会员
     * 1 -> 用户购买了ERP课程，只要用户购买了ERP课程，就默认显示场馆会员
     * 2 -> 用户仅仅购买了三次通卡，默认显示三次通卡
     * 3 -> 用户没有购买任何课程，默认显示场馆会员
     */
    public static final String THE_USER_TYPE_OF_GYM = "the_user_type_of_gym";

    /**
     * true 表示有氧空间场馆上线
     * false 表示有氧空间场馆下线
     */
    public static final String IS_SPECIAL_MERCHANT_ONLINE = "is_special_merchant_online";

    public static final String JUMP_FROM_MERCHANT_MOMENTS = "from_merchant_moments";

    /**
     * v3.3 开门逻辑更改
     * 仅通过传递该字段完成开门流程
     * 1 -> 进门开始锻炼
     * 2 -> 暂时离店
     * 3 -> 结束健身离店
     */
    public static final String BUNDLE_JUMP_TO_OPEN_DOOR_ACTION = "bundle_jump_to_open_door_action";

    public static final int ACTION_OF_OPEN_DOOR_FOR_START = 0;
    public static final int ACTION_OF_OPEN_DOOR_FOR_PAUSE = 2;
    public static final int ACTION_OF_OPEN_DOOR_FOR_FINISH = 1;


    public static final String PREFERENCE_KEY_IN_MERCHANT_TOTAL_TIME = "merchant_time_count_total_time";
    public static final String PREFERENCE_KEY_IN_MERCHANT_START_TIME = "merchant_time_start_time";

    public static final int ACTION_OF_OPEN_MERCHANT_DOOR_IN = 1;


    public static final String NEED_JUMP_TO_GYM_CARD_MANAGER_AUTO = "need_jump_to_gym_card_manager_auto";
}

