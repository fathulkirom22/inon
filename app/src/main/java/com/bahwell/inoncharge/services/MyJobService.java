package com.bahwell.inoncharge.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.OrderConfirmationActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bahwell on 02/08/17.
 */

public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
//        displayNotification(job.getExtras().getString("body"),job.getExtras().getString("title"));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

//    private void displayNotification(String msgTitle,String msgBody) {
//        Intent intent = new Intent(this, OrderConfirmationActivity.class);
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.putExtra("AnotherActivity", "True");
//        intent.putExtra("_ID", _ID);
//        intent.putExtra("_NAME", _NAME);
//        intent.putExtra("_TOTAL_HARGA", _TOTAL_HARGA);
//        intent.putExtra("_TITLE", _TITLE);
//        intent.putExtra("_hari_or_jam", _hari_or_jam);
//        if (_hari_or_jam.equals("true")){
//            intent.putExtra("_date_awal", _date_awal);
//            intent.putExtra("_date_ahir", _date_ahir);
//        }
//        else if (_hari_or_jam.equals("false")){
//            intent.putExtra("_date", _date);
//            intent.putExtra("_time_awal", _time_awal);
//            intent.putExtra("_time_ahir", _time_ahir);
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_notify)
//                .setContentTitle(msgTitle)
//                .setContentText(msgBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setPriority(Notification.PRIORITY_HIGH);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int id = createID();
//        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
//    }
//
//    public int createID(){
//        Date now = new Date();
//        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
//        return id;
//    }
}
