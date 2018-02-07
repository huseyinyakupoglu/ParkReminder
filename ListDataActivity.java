package com.greyjacob.parkingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by hyakupoglu on 28-6-2017.
 * Sqlite deki datalari List View a aktariyoruz
 */

public class ListDataActivity extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHelper;
    ListView mListView;
    Cursor cursor;
    ListDataAdapter listDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        //Header
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.header_list, mListView,false);
        mListView.addHeaderView(headerView);
        //Header**

        populateListView();
        listViewItemClick(); // Bu ustteydi programa zarar vermediyse bu commenti sil
    }
    public void populateListView() {
        //cursor ile data base i cagirdim
        cursor = mDatabaseHelper.getData();
        //ListViewAdapter yarattik. row layoutu Listdata adaptera ekledik
        listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.row_layout);
        mListView.setAdapter(listDataAdapter);
        //Bir sonraki satira inebiliyormu
        if (cursor.moveToNext()) {
            do {
                //cursor ile sql databaseindeki satirlardaki degerleri aliyorum
                String name, period, tariff;
                name = cursor.getString(1);
                period = cursor.getString(2);
                tariff = cursor.getString(3);
                //Cursor ile aldigim degerlerle data providerda nesne yaratiyorum
                DataProvider dataProvider = new DataProvider(name, period, tariff);
                //nesneyi adaptera ekliyorum
                listDataAdapter.add(dataProvider);

            }while(cursor.moveToNext());
        }
        cursor.close(); //Bununla calisiyormu
    }

    /**
     * ListView a tiklandiginda Dialog acilcak
     * Dialogun icinde DELETE ve LOAD Buttonlari olacak
     *
     */
    private void listViewItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //Get Row ID from Selected Item
                cursor = mDatabaseHelper.getData();
                cursor.moveToPosition(position-1);
                final Integer id = cursor.getInt(cursor.getColumnIndex("ID"));
                Log.e(TAG, position + " position/ " + l + " long/ " + "Selected Row ID:" + id);
                cursor.close();
                //Dialog Creation
                AlertDialog.Builder builder = new AlertDialog.Builder (ListDataActivity.this);
                builder.setTitle("Options");
                //Load Button
                builder.setPositiveButton("Load", new  DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Data will be send to Information Activity
                        cursor = mDatabaseHelper.getRow(id);
                        Intent loadintent = new Intent(ListDataActivity.this, ParkInformation.class);

                        cursor.moveToFirst();
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        Log.e(TAG,"Cursordan alinan name: " + name);
                        loadintent.putExtra("name", name);
                        int intentCont = 1;
                        loadintent.putExtra("intentController", intentCont);

                        String period = cursor.getString(cursor.getColumnIndex("period"));
                        int iend = period.indexOf(" ");
                        loadintent.putExtra("period", period.substring(0, iend));

                        String tariff = cursor.getString(cursor.getColumnIndex("tariff"));
                        int iend2 = tariff.indexOf(" ");
                        loadintent.putExtra("tariff", tariff.substring(0, iend2));
                        startActivity(loadintent);
                        cursor.close();
                    }
                });
                //Delete Button
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete from database bu ID
                        mDatabaseHelper.deleteRow(id);
                        populateListView();
                    }
                });
                //Edit Button

                //Dialog Added
                builder.create();
                builder.show();

            }
        });
    }
    //ListView a tiklandiginda Dialog acilcak bitti

    private void toastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



}
