package com.example.gsc.template2.Back.push;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by GSC on 29/11/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, AlarmService.class);
        context.startService(service1);
    }
}
