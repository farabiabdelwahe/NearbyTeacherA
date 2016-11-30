package com.example.gsc.template2.Back.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.example.gsc.template2.R;
import com.example.gsc.template2.Splash;

/**
 * Created by GSC on 29/11/2016.
 */

public class AlarmService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, Splash.class);
        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Session");
        builder.setContentText("Your session is about to start");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId ) {
        super.onStartCommand(intent,flags,startId);
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, Splash.class);
        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Time is up ");
        builder.setContentText("your session is about to start");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        return START_NOT_STICKY;
    }
}