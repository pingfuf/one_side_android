package com.kuaipao.model.request;

import com.kuaipao.base.net.model.BaseRequestParam;

/**
 * Created by MVEN on 16/8/2.
 * <p/>
 * email: magiwen@126.com.
 */


public class ShowerQRRequest extends BaseRequestParam {
    // ACTION_ENTER = 1 # 进店 ACTION_AWAY = 2 # 离店 ACTION_STEP_OUT = 3 # 暂时离开 ACTION_SHOWER = 4 # 淋浴
    public int action;

    @Override
    protected void addRequestParams() {
        addParam("action", 4);
    }
}
