package com.kuaipao.ui.home;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.base.net.XService;
import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.response.OpenDoorQrPicResponse;
import com.kuaipao.base.BaseFragment;
import com.kuaipao.base.inject.From;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.manager.R;
import com.kuaipao.zxing.decoding.BitmapUtils;

/**
 * Created by MVEN on 16/3/18.
 */
@XAnnotation(layoutId = R.layout.fragment_open_door_qr_pic)
public class OpenDoorQRPicFragment extends BaseFragment {
    private static final long SPACE_TIME = 30 * 1000;

    @From(R.id.iv_qr_pic)
    private ImageView ivQr;

    private long mGymId;

    private int mType;

    private int mRequestNo;

    boolean isFinished;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRequestNo = 0;
        mGymId = 0;
        mType = 0;
        isFinished = true;
    }

    public void startFetchQrPic(int type, long gymId) {
        synchronized (OpenDoorQRPicFragment.class) {
            if (isFinished || mGymId != gymId) {
                isFinished = false;
                mGymId = gymId;
                mType = type;
                mRequestNo++;
                fetchQRPic();
            }
        }
    }

    public synchronized void stopFetch() {
        isFinished = true;
    }

    private void fetchQRPic() {
        if (!isValidFragment()) {
            return;
        }
        BaseRequestParam param = new BaseRequestParam();
        param.addUrlParams(mGymId);
        param.addParam("action", mType);

        startRequest(XService.OpenDoorQR, param, mRequestNo);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult result) {
        super.onResponseSuccess(request, result);
        if (request != null && request.getNetworkParam() != null
                && request.getNetworkParam().requestNo == mRequestNo) {
            OpenDoorQrPicResponse response = (OpenDoorQrPicResponse) result;
            freshImageView(response);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        ivQr.setImageDrawable(null);
        ViewUtils.showToast(message, Toast.LENGTH_SHORT);
        loopFetchQrPic(SPACE_TIME / 2);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        ivQr.setImageDrawable(null);
        ViewUtils.showToast("网络异常", Toast.LENGTH_SHORT);
        loopFetchQrPic(SPACE_TIME / 2);
    }

    private void freshImageView(OpenDoorQrPicResponse response) {
        if (response == null) {
            return;
        }

        Bitmap bitmap;
        try {
            bitmap = BitmapUtils.createQRCode(response.qrcode, ViewUtils.rp(250));
        } catch (WriterException e1) {
            e1.printStackTrace();
            bitmap = null;
        }

        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            ivQr.setImageDrawable(drawable);
        } else {
            ivQr.setImageDrawable(null);
        }

        loopFetchQrPic(SPACE_TIME);
    }

    private void loopFetchQrPic(long time) {

        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinished) {
                    fetchQRPic();
                }
            }
        }, time);
        ;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
