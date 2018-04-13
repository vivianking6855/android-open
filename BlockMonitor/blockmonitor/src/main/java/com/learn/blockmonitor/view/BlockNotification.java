package com.learn.blockmonitor.view;

import android.content.Context;

import com.learn.blockmonitor.share.util.NotificationUtil;
import com.learn.blockmonitor.data.listener.IMonitorListener;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class BlockNotification implements IMonitorListener {
    private NotificationUtil notificationUtil;
    private Reference<Context> mContextRef;

    public BlockNotification(Context context) {
        mContextRef = new WeakReference<>(context);
        notificationUtil = new NotificationUtil(mContextRef.get());
    }

    @Override
    public void onBlocked(String msg) {
        notificationUtil.showNotification(msg);
    }
}
