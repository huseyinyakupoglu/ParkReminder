package com.greyjacob.parkingapp;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by hyakupoglu on 3-1-2018.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    private static final String ALARM = "broadcastteneoluyor";
    public static final String PREFS_NAME = "config";
    public static final String KEY_COUNT = "notificationCount";
    public static final String TARIFF_VALUE = "tariffValue";
    public int currentCount;
    public float tariff;

    @Override
    public void onReceive(Context context, Intent intent) {
//
//        ListCalculateAdapter  listDataAdapter = new ListCalculateAdapter(context, R.layout.row_calculate);
//        int i = listDataAdapter.getCount()-1;
//        DataProvider dataProvider = (DataProvider) listDataAdapter.getItem(i);
//        String period = dataProvider.getPeriod();
//        String tariff = dataProvider.getTariff();

//        Intent intentToRepeat = new Intent(context, Calculation.class);
//        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(context,0, intentToRepeat, Intent.FILL_IN_ACTION);

//        SharedPreferences values = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        currentCount = values.getInt(KEY_COUNT, 0);  //Sets to zero if not in prefs yet
//        currentCount++;
//        SharedPreferences.Editor editor = values.edit();
//        editor.putInt(KEY_COUNT, currentCount);
//        editor.apply();
//        tariff = values.getFloat(TARIFF_VALUE, 0);



        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setSmallIcon(R.drawable.ic_action_name);
        notification.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            notification.setShowWhen(true);
        }
        notification.setContentTitle("ParkReminder");
        notification.setContentText("End of parking period");
        notification.setContentText("You are in "+ "period"+". parking period, " + "Total Cost:"+" €"+tariff);
        notification.setAutoCancel(true);
        notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification.build());

        //Periyodun degerini +1 arttirdimswwwqwqeeeeeeeeeeee
    }
}
