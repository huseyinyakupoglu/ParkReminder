package com.greyjacob.parkingapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

/**
 * Created by hyakupoglu on 3-1-2018.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    public float tariff;

    @Override
    public void onReceive(Context context, Intent intent) {



//        notification = parseInt(receivedIntent.getStringExtra("period"));


//        Intent intentToRepeat = new Intent(context, Calculation.class);
//        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(context,1, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setSmallIcon(R.drawable.ic_action_name);
        notification.setAutoCancel(true);
//        notification.setContentIntent(pendingIntent);
        notification.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            notification.setShowWhen(true);
        }
        notification.setContentTitle("ParkReminder");
        notification.setContentText("End of parking period");
        notification.setAutoCancel(true);
        notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1,notification.build());
//        if(intent!=null) {
//            String clearNot;
//            clearNot = intent.getStringExtra("clearNotification");
//            if(clearNot.equals("ok")){
//                notificationManager.cancel(1);
//            }
//        }
    }
}
