package com.bahwell.inoncharge.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.AboutActivity;
import com.bahwell.inoncharge.activity.OrderConfirmationActivity;
import com.bahwell.inoncharge.activity.SplashActivity;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InonFirebaseMessagingService extends FirebaseMessagingService {

    String _ID, _NAME, _TOTAL_HARGA, _TITLE, _hari_or_jam, _date_awal, _date_ahir, _date,
            _time_awal, _time_ahir, _ID_TRANSAKSI;

    public InonFirebaseMessagingService() {

    }

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message Click: " + remoteMessage.getNotification().getClickAction());
        Log.d(TAG, "Notification Data: " + remoteMessage.getData());

        _ID             = remoteMessage.getData().get("_ID");
        _NAME           = remoteMessage.getData().get("_NAME");
        _TOTAL_HARGA    = remoteMessage.getData().get("_TOTAL_HARGA");
        _TITLE          = remoteMessage.getData().get("_TITLE");
        _hari_or_jam    = remoteMessage.getData().get("_hari_or_jam");
        _ID_TRANSAKSI    = remoteMessage.getData().get("_ID_TRANSAKSI");

        if (_hari_or_jam.equals("true")){
            _date_awal = remoteMessage.getData().get("_date_awal");
            _date_ahir = remoteMessage.getData().get("_date_ahir");
        }
        else if (_hari_or_jam.equals("false")){
            _date = remoteMessage.getData().get("_date");
            _time_awal = remoteMessage.getData().get("_time_awal");
            _time_ahir = remoteMessage.getData().get("_time_ahir");
        }

        displayNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());
    }
    private void displayNotification(String msgTitle,String msgBody) {
        Intent intent = new Intent(this, OrderConfirmationActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("AnotherActivity", "True");
        intent.putExtra("_ID", _ID);
        intent.putExtra("_NAME", _NAME);
        intent.putExtra("_ID_TRANSAKSI", _ID_TRANSAKSI);
        intent.putExtra("_TOTAL_HARGA", _TOTAL_HARGA);
        intent.putExtra("_TITLE", _TITLE);
        intent.putExtra("_hari_or_jam", _hari_or_jam);
        if (_hari_or_jam.equals("true")){
            intent.putExtra("_date_awal", _date_awal);
            intent.putExtra("_date_ahir", _date_ahir);
        }
        else if (_hari_or_jam.equals("false")){
            intent.putExtra("_date", _date);
            intent.putExtra("_time_awal", _time_awal);
            intent.putExtra("_time_ahir", _time_ahir);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_notify)
                .setContentTitle(msgTitle)
                .setContentText(msgBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int id = createID();
        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }

//    curl --header "Authorization: key=AIzaSyBX75fFT_2RvmG-WQh1GfckLICcWipqRc8" --header Content-Type:"application/json" https://fcm.googleapis.com/fcm/send  -d "{\"to\":\"dTB_j_HGEzQ:APA91bEMJZocCf2DZOLjxdWamC5JdYChWdua8jsBZhf-t1xYClCsnYsJIJMZHAhDbHKYKQaal6Xi85AtaiGRWN5ICABl82_OVpIM7LYQaKs3Ohs844JNDaJfKA1l9tQV_t3N4HHBXRgk\",\"notification\": {\"title\": \"Click Action Message\",\"text\": \"Sample message\",\"click_action\":\"OPEN_ACTIVITY_1\"},\"data\":{\"_hari_or_jam\":\"true\"}}"
}
