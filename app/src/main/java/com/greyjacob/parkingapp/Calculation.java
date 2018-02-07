package com.greyjacob.parkingapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import java.util.Calendar;
import static java.lang.Integer.parseInt;

public class Calculation extends AppCompatActivity {
    private static final String TAG = "CalculationTAG";
    private static final String DEBUG = "analizet";
    private static final String ALARM = "alarmyonet";
    public int period;
    public float tariff;
    public String receivedEntryTime;
    public int periodCount;
    ListCalculateAdapter listDataAdapter;
    ListView mListView;
    int finishTimeMinute;
    int parkingTimeInMinute;
    int remainTimeToPeriod;
    boolean isRemainTimeZero;
    Calendar creationTime;
    AlarmManager alarmManager;
    PendingIntent alarmIntent;
    long onResumeTime;
    int onResumeMinute, onStopMinute;
    boolean isFirstResume = true;
    int passedPeriod = 0;
    static boolean isActive = false;
    private static  final int uniqueID = 178265;
    NotificationManager nm;
    public static final String MYFILTER = "com.my.broadcast.RECEIVER";
    public static final String PREFS_NAME = "config";
    public static final String KEY_COUNT = "notificationCount";
    public static final String TARIFF_VALUE = "tariffValue";
    public static final String MSG = "_message";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if(bundle != null){
                int iValue = bundle.getInt(MSG);
                Log.d(TAG,  String.valueOf(iValue) + ". Broadcast alindi ");

                checkTimeChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_listview_layout);
        mListView = (ListView) findViewById(R.id.listView);
        periodCount = 1;
        Intent receivedIntent = getIntent();
        creationTime = (Calendar) receivedIntent.getSerializableExtra("time");
        period = parseInt(receivedIntent.getStringExtra("period")); //Bu sekilde aliniyor
        Log.e(TAG, "Periodun degeri: " + String.valueOf(period));
        tariff = Float.parseFloat(receivedIntent.getStringExtra("tariff"));
        receivedEntryTime = receivedIntent.getStringExtra("entry");
        int enterTimeHour = hourExtract(receivedEntryTime);
        int enterTimeMinute = minuteExtract(receivedEntryTime);
        parkingTimeInMinute = enterTimeHour*60 + enterTimeMinute;
        Log.d(TAG, "parkingTimeInMinute: " + parkingTimeInMinute);
        finishTimeMinute = parkingTimeInMinute + period;
        Log.d(TAG, "entrytimeMinute: "+ parkingTimeInMinute);


