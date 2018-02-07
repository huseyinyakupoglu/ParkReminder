package com.greyjacob.parkingapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

/**
 * Created by hyakupoglu on 15-12-2017.
 */

public class CalculationService extends Service{

    private static String TAG= "CalculationService";
    Handler handler, myHandler;
    Runnable myRunnable, runnable;
    public Calendar currentTime;
    boolean isResume = false;
    int i=0;
    PowerManager.WakeLock wakeLock;
    @Override
    public void onCreate() {
        super.onCreate();
        currentTime = Calendar.getInstance();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started..", Toast.LENGTH_LONG).show();
        final int secondCurrent = currentTime.get(Calendar.SECOND);
        long syncFirst = currentTime.get(Calendar.MILLISECOND);
        int waitTime = 60 - secondCurrent;

        handler = new Handler();
        Log.d(TAG, "Service calisiyor");

        runnable = new Runnable() {
            @Override
            public void run() {
                i=i+1;
                Log.d(TAG, "Service icindeki ilk dongu geri sayiyor"+ String.valueOf(i));
                wakeLockService();
                sendBroadcast();
                dongu();
            }
        };
        handler.postDelayed(runnable, (waitTime*1000)-syncFirst);
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        if(runnable!=null){
            handler.removeCallbacks(runnable);
            Log.d(TAG, "runnable destroy edildi");
        }
        if(myRunnable!=null){
            myHandler.removeCallbacks(myRunnable);
            Log.d(TAG, "MyRunnable destroy edildi");
        }
        Log.d(TAG, "Service yok edildi");
        Toast.makeText(this, "Service Destroyed..", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void dongu() {
        Log.d(TAG, "HUHUHHUHU");
        long syncMiliSec = currentTime.get(Calendar.MILLISECOND);
        Log.d(TAG,"Hesaplanan salise: " + String.valueOf(syncMiliSec));
        myHandler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                i=i+1;
//                wakeLockService();
                sendBroadcast();
                Log.d(TAG, "Service icindeki "+Integer.toString(i)+"."+" dongu geri sayiyor"+String.valueOf(i));
                dongu();
            }
        };
        myHandler.postDelayed(myRunnable, (1000 * 60) - syncMiliSec);
    }

    public void sendBroadcast(){
        Intent intent = new Intent();
        intent.putExtra(Calculation.MSG, i);
//        intent.putExtra(Calculation.MSG, isResume);
        intent.setAction(Calculation.MYFILTER);
        sendBroadcast(intent);
        Log.d(TAG,"Service olarak Broadcast yolladim");
    }
    public void wakeLockService () {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire(7000);
    }

}