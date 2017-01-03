package com.kuaipao.pay;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.ui.CustomDialog;
import com.kuaipao.manager.R;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.utils.WebUtils;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.base.net.RequestDelegate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AlipayPay {

    private static final String PREPAY_URL = "client/alipay/prepay";
    private static final String PAY_SINGLE_CARD = "/client/alipay/course";
    private static final String PAY_YEAR_CARD = "/client/alipay/gym-card";
    private static final String PAY_SHOWER = "client/alipay/shower";

    private static final String PAY_PERSONAL_SINGLE = "/client/alipay/course";
    private static final String PAY_COACH_PLAN_CARD = "/client/alipay/personal-course";

    // private boolean onlyBalancePay = false;// add by shi 只使用余额支付为true

    /**
     * call alipay sdk pay.
     */
    public static void pay(final Activity a, final String sign, final PayResultListener l) {


        // // 订单
        // String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
        //
        // // 对订单做RSA 签名
        // String sign = sign(orderInfo);
        // try {
        // // 仅需对sign 做URL编码
        // sign = URLEncoder.encode(sign, "UTF-8");
        // } catch (UnsupportedEncodingException e) {
        // e.printStackTrace();
        // }
        //
        // // 完整的符合支付宝参数规范的订单信息
        // final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                // 调用支付接口，获取支付结果

                AlipayPayResult payResult = new AlipayPayResult("");
                // String resultInfo = payResult.getResult();

                final String resultStatus = payResult.getResultStatus();


                ViewUtils.runInHandlerThread(new Runnable() {

                    @Override
                    public void run() {

                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // Toast.makeText(a, "支付成功", Toast.LENGTH_SHORT).show();
                            l.onResult(true);
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(a, "支付结果确认中", Toast.LENGTH_SHORT).show();
                                l.onResult(true);

                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                Toast.makeText(a, "支付失败", Toast.LENGTH_SHORT).show();
                                l.onResult(false);

                            }
                        }
                    }
                });


            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static void prePay(final BaseActivity a, final int count, final long couponId,
                              final String phoneNum, int level, String city, final PayResultListener l) {
        prePay(a, count, couponId, phoneNum, level, city, 0, l);
    }

    /**
     * @param a
     * @param count
     * @param couponId
     * @param phoneNum
     * @param level
     * @param city
     * @param special  当参数为1时表示取有氧空间的场馆，不传这个参数则为购买小熊通卡
     * @param l
     */
    public static void prePay(final BaseActivity a, final int count, final long couponId,
                              final String phoneNum, int level, String city, int special, final PayResultListener l) {
        // HashMap<String, Object> params = new HashMap<String, Object>();
        // params.put("amount", count);
        // params.put("platform", "Android");
        a.showLoadingDialog();
        UrlRequest r = new UrlRequest(PREPAY_URL);
        r.addPostParam("amount", count);
        if (LangUtils.isNotEmpty(phoneNum))
            r.addPostParam("phone", phoneNum);
        if (LangUtils.isNotEmpty(city))
            r.addPostParam("city", city);
        r.addPostParam("level", level);
        if (special != 0)
            r.addPostParam("special", special);
        r.addPostParam("platform", "Android");
        if (couponId > 0)
            r.addPostParam("coupon_id", couponId);
        r.addPostParam("use_balance", "1");// add by shi ,1 stand for use
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {
            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                a.dismissLoadingDialog();
                ViewUtils.showToast("prePay支付失败", Toast.LENGTH_SHORT);
                l.onResult(false);
            }
        });
        r.start();
    }

    public static void payShower(final BaseActivity a,final int unitPrice,final int amount,final long gymID,
                                 final PayResultListener l){
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
            }
        });
        r.start();
    }
    public static void paySingleCard(final BaseActivity a, final String classId,
                                     final PayResultListener l) {
        a.showLoadingDialog();
        UrlRequest r = new UrlRequest(PAY_SINGLE_CARD);
        r.addPostParam("course_id", classId);
        r.addPostParam("platform", "Android");
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {
            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                ViewUtils.runInHandlerThread(new Runnable() {
                    @Override
                    public void run() {
                        a.dismissLoadingDialog();
                        ViewUtils.showToast("购买单次卡支付失败", Toast.LENGTH_SHORT);
                    }
                });

                l.onResult(false);
            }
        });
        r.start();
    }

    public static void payYearCard(final BaseActivity a, final String cardId, final long gymID,final long couponID,
                                   final PayResultListener l) {
        a.showLoadingDialog();
        UrlRequest r = new UrlRequest(PAY_YEAR_CARD);
        r.addPostParam("gym_card_id", cardId);
        r.addPostParam("platform", "Android");
        r.addPostParam("gym_id", gymID);
        if (couponID > 0)
            r.addPostParam("coupon_id", couponID);
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {
                l.onResult(false);
            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                a.dismissLoadingDialog();
                if (LangUtils.isNotEmpty(message)) {
                    showConflictDialog(a, message);
                } else
                    ViewUtils.showToast("支付失败", Toast.LENGTH_SHORT);
                l.onResult(false);
            }
        });
        r.start();
    }


    public static void payPersonalSingle(final BaseActivity a, final String classId,
                                         final PayResultListener l) {
        a.showLoadingDialog();
        UrlRequest r = new UrlRequest(PAY_PERSONAL_SINGLE);
        r.addPostParam("course_id", classId);
        r.addPostParam("platform", "Android");
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {
            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                a.dismissLoadingDialog();
                if (LangUtils.isNotEmpty(message)) {
                    showConflictDialog(a, message);
                } else
                    ViewUtils.showToast("购买私教课支付失败", Toast.LENGTH_SHORT);
                l.onResult(false);
            }
        });
        r.start();
    }


    //TODO
    public static void payCoachPlan(final BaseActivity a, final String comboID,
                                    final PayResultListener l) {
        a.showLoadingDialog();
        UrlRequest r = new UrlRequest(PAY_COACH_PLAN_CARD);
        r.addPostParam("combo_id", comboID);
        r.addPostParam("platform", "Android");
        r.setDelegate(new RequestDelegate() {

            public void requestFinished(UrlRequest request) {
            }

            public void requestFailed(UrlRequest request, int statusCode, String message) {
                a.dismissLoadingDialog();
                if (LangUtils.isNotEmpty(message)) {
                    showConflictDialog(a, message);
                } else
                    ViewUtils.showToast("购买私教套餐支付失败", Toast.LENGTH_SHORT);
                l.onResult(false);
            }
        });
        r.start();
    }

    public static void check(final BaseActivity a, final int count, final long couponId,
                             final String phoneNum, final int level, final String city, final PayResultListener l) {
        check(a, count, couponId, phoneNum, level, city, 0, l);
    }

    /**
     * check whether the device has authentication alipay account.
     *
     * @param special 1:有氧空间卡 0:其他卡
     */
    public static void check(final BaseActivity a, final int count, final long couponId,
                             final String phoneNum, final int level, final String city, final int special, final PayResultListener l) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象


            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version.
     */
    public static void getSDKVersion(Activity a) {

    }

    /**
     * create the order info.
     */
    public static String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Constant.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Constant.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order.
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info.
     *
     * @param content 待签名订单信息
     */
    public static String sign(String content) {
        return AlipaySignUtils.sign(content, Constant.RSA_PRIVATE);
    }

    /**
     * get the sign type we use.
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }


    public static void showConflictDialog(final Activity activity, final String msg) {
        ViewUtils.runInHandlerThread(new Runnable() {
            @Override
            public void run() {
                CustomDialog.Builder b = new CustomDialog.Builder(activity)/* .setTitle(R.string.dialog_pay_title) */
                        .setPositiveButton(R.string.yes, null)/* .setNegativeButton(R.string.no, null) */;
                CustomDialog d = b.create();
                d.setMessage(msg + "\n");
//     CustomDialogHelper.setIconDialogMessage(d, R.drawable.ic_book_failed, msg);
                d.show();
            }
        });

    }
}