        //Header Row Layout
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.calculation_header_layout, mListView, false);
        mListView.addHeaderView(headerView);
        //Header**

        listDataAdapter = new ListCalculateAdapter(getApplicationContext(), R.layout.row_calculate);
        mListView.setAdapter(listDataAdapter);

        //eger suanki zaman finishTimedan buyukse

        Calendar currentTime = Calendar.getInstance();
        int hourCurrent = currentTime.get(Calendar.HOUR_OF_DAY);
        int minuteCurrent = currentTime.get(Calendar.MINUTE);
        double secondCurrent = currentTime.get(Calendar.SECOND);
        long syncFirst = currentTime.get(Calendar.MILLISECOND);
        Double waitTime = 60 - secondCurrent; //bunlara gerek yok service de hallediliyor
        int wait = waitTime.intValue(); //bunu yolla
        Log.d(TAG, "waitTime: " + wait);
        Log.d(TAG, "secondCurrent: " + secondCurrent);
        int currentTimeMinuteInteger = hourCurrent*60 +minuteCurrent;

        int passedTime = currentTimeMinuteInteger - parkingTimeInMinute;
        int finishedPeriods = passedTime / period;
        int remainTime = period - passedTime%period;
        Log.d(TAG, "remainTime: " + remainTime);

        //Populating already finished periods
        existedPeriods();

        String finishTime = timeStringFormat(parkingTimeInMinute,periodCount, period);
        float tariffPeriodic = tariff * periodCount;

        // liste nin devam eden periyodu ekrana basildi
        populateListView(String.valueOf(periodCount), finishTime, remainTime, String.format("%.2f", tariffPeriodic), creationTime, period);
        startAlarm();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
        Log.d(TAG, "ON Start: Aktif");
    }

    @Override
    protected void onResume(){
        onResumeTime = Calendar.getInstance().getTimeInMillis();
//        onResumeMinute = Calendar.getInstance().get(Calendar.MINUTE);
        onResumeMinute = timeInMinute();
        Log.d(DEBUG, "Resume in Minute: " + onResumeMinute);
        Log.d(TAG,"OnResume: Resume Zamani Milisaniye:" + onResumeTime);
        registerBroadcastListenerStartService();
        if (!isFirstResume){
            Log.d(DEBUG, "IsFirstResume False Hesapla!!");
            calculationTest();
        }
        super.onResume();
        Log.d(TAG, "ON Resume: Aktif");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "ON Pause: Aktif");
    }
    // Stop Service
    @Override
    protected void onStop(){
        remainTimeToPeriod = getRemainMinute();
//        onStopMinute = Calendar.getInstance().get(Calendar.MINUTE);
        onStopMinute = timeInMinute();
        Log.d(DEBUG,"OnStop Minute:" + onStopMinute);
        isFirstResume = false;
        stopServiceAndRegister();
        Log.d(TAG, "ON Stop: Aktif");
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.d(ALARM,"Alarm yokedildi");
//        removeSharedPreferences();
        cancelAlarm();
        super.onDestroy();
        isActive = false;
        if (nm!=null) {
            nm.cancel(uniqueID);
        }

        Log.d(TAG, "ON Destroy: Aktif");
    }

    private void checkTimeChanged() {
        if (!isActive){
            Log.d(TAG, "checkTimeChanged: Activity destroy olmus checkTimeChanged Calismamali: ");
            return;
        }
            DataProvider dataProvider = (DataProvider) listDataAdapter.getItem(periodCount-1); // i pozisyonundaki item i al
            assert dataProvider != null;
            int newTime = dataProvider.getRemainTime(); // remain time new time a set edildi
            newTime--; //new time 1 azaltildi
            Log.d(TAG, "newTime: " + newTime);
            dataProvider.setRemainTime(newTime); //new time remain time a set edildi
            //TODO Bunu set etmezsek rowun rengi degisicek mi? Bunu test et!
            listDataAdapter.notifyDataSetChanged(); //listedeki bu degisiklik yenilendi

            Calendar calendarMili = Calendar.getInstance();
            long syncMiliSec = calendarMili.get(Calendar.MILLISECOND);
            if (newTime==0){
            periodCount++;
            String finishTime = timeStringFormat(parkingTimeInMinute,periodCount, period);
            float tariffPeriodic = tariff * periodCount;
            //newtime i perioda set ettik ve yeni row u list view a ekledik
            populateListView(String.valueOf(periodCount), finishTime, period, String.format("%.2f", tariffPeriodic), creationTime, period);
//            notificationEndPark();
        }
    }

    public void populateListView(String number, String finish, Integer remain, String tariff, Calendar creationTime, Integer period) {
        DataProvider dataProvider = new DataProvider(number, finish, remain, tariff, creationTime, period);
        listDataAdapter.add(dataProvider);
        isRemainTimeZero = false;
    }
    public String timeStringFormat (int entryTimeMinute, int periodCount, int period) {
        int periodFinishTimeTotal = entryTimeMinute + periodCount*period;
        int periodFinishTimeHour = periodFinishTimeTotal/60;
        if(periodFinishTimeHour==24){
            periodFinishTimeHour=0;
        }
        int periodFinishTimeMinute = periodFinishTimeTotal%60;
        String finishTime;
        finishTime= zeroAdd(periodFinishTimeHour) + ":" + zeroAdd(periodFinishTimeMinute);
        return finishTime;
    }
    public String zeroAdd(int i){
        String string = String.valueOf(i);
        if (i<10){
            string =  "0"+ string;
        }
        return string;
    }
    public Integer hourExtract(String string) {
        int startIndex = string.indexOf(":");
        return parseInt(string.substring(0, startIndex));
    }
    public Integer minuteExtract(String string) {
        int startIndex = string.indexOf(":");
        return parseInt(string.substring(startIndex + 1, startIndex + 3));
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Park Timer")
                .setMessage("Are you sure you want to close the list?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    //TODO burayi duzelmem gerekiyor First Priority
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Clear Keep Screen Flag OFF
//                        removeSharedPreferences();
                        cancelAlarm();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        Calculation.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    // Stop Broadcast Receiver
    public void stopServiceAndRegister(){
        Intent myIntent = new Intent(this, CalculationService.class);
        if(broadcastReceiver!=null) {
            this.unregisterReceiver(broadcastReceiver);
        }
        this.stopService(myIntent);
    }

    //Register receiver
    public void registerBroadcastListenerStartService() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MYFILTER);
        this.registerReceiver(broadcastReceiver, intentFilter);
        Intent myIntent = new Intent(Calculation.this, CalculationService.class);
        startService(myIntent);
    }
    //Calculate the time between onResume and onStop
    public int timeDifference(){
        Log.d(DEBUG, "Time Difference: " + String.valueOf(onResumeMinute-onStopMinute));
        return onResumeMinute-onStopMinute;
    }

    //Get Remain time from listDataAdapter
    public int getRemainMinute(){
        DataProvider dataProvider = (DataProvider) listDataAdapter.getItem(periodCount-1); // i pozisyonundaki item i al
        Log.d(DEBUG,"Kalan zaman dakika olarak: " +dataProvider.getRemainTime());
        assert dataProvider != null;
        return dataProvider.getRemainTime();
//        return (dataProvider.getRemainTime()*60*1000)-(onStopSecond*1000+onStopSalise); // remain time new time a set edildi
    }

    //Calculate how many period is passed
    //TODO Buyuk ama ne kadar buyuk bir periyod kadar buyuk degilse ne olcak bunu coz
    public void calculationTest(){
        if (timeDifference()>remainTimeToPeriod){
            Log.d(DEBUG, "Passed Time is greater than Remain Period of Time");

            //Current Periyod become finished
            DataProvider dataProvider = (DataProvider) listDataAdapter.getItem(periodCount-1); // i pozisyonundaki item i al
            assert dataProvider != null;
            dataProvider.setRemainTime(0); //new time remain time a set edildi
            //TODO Bunu set etmezsek rowun rengi degisicek mi? Bunu test et!
            listDataAdapter.notifyDataSetChanged(); //listedeki bu degisiklik yenilendi

            int diffWithoutRemain = timeDifference()-remainTimeToPeriod;
            Log.d(DEBUG,"Diff without Remain: " + diffWithoutRemain);
            int passedPeriod = diffWithoutRemain/period;
            Log.d(DEBUG,"passed Period: " + passedPeriod);
            int passedTimeFromPeriod = diffWithoutRemain%period;
            Log.d(DEBUG,"remainTimeFromPeriod: " + passedTimeFromPeriod);
            int actualRemainTime = period - passedTimeFromPeriod;
            Log.d(DEBUG,"actualRemainTime: " + actualRemainTime);

            if(diffWithoutRemain > period){
                Log.d(DEBUG,"diffWithoutRemain > period");
                    for(int i = 1; i<=passedPeriod; i++){
                        periodCount++;
                        Log.d(DEBUG,"PassedPeriyodlar basiliyor");
                        String finishTime = timeStringFormat(parkingTimeInMinute, periodCount, period);
                        float tariffPeriodic = tariff * periodCount;
                        //Remain Time i 0 yaptik yani finished olcak
                        populateListView(String.valueOf(periodCount), finishTime, 0, String.format("%.2f", tariffPeriodic), creationTime, period);
                    }
                    periodCount++;
                    Log.d(DEBUG,"actualRemainTime basiliyor");
                    String finishTime = timeStringFormat(parkingTimeInMinute, periodCount, period);
                    float tariffPeriodic = tariff * periodCount;
                    //Remain Time actual remain time olcak
                    populateListView(String.valueOf(periodCount), finishTime, actualRemainTime, String.format("%.2f", tariffPeriodic), creationTime, period);
            }
            else{
                periodCount++;
                Log.d(DEBUG,"diffWithoutRemain < period");
                String finishTime = timeStringFormat(parkingTimeInMinute, periodCount, period);
                float tariffPeriodic = tariff * periodCount;
                //Remain Time actual remain time olcak
                populateListView(String.valueOf(periodCount), finishTime, actualRemainTime, String.format("%.2f", tariffPeriodic), creationTime, period);
            }
         }
        else{
//            if ((onResumeMinute-onStopMinute)>0){
                Log.d(DEBUG,"Herhangi bir Periyod gecilmedi");
                int passedMinute = onResumeMinute-onStopMinute;
                Log.d(TAG, "Gecen Periyod: " + passedMinute );
                for(int i = 1; i<=passedMinute;i++){
                    checkTimeChanged();
                }
            }
//        }
    }

    //Create Existing List
    public void existedPeriods(){
        Calendar currentTime = Calendar.getInstance();
        int hourCurrent = currentTime.get(Calendar.HOUR_OF_DAY);
        int minuteCurrent = currentTime.get(Calendar.MINUTE);
        int currentTimeMinuteInteger = hourCurrent*60 +minuteCurrent;
        int passedTime = currentTimeMinuteInteger - parkingTimeInMinute;
        int finishedPeriods = passedTime / period;
        if (finishedPeriods>0){
            for(int i=1; i<=finishedPeriods;i++){
                String finishTime = timeStringFormat(parkingTimeInMinute,periodCount, period);
                float tariffPeriodic = tariff * periodCount;
                populateListView(String.valueOf(periodCount), finishTime, 0, String.format("%.2f", tariffPeriodic), creationTime, period);
                periodCount++;
                Log.d(TAG, "period: " + periodCount);
            }
        }
    }

    private void startAlarm() {
        Calendar calendar = Calendar.getInstance();
        int remainMinPeriod= getRemainMinute();
        Log.d(ALARM, "current dakka: " + remainMinPeriod);
        int remainSecondPeriod= calendar.get(Calendar.SECOND);
        Log.d(ALARM, "current saniye: " + remainSecondPeriod);
        int remainMiliSecPeriod= calendar.get(Calendar.MILLISECOND);
        int waitTime = (remainMinPeriod*60*1000) - (remainSecondPeriod*1000 + remainMiliSecPeriod);
        Log.d(ALARM, "wait time: " + waitTime);


//        SharedPreferences values = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        SharedPreferences.Editor editor = values.edit();
//        editor.putInt(KEY_COUNT, periodCount);
//        editor.putFloat(TARIFF_VALUE, tariff);
//        editor.apply();

        Intent intentHuseyin = new Intent(this, AlarmNotificationReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this,0,intentHuseyin,0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+waitTime,period*60*1000, alarmIntent);
        Log.d(ALARM, "alarm set edildi");

    }


    public void cancelAlarm(){
       alarmManager.cancel(alarmIntent);
    }

//    public void removeSharedPreferences(){
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();
//        int currentCount = preferences.getInt(KEY_COUNT, 0);  //Sets to zero if not in prefs yet
//        Log.d(ALARM, "Prefdeki Count degeri: " + currentCount);
//    }


    public int timeInMinute (){
        Calendar calendar = Calendar.getInstance();
        int hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteCurrent = calendar.get(Calendar.MINUTE);
        return (hourCurrent*60)+minuteCurrent;
    }
}