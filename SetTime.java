package com.greyjacob.parkingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static com.greyjacob.parkingapp.R.id.et2;
//import static com.greyjacob.parkingapp.R.id.et3;


/**
 * Created by hyakupoglu on 6-7-2017.
 */

public class SetTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    String TAG="SetTime";
    Context context= getActivity();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Integer title = getArguments().getInt("key");
        Log.e(TAG, String.valueOf(title));
        //Get the AM or PM for current time
        String aMpM = "AM";

        int setTime = hourOfDay * 60 + minute;
        Calendar c2 = Calendar.getInstance();
        int hourCurrent = c2.get(Calendar.HOUR_OF_DAY);
        int minuteCurrent = c2.get(Calendar.MINUTE);

        if (setTime > hourCurrent * 60 + minuteCurrent) {
//            Toast.makeText(getAppi, "Start time cannot be later than now", Toast.LENGTH_SHORT).show();
        } else {

            if (hourOfDay > 11) {
                aMpM = "PM";
            }

//            if (title == 2131427432) {
//                TextView tv = (TextView) getActivity().findViewById(et3);
//                //Display the user changed time on TextView
//                tv.setText(zeroAdd(hourOfDay) + ":" + zeroAdd(minute) + aMpM + "\n");
//
//            } else {
                TextView tv = (TextView) getActivity().findViewById(et2);
                //Display the user changed time on TextView
                tv.setText(zeroAdd(hourOfDay) + ":" + zeroAdd(minute) + aMpM);
//            }
        }
    }
    public String zeroAdd(int i){
        String string = String.valueOf(i);
        if (i<10){
           string =  "0"+ string;
        }
        return string;
    }

}

