package com.oneside.model.data;

import com.oneside.base.net.RequestDelegate;
import com.oneside.base.net.UrlRequest;

import java.util.List;

/**
 * Created by guoming on 6/3/16.
 */
public abstract class BaseDataSource {
    public interface OnDataResultListener {

        public void onReloadDataFinished(BaseDataSource dataSource, boolean result);

        public void onLoadMoreDataFinished(BaseDataSource dataSource, boolean result);
    }


    protected int limit;
    protected BaseDataList dataList;
    protected OnDataResultListener onDataResultListener;

    public BaseDataSource(int limit) {
        this.limit = limit;
    }

    public List getDataList(){
        if (dataList == null){
            return null;
        }
        return dataList.getList();
    }

    public boolean isHasMore() {
        if (dataList != null) {
            return dataList.isHasMore();
        }
        return false;
    }

    public int getLimit(){
        return limit;
    }

    public void loadData(final boolean refresh) {
        if (refresh) {
            resetData();
        }
        UrlRequest r = getUrlRequest();
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String errorString) {
                fireResultListener(refresh, false);
            }

            @Override
            public void requestFinished(UrlRequest request) {
//                JSONObject jsonObject = request.getJsonData();
//
//                JSONObject dataJson = WebUtils.getJsonObject(jsonObject, "data");
//                if (dataJson != null) {
//                    CardErpOrderList orderList = CardErpOrderList.fromJson(dataJson);
//                    addDataList(orderList, refresh);
//                }
                fireResultListener(refresh, true);
            }
        });
        r.start();
    }

    public void setOnDataResultListener(OnDataResultListener onDataResultListener) {
        this.onDataResultListener = onDataResultListener;
    }


    public abstract UrlRequest getUrlRequest();

    protected void addDataList(BaseDataList orderList, boolean refresh) {
        if (orderList == null) {
            return;
        }
        if (dataList == null) {
            dataList = orderList;
        } else {
            dataList.addOtherList(orderList, refresh);
        }

    }

    private void fireResultListener(boolean refresh, boolean result) {
        if (onDataResultListener != null) {
            if (refresh) {
                onDataResultListener.onReloadDataFinished(this, result);
            } else {
                onDataResultListener.onLoadMoreDataFinished(this, result);
            }
        }
    }

    private void resetData() {
        if (dataList != null) {
            dataList.setHasMore(true);
            dataList.setPage(0);
        }
    }
}
