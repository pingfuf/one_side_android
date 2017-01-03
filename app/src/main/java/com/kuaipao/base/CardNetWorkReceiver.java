package com.kuaipao.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kuaipao.manager.CardSessionManager;
import com.kuaipao.manager.CardSessionManager.NetworkStatus;
import com.kuaipao.model.event.NetWorkChangedEvent;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.WebUtils;

import org.greenrobot.eventbus.EventBus;

public class CardNetWorkReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (CardSessionManager.getInstance() != null) {
            boolean isConnected = WebUtils.isNetworkConnected(context);
            LogUtils.d("isNetworkConnected %s", isConnected);
            if (isConnected != (CardSessionManager.getInstance().getNetworkStatus() == NetworkStatus.OnLine)) {
                NetworkStatus mode = isConnected ? NetworkStatus.OnLine : NetworkStatus.OffLine;
                CardSessionManager.getInstance().setNetworkStatus(mode);
                EventBus.getDefault().post(new NetWorkChangedEvent(mode));
                CardSessionManager.getInstance().updateFromServer();
            }
        }
    }


}
