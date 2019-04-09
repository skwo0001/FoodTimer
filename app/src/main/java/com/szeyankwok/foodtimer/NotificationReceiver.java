package com.szeyankwok.foodtimer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
