package com.szeyankwok.foodtimer;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannel extends Application {

    public static final String CHANNEL_ID = "Food Timer";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel1 = new android.app.NotificationChannel(CHANNEL_ID, "Food Timer", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Regular Notification");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }

    }
}
