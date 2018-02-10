package com.greyjacob.parkingapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Locale;

import static android.content.ContentValues.TAG;


/**
 * Created by hyakupoglu on 3-1-2018.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("broadcastNot", "Broadcastteyim suan: ");
        String action = intent.getAction();

        if(action != null && action.equals("DataAction")){
            Log.i("broadcastNot", "DataActiondayim: ");
            float tariff = intent.getFloatExtra("tariff",0);
            int periodCount = intent.getIntExtra("periodCount",0);
            SharedPreferences sharedPreferences = context.getSharedPreferences("Data",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("tariff",tariff);
            editor.putInt("periodCount",periodCount);
            editor.apply();
        }
        else {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setSmallIcon(R.drawable.ic_action_name);
            notification.setAutoCancel(true);
            notification.setWhen(System.currentTimeMillis());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                notification.setShowWhen(true);
            }
            notification.setContentTitle("ParkReminder");

            //Get Data sharedPref
            SharedPreferences sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
            int periodCount = sharedPreferences.getInt("periodCount", 0);
            float tariff = sharedPreferences.getFloat("tariff", 0);

            notification.setContentText("End of " + periodCount + ". " + "period" + ".Current Total: €" + String.format("%.2f",tariff*(periodCount+1)));
            notification.setAutoCancel(true);
            notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(1, notification.build());

            //Upload Data SharedPref
            periodCount++;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("periodCount", periodCount);
            editor.apply();
        }
    }
}
