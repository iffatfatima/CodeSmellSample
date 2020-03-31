package com.example.app;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

//HSS
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        startActivity(new Intent(this, CameraActivity.class));//IB
        Map<String, String> map = remoteMessage.getData();
        for (String key: map.keySet()){
            Log.d(TAG, "Key: "+ key);
            Log.d(TAG, "Value: "+ map.get(key));
        }
        if (remoteMessage.getMessageId().equals("Admin")){
            Log.d(TAG, "Admin: "+ remoteMessage.getMessageId());
        }
        if (!remoteMessage.getMessageType().isEmpty()){
            Log.d(TAG, "Message Type : "+ remoteMessage.getMessageType());
        }
        if (remoteMessage.getNotification() != null){
            Log.d(TAG, "Notification Body : "+ remoteMessage.getNotification().getBody());
            Log.d(TAG, "Notification Action : "+ remoteMessage.getNotification().getClickAction());
            Log.d(TAG, "Notification Title : "+ remoteMessage.getNotification().getTitle());
        }
        if (remoteMessage.getSentTime() > System.currentTimeMillis() - 5000){
            Log.d(TAG, "Notification came within last 5 seconds");
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }
}