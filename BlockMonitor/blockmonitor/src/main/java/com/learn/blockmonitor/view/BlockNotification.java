package com.learn.blockmonitor.view;

import android.content.Context;

import com.learn.blockmonitor.data.listener.IMonitorListener;
import com.learn.blockmonitor.share.util.NotificationUtil;

import java.lang.ref.Reference;

public class BlockNotification implements IMonitorListener {
    private NotificationUtil notificationUtil;
    private Reference<Context> mContextRef;

    public BlockNotification(Reference<Context> ref) {
        mContextRef = ref;
        notificationUtil = new NotificationUtil(mContextRef.get());
    }

    @Override
    public void onBlocked(String msg) {
        notificationUtil.showNotification(msg);
    }
}
