package com.greyjacob.parkingapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by hyakupoglu on 3-1-2018.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    public float tariff;

    @Override
    public void onReceive(Context context, Intent intent) {

//        Intent intentToRepeat = new Intent(context, Calculation.class);
//        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(context,0, intentToRepeat, Intent.FILL_IN_ACTION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setSmallIcon(R.drawable.ic_action_name);
        notification.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            notification.setShowWhen(true);
        }
        notification.setContentTitle("ParkReminder");
        notification.setContentText("End of parking period");
        notification.setAutoCancel(true);
        notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification.build());
    }
}
