package com.task;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Myservice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder notifibu = new NotificationCompat.Builder(this, getApplication().getPackageName());
        notifibu.setContentIntent(pendingIntent);
        notifibu.setContentTitle(getApplication().getPackageName() + "正在运行！");
        notifibu.setContentText("点击查看详情");
        notifibu.setWhen(System.currentTimeMillis());
        notifibu.setSmallIcon(R.drawable.ic_launcher_background);
        NotificationManagerCompat.from(this).notify(1, notifibu.build());
        startForeground(1, notifibu.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
