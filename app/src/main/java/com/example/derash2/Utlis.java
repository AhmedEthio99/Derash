package com.example.derash2;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

import android.app.*;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class Utlis {
    public static final String  TAG = "Hello";
    public static void showNotification(Context context,String title, String body){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"com.example.derash2");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(title);
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.setSummaryText(title);

        builder.setStyle(bigTextStyle);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (VERSION.SDK_INT >= VERSION_CODES.O){
           String channelId = "com.example.derash2.id";
            NotificationChannel channel = new NotificationChannel(channelId,"DERASH",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        manager.notify(new Random().nextInt(),builder.build());

    }
}
