package com.kuaipao.base.net;

import com.android.volley.Request;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.response.AddCourseResponse;
import com.kuaipao.model.response.BusinessListResponse;
import com.kuaipao.model.response.CardTypeFilterResponse;
import com.kuaipao.model.response.CoachCourseDetailResponse;
import com.kuaipao.model.response.CoachCourseListResponse;
import com.kuaipao.model.response.CoachMemberListResponse;
import com.kuaipao.model.response.CoachPersonalCardsResponse;
import com.kuaipao.model.response.CoachPersonalCourseResponse;
import com.kuaipao.model.response.CoachReceiveCustomerListResponse;
import com.kuaipao.model.response.CoachReceiveRecordsResponse;
import com.kuaipao.model.response.MemberDetailInfoResponse;
import com.kuaipao.model.response.MembershipCardsResponse;
import com.kuaipao.model.response.UserInfoResponse;
import com.kuaipao.model.response.GymMembershipCardListResponse;
import com.kuaipao.model.response.BannerListResponse;
import com.kuaipao.model.response.LoginResponse;
import com.kuaipao.model.response.CustomerListResponse;
import com.kuaipao.model.response.OpenDoorQrPicResponse;
import com.kuaipao.model.response.UserGymStatusResponse;
import com.kuaipao.model.response.CoachReceiveCustomerResponse;
import com.kuaipao.model.response.MemberRecordInfoResponse;
import com.kuaipao.model.response.UserPhysicalRecordResponse;

/**
 * 后端服务注册
 * <p/>
 * 在注册的时候，需要指定接口名称，接口类型，返回结果的数据类型
 * 接口名称由后端提供，首字母不需要"/"
 * 接口类型分为两种：<p/>
 * <u>1:URL传递参数类型，后端能告诉我这是为什么吗？客户端通过URL传递参数有意义吗？</u>
 * <u>2:URL不带参数类型</u>
 * 请求类型有两种：get和post
 * <u>为什么要区分get和post呢</u>
 * 返回结果需继承BaseResponseData，自己解析JSON，后端同学能说一下，为什么同一个字段，同一个意思，在不同的接口中有不同的命名吗？
 * <p/>
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-20
 * Time: 17:31
 * Author: pingfu
 * FIXME
 */
public enum XService implements IService {
    /**
     * 首页
     */
    //登陆
    LOGIN("login", POST, LoginResponse.class),

    //广告列表
    BannerList("helper/banners/", BannerListResponse.class),

    //用户信息
    UserInfo("users/info", UserInfoResponse.class),

    /**
     * 扫描二维码
     */
    //获取二维码
    OpenDoorQR("gyms/%s/door-qrcode", OpenDoorQrPicResponse.class),

    //用户进店状态
    UserGymStatus("gyms/%s/user-in-gym-status", UserGymStatusResponse.class),

    /**
     * 场馆接口
     */
    //场馆列表
    GymList("client/gym/list", UserInfoResponse.class),

    //关注场馆
    CareGym("client/user/collect", BaseResult.class),

    //场馆会员卡列表
    GymMemberShipCardList("client/gym/%s/vip-service", GymMembershipCardListResponse.class),

    //商圈列表
    BusinessDistrict("client/business_block_list", BusinessListResponse.class),

    //卡片类型Filter接口
    SortTypeFilter("client/gym/filter/service", CardTypeFilterResponse.class),

    //场馆类型Filter接口
    CardTypeFilter("client/gym/filter/category", CardTypeFilterResponse.class),

    /**
     * 教练
     */
    //教练信息
    CoachInfo("client/coach/single", BaseResult.class),

    //教练接待人员列表
    CoachReceiveCustomerList("users", CoachReceiveCustomerListResponse.class),

    //教练接待记录
    CoachReceiveRecordList("follow-ups/", CoachReceiveRecordsResponse.class),

    //教练顾客列表（非会员列表）
    CoachMemberList("members/", CustomerListResponse.class),

    //教练私教顾客列表（会员列表）
    CoachPersonalMemberList("members/", CustomerListResponse.class),

    //接待客户信息
    CoachReceiveCustomer("customer/", POST, CoachReceiveCustomerResponse.class),

    //私教会员
    COACH_MEMBERS("/courses/ic-members/coach/%s", CoachMemberListResponse.class),

    /**
     * 课程
     */
    //教练私教课程
    CoachPersonalCourse("client/coach-combo-detail", CoachPersonalCourseResponse.class),

    //ERP课程详情
    MembershipCourse("client/erp/course/%s", OpenDoorQrPicResponse.class),

    //取消预约ERP课程
    UnBookMembershipCourse("client/erp/order/cancel", POST, BaseResult.class),

    //教练私教课程列表
    CoachCourseList("ic-arrangement/arrangement/", CoachCourseListResponse.class),

    //教练私教课详情
    CoachCourseDetail("ic-arrangement/arrangement/%s", CoachCourseDetailResponse.class),

    //添加课程
    AddCourse("ic-arrangement/arrangement/", POST, AddCourseResponse.class),

    //更新课程
    UpdateCourse("ic-arrangement/arrangement/%s", Request.Method.PUT, BaseResult.class),

    /**
     * 会员接口
     */
    //会员记录信息（跟进记录及体侧数据）
    MemberRecordInfo("helper/members/%s/info-summary", MemberRecordInfoResponse.class),

    //会员详情
    MemberDetailInfo("members/%s", MemberDetailInfoResponse.class),

    //添加优惠券
    AddCoupon("client/coupon/get", POST, BaseResult.class),

    //用户私教列表
    UserCoachCourses("client/user/erp-gym/%s/combo-order", CoachReceiveCustomerResponse.class),

    //用户体检记录
    UserPhysicalRecord("members/%s/physical-exams", UserPhysicalRecordResponse.class),

    /**
     * 其他
     */
    //用户会员卡列表
    MembershipCardList("members/%s/membership-cards", MembershipCardsResponse.class),

    //用户私教卡列表
    CoachPersonalCardList("courses/individuals/orders/", CoachPersonalCardsResponse.class),

    //获取用户最新信息
    AddReceiveRecord("follow-ups/", POST, BaseResult.class),

    //编辑体侧数据
    UpdatePhysicalRecord("members/%s/physical-exams/%s", Request.Method.PUT, BaseResult.class),

    //添加体侧数据
    AddPhysicalRecord("members/%s/physical-exams", POST, BaseResult.class),

    //更改用户在场馆的信息
    UpdateGymUserInfo("client/erp/%s/user-info", POST, BaseResult.class),

    //激活场馆卡
    ActiveMembershipCard("client/card/teddy-membership-card/%s", POST, BaseResult.class),

    //完成上课
    FinishCourse("courses/individuals/orders/records/", POST, BaseResult.class),

    //删除课程
    DeleteCourse("ic-arrangement/arrangement/%s", Request.Method.DELETE, BaseResult.class)
    ;
    //网络请求URL
    private final String mUrl;

    private final int mRequestType;

    private final Class<? extends BaseResult> mClazz;


    XService(String realUrl, Class<? extends BaseResult> clazz) {
        this(realUrl, GET, clazz);
    }

    XService(String url, int requestType, Class<? extends BaseResult> clazz) {
        mUrl = url;
        mRequestType = requestType;
        mClazz = clazz;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    public int getRequestType() {
        return mRequestType;
    }

    @Override
    public Class<? extends BaseResult> getResultClazz() {
        return mClazz;
    }
}
