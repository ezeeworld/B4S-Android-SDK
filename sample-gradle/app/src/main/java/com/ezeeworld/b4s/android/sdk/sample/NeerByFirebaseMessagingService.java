package com.ezeeworld.b4s.android.sdk.sample;

import android.app.Activity;
import android.content.Context;

import com.ezeeworld.b4s.android.sdk.B4SLog;
import com.ezeeworld.b4s.android.sdk.push.PushApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NeerByFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = "PushApi";

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);

        PushApi.registerFirebaseToken(newToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        PushApi.onMessageReceived(getApplicationContext(), remoteMessage.getData());
    }

    public static void initFirebase(Activity activity) {
        try {
            FirebaseApp.initializeApp(activity);
        } catch (Exception e) {
            B4SLog.e("PushAPI", "FirebaseApp init failed:"+e.toString());
            e.printStackTrace();
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( activity,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                PushApi.registerFirebaseToken(newToken);
            }
        });
    }
}
