package com.kuaipao.model.event;

/**
 * Created by ZhanTao on 2/3/16.
 */
public class WebBaseEvent {
    public boolean bResult;

    public int nFailedType;

    public static final int FAILED_TYPE_NO_NETWORK = 1;
    public static final int FAILED_TYPE_UNKOWN = 2;
    public static final int FAILED_TYPE_RESPONSE_CODE_WRONG = 3;// code != 0: wrong
    public static final int FAILED_TYPE_RESPONSE_DATA_FORMAT_WRONG = 4;

    public WebBaseEvent(boolean bResult) {
        this.bResult = bResult;
    }
}
