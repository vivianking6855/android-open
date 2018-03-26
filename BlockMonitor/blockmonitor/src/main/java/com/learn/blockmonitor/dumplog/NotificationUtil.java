package com.learn.blockmonitor.dumplog;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.learn.blockmonitor.R;
import com.learn.blockmonitor.ui.StackLogActivity;

/**
 * NotificationUtil on 2018/3/26.
 *
 */

class NotificationUtil {

    private NotificationManagerCompat notificationManager;
    private static final String CHANNEL_ID ="blockMonitor";
    private static final int NOTIFY_ID = 100;
    private Context context;
    private String notifyTitle;

    NotificationUtil(Context ctx){
        context = ctx;
        notifyTitle = context.getString(R.string.channel_description);
        notificationManager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            @SuppressLint("WrongConstant")
            NotificationChannel channel =new NotificationChannel(CHANNEL_ID, name,importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE));
             if(notificationManager !=null) {
                 notificationManager.createNotificationChannel(channel);
             }
        }

    }

    void showNotification(String msg){
        Intent intent = new Intent(context, StackLogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle(notifyTitle)
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        notificationManager.notify(NOTIFY_ID,builder.build());
    }
}
