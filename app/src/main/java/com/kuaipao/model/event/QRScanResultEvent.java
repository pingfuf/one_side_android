package com.kuaipao.model.event;

/**
 * Created by MVEN on 16/7/6.
 * <p/>
 * email: magiwen@126.com.
 */


public class QRScanResultEvent extends WebBaseEvent {
    private long orderID;

    public QRScanResultEvent(boolean bResult) {
        super(bResult);
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public long getOrderID() {
        return orderID;
    }
}
