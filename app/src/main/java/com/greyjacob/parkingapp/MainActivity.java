package com.greyjacob.parkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static Button buttonNew;
    private static Button buttonExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnClickButtonListener();
    }

    public void OnClickButtonListener() {
        buttonNew = (Button)findViewById(R.id.btnew);
        buttonExist = (Button)findViewById(R.id.btexist);

        buttonExist.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                        startActivity(intent);

                    }
                }
        );

        buttonNew.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.greyjacob.parkingapp.ParkInformation");
                        startActivity(intent);
                    }
                }
        );
    }
}
