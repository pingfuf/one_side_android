package com.oneside.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.oneside.manager.CardSessionManager;
import com.oneside.manager.CardSessionManager.NetworkStatus;
import com.oneside.model.event.NetWorkChangedEvent;
import com.oneside.utils.LogUtils;
import com.oneside.utils.WebUtils;

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
