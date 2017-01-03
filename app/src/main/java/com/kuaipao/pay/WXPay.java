package com.kuaipao.pay;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.utils.WebUtils;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.base.net.RequestDelegate;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import static com.kuaipao.utils.WebUtils.getJsonInt;
import static com.kuaipao.utils.WebUtils.getJsonObject;
import static com.kuaipao.utils.WebUtils.getJsonString;

public class WXPay {

    private static final String PREPAY_URL = "client/wxpay/prepay";
    private static final String PAY_SINGLE_CARD = "/client/wxpay/course";
    private static final String PAY_YEAR_CARD = "/client/wxpay/gym-card";
    private static final String PAY_SHOWER = "client/wxpay/shower";


    private static final String PAY_PERSONAL_SINGLE = "/client/wxpay/course";
    private static final String PAY_COACH_PLAN_CARD = "/client/wxpay/personal-course";

    PayReq req;
    final IWXAPI msgApi;
    Map<String, String> resultunifiedorder = new HashMap<String, String>();
    StringBuffer sb;
    Context myCon;

    // boolean onlyBalancePay = false;//add by shi 只使用余额支付为true

    /**
     * 需要重新从后台获取，此处为测试数据
     */

    public WXPay(Context context) {
        myCon = context;
        msgApi = WXAPIFactory.createWXAPI(myCon, null);
        msgApi.registerApp(Constant.APP_ID);
        req = new PayReq();
        sb = new StringBuffer();
    }

    public void pay(final int count, final long couponId, final String phoneNum, final int level,
                    final String city, final PayResultListener l) {
        pay(count, couponId, phoneNum, level, city, 0, l);
    }

    /**
     * @param count
     * @param couponId
     * @param phoneNum
     * @param level
     * @param city
     * @param special  当参数为1时表示取有氧空间的场馆，不传这个参数则为购买小熊通卡
     * @param l
     */
    public void pay(final int count, final long couponId, final String phoneNum, final int level,
                    final String city, int special, final PayResultListener l) {

        UrlRequest r = new UrlRequest(PREPAY_URL);
        r.addPostParam("amount", count);
        r.addPostParam("platform", "Android");
        if (LangUtils.isNotEmpty(phoneNum))
            r.addPostParam("phone", phoneNum);
        if (LangUtils.isNotEmpty(city))
            r.addPostParam("city", city);
        r.addPostParam("level", level);
        if (couponId > 0)
            r.addPostParam("coupon_id", couponId);
        r.addPostParam("use_balance", "1");// add by shi ,1 stand for use

        if (special != 0) {
            r.addPostParam("special", special);
        }
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {

            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                ViewUtils.showToast("prePay支付失败", Toast.LENGTH_SHORT);
            }
        });
        r.start();

    }

    public void payShower(final BaseActivity a,final int unitPrice,final int amount,final long gymID,final PayResultListener l){
        a.showLoadingDialog();
        UrlRequest r = new UrlRequest(PAY_SHOWER);
        r.addPostParam("gym_id", gymID);
        r.addPostParam("amount",amount);
        r.addPostParam("unit_price",unitPrice);
        r.addPostParam("platform", "Android");
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String errorString) {
                ViewUtils.runInHandlerThread(new Runnable() {
                    @Override
                    public void run() {
                        a.dismissLoadingDialog();
                        ViewUtils.showToast("购买淋浴时间支付失败", Toast.LENGTH_SHORT);
                    }
                });

                l.onResult(false);
            }

            @Override
            public void requestFinished(UrlRequest request) {
                ViewUtils.runInHandlerThread(new Runnable() {
                    @Override
                    public void run() {
                        a.dismissLoadingDialog();
                    }
                });
            }
        });
        r.start();
    }
    public void paySingleCard(final String classId, final PayResultListener l) {

        UrlRequest r = new UrlRequest(PAY_SINGLE_CARD);
        r.addPostParam("course_id", classId);
        r.addPostParam("platform", "Android");
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {

            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                if (LangUtils.isNotEmpty(message)) {
                    AlipayPay.showConflictDialog((BaseActivity) myCon, message);
                } else
                    ViewUtils.showToast("购买单次卡支付失败", Toast.LENGTH_SHORT);
            }
        });
        r.start();

    }

    public void payYearCard(final String cardId, final long gymID, final long couponID,final PayResultListener l) {

        UrlRequest r = new UrlRequest(PAY_YEAR_CARD);
        r.addPostParam("gym_card_id", cardId);
        r.addPostParam("gym_id", gymID);
        r.addPostParam("platform", "Android");
        if (couponID > 0)
            r.addPostParam("coupon_id", couponID);
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {

            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                if (LangUtils.isNotEmpty(message)) {
                    AlipayPay.showConflictDialog((BaseActivity) myCon, message);
                } else
                    ViewUtils.showToast("购买年卡支付失败", Toast.LENGTH_SHORT);
            }
        });
        r.start();

    }


    public void payPersonalSingle(final String classId, final PayResultListener l) {

        UrlRequest r = new UrlRequest(PAY_PERSONAL_SINGLE);
        r.addPostParam("course_id", classId);
        r.addPostParam("platform", "Android");
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {

            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                if (LangUtils.isNotEmpty(message)) {
                    AlipayPay.showConflictDialog((BaseActivity) myCon, message);
                } else
                    ViewUtils.showToast("购买私教课支付失败", Toast.LENGTH_SHORT);
            }
        });
        r.start();

    }

    public void payCoachPlan(final String comboID, final PayResultListener l) {

        UrlRequest r = new UrlRequest(PAY_COACH_PLAN_CARD);
        r.addPostParam("combo_id", comboID);
        r.addPostParam("platform", "Android");
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {
            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                if (LangUtils.isNotEmpty(message)) {
                    AlipayPay.showConflictDialog((BaseActivity) myCon, message);
                } else
                    ViewUtils.showToast("购买私教套餐支付失败", Toast.LENGTH_SHORT);
            }
        });
        r.start();

    }

    private void genPayReq(JSONObject json) {

        // "partnerid": "1236394002",
        // "package": "Sign=WXPay",
        // "prepayid": "wx2015052116151513bc3916b00681534073",
        // "timestamp": "1432195833",
        // "noncestr": "q2o1krr7czwgeuyptrtve1i30e3cidzs",
        // "appid": "wx445365abee4c4c01",
        // "sign": "657651A53142E2DF423EF95BDD24C318"

        req.appId = Constant.APP_ID;
        req.partnerId = getJsonString(json, "partnerid", "");// Constant.MCH_ID;
        req.prepayId = getJsonString(json, "prepayid", "");// prePayId;
        req.packageValue = getJsonString(json, "package", "");// "prepay_id=" + prePayId;
        req.nonceStr = getJsonString(json, "noncestr", "");// genNonceStr();
        req.timeStamp = getJsonString(json, "timestamp", "");// String.valueOf(genTimeStamp());


        req.sign = getJsonString(json, "sign", "");// genAppSign(signParams);
        LogUtils.d("ddddddddddddd gen pay req");
    }

    private void sendPayReq() {

        msgApi.sendReq(req);
        LogUtils.d("ddddddddddddd send pay req");
    }


}
