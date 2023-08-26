package com.example.derash2;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.*;
import com.google.gson.Gson;
 public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(Utlis.TAG,token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getNotification().getTitle();
        String content = remoteMessage.getNotification().getBody();
        String data = new Gson().toJson(remoteMessage.getData());

        Utlis.showNotification(this,title,content);
        Log.d(Utlis.TAG,data);

    }
}