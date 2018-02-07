package com.greyjacob.parkingapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class ParkInformation extends AppCompatActivity {
    String TAG = "ParkInformationTAG";
    private  Button buttonSave;
    private  Button buttonCalculate;
    public TextInputLayout nameInputLayout, periodInputLayout, tariffInputLayout;
    private EditText name, tariff, period;
    DatabaseHelper mDatabaseHelper;
    String selectedName, selectedPeriod, selectedTariff;
    String IntentController;
//    Context context = this;
    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_information);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCalculate = (Button) findViewById(R.id.buttonCalculate);
        mDatabaseHelper = new DatabaseHelper(this);

        nameInputLayout = (TextInputLayout) findViewById(R.id.name_input_layout_name);
        periodInputLayout = (TextInputLayout) findViewById(R.id.period_input_layout_name);
        tariffInputLayout = (TextInputLayout) findViewById(R.id.tariff_input_layout_name);

        name = (EditText) findViewById(R.id.et1);
        period = (EditText) findViewById(R.id.et4);
        tariff = (EditText) findViewById(R.id.et5);

        Intent receivedIntent = getIntent();
        IntentController = receivedIntent.getStringExtra("IntentController");
        Log.e(TAG, " IntentController: " + String.valueOf(IntentController));



        //Get All data came from LisDataActivity(Existing Park Data)
        //Assign all of them related TVs
        //IntenController I si i degil bu sorun degilmi
        if(TextUtils.isEmpty(IntentController)){    //Bunun calisip calismadigini bir kontrol et
            selectedName = receivedIntent.getStringExtra("name");
            Log.e(TAG, " IntentController: " + IntentController);
            name.setText(selectedName);
            IntentController="";
            selectedPeriod = receivedIntent.getStringExtra("period");
            period.setText(selectedPeriod);
            selectedTariff = receivedIntent.getStringExtra("tariff");
            tariff.setText(selectedTariff);
        }
        OnClickButtonListener();
    }

    // Adding Data to Database
    public void AddData(String name, String period, String tariff){
        boolean insertData = mDatabaseHelper.addData(name, period, tariff);
        if (insertData){
            toastMessage("Data Successfully Saved!");
        } else{
            toastMessage("Something went wrong!");
        }
    }
    private void toastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    public void  OnClickButtonListener() {

        buttonSave.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String name1 = name.getText().toString();
                        String period1 = period.getText().toString();
                        String tariff1 = tariff.getText().toString();
                        if (name1.length()!=0 && period1.length()!=0 && tariff1.length()!=0){
                            AddData(name1, period1, tariff1);
//                            clearTextView(name,period,tariff);
                        } else {
                            toastMessage("Please put data in the mandatory text fields");
                        }
                    }
                }
        );
        buttonCalculate.setOnClickListener(

                new View.OnClickListener(){
                    public void onClick(View v) {
                        TextView entryTime = (TextView)findViewById(R.id.et2);
                        String enT = entryTime.getText().toString();
                        if (TextUtils.isEmpty(enT)||TextUtils.isEmpty(tariff.getText().toString())||TextUtils.isEmpty(period.getText().toString())){
                            Toast.makeText(ParkInformation.this, "Please set time, minute and tariff.", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(ParkInformation.this, Calculation.class);
                            intent.putExtra("name", name.getText().toString()); //Bu gereksiz bence
                            intent.putExtra("time", Calendar.getInstance());
                            intent.putExtra("period", period.getText().toString());
                            intent.putExtra("tariff", tariff.getText().toString());
                            intent.putExtra("entry", enT);
                            startActivity(intent);

//                            Intent intent1 = new Intent(ParkInformation.this, AlarmNotificationReceiver.class);
//                            intent1.putExtra("tariff", tariff.getText().toString());
//                            sendBroadcast(intent1);


//                            startAlarm();
                        }
                    }
                }
        );
    }

//    private void startAlarm() {
//        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, Calculation.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,0,intent,0);
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtime());
//    }


    public void onEntrySet(View v){
        showTime(v);
    }

//    public void onExitSet(View v){
//        showTime(v);
//    }
/// Bunu KEsin anlayama calis iyice
    public void showTime (View v){
        int timePickerInput = v.getId();
        Log.e(TAG,String.valueOf(timePickerInput));
        Log.e(TAG,"You click the timePick edittext");
        DialogFragment newFragment = new SetTime();
        Bundle args = new Bundle();
        args.putInt("key", timePickerInput);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(),"TimePicker");
    }

//    public void clearTextView(TextView name, TextView period, TextView tariff){
//        name.setText("");
//        period.setText("");
//        tariff.setText("");
//    }
}
