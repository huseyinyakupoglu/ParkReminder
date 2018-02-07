package com.greyjacob.parkingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.R.attr.id;
import static android.os.Build.ID;

/**
 * Created by hyakupoglu on 27-6-2017.
 * Database yaratip Textlerdeki infoyu
 * Databasedeki tabloya aktariyoruz
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "parking_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "period";
    private static final String COL4 = "tariff";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null,3);
    }

    //TODO Database yaratiliyor bu sadece bir keremi yaratiliyo bunu TestET
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," +
                COL3 + " TEXT," +
                COL4 + " TEXT" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean addData(String name, String period, String tariff){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, period + " min");
        contentValues.put(COL4, tariff + " €/period");

        Log.d(TAG, "addData: Adding " + id + name + period + tariff + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //Burdaki amac ne
        return result != -1;
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public Cursor getRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL2 + ", " + COL3 + ", " + COL4 +
                " FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + id + "'";
        return db.rawQuery(query, null);
    }
    public void deleteRow(Integer id)
    {
        Log.e("ListPopulation ", "deletetitle Process" );
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("ListPopulation ", "Database alindi" );
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + "= '" + id + "'");
        Log.e("ListPopulation ", "Rowun silinmesi lazim" );
        db.close();
        Log.e("ListPopulation ", "Database kapandi" );
    }



}
