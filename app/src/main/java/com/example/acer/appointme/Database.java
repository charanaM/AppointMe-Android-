package com.example.acer.appointme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Charana Mayakaduwa
 * 2016139
 * w1626663
 *
 * Class for the database
 *  stores and retrive data from database
 */

public class Database extends SQLiteOpenHelper {

    public static final String TAG = "Database";

    public static final String DATABASE_NAME = "appointmentTabel.db";
    public static final String TABLE_NAME = "ap_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "title";
    public static final String COL3 = "time";
    public static final String COL4 = "details";
    public static final String COL5 = "date";


    public Database(Context context, String name, SQLiteDatabase.CursorFactory CFactory, int version){
        super(context, DATABASE_NAME, CFactory, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTable = " CREATE TABLE " + TABLE_NAME + "(" + COL1+ " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COL2 + " TEXT ,"  + COL3 + " DATETIME, " + COL4 + "  TEXT ," + COL5 + " TEXT );";
        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS  " + TABLE_NAME );
        onCreate(sqLiteDatabase);

    }

    public int addAppointmentData(Data data){
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlQuery = " SELECT * FROM " + TABLE_NAME + " WHERE "
                + COL5 + "=\'" + data.getDate() + "\'" + " AND " + COL2
                + "=\'" + data.getTitle() + "\';";

        //using raw query to store columns
        //execute the query and store the data in the cursor
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if (cursor == null || !cursor.moveToFirst()){

            ContentValues contentValues = new ContentValues();


            contentValues.put(COL2, data.getTitle());
            contentValues.put(COL3, data.getTime());
            contentValues.put(COL4, data.getDetails());
            contentValues.put(COL5, data.getDate());

            db.insert(TABLE_NAME, null, contentValues);
            db.close();
            return 1;
        }else {
            return -1;
        }

    }


    public int updateAppointmentData(Data data , String time , String title , String details){

        SQLiteDatabase db = getWritableDatabase();

        String sqlQuery = " SELECT * FROM " + TABLE_NAME + " WHERE "
                + COL5 + "=\'" + data.getDate() + "\'" + " AND " +
                COL2 + "=\'" + data.getTitle() + "\';";

        Cursor cursor = db.rawQuery(sqlQuery,null);

        if (cursor == null || !cursor.moveToFirst()) {

            return -1;

        } else {

            ContentValues contentValues = new ContentValues();


            contentValues.put(COL3 , time);
            contentValues.put(COL2 , title );
            contentValues.put(COL4 , details);


            //insert the values into the database
            db.update(TABLE_NAME, contentValues , COL5 + "='" + data.getDate() + "'" + " AND " +
                    COL2 + "='" + data.getTitle() + "'" , null);
            db.close(); //restores the memory
            cursor.close();
            return 1;

        }
    }

    public int moveAppointmentData(Data data , String date ){

        SQLiteDatabase db = getWritableDatabase();

        String sqlQuery = " SELECT * FROM " + TABLE_NAME + " WHERE "
                + COL5 + "=\'" + data.getDate() + "\'" + " AND " +
                COL2 + "=\'" + data.getTitle() + "\';";

        Cursor cursor = db.rawQuery(sqlQuery,null);

        if (cursor == null || !cursor.moveToFirst()) {

            return -1;

        } else {

            ContentValues contentValues = new ContentValues();

            //stores the values to be updated
            contentValues.put(COL5 , date);

            //insert the values into the database
            db.update(TABLE_NAME, contentValues , COL5 + "='" + data.getDate() + "'" + " AND " +
                    COL2 + "='" + data.getTitle() + "'" , null);
            db.close(); //restores the memory
            cursor.close();
            return 1;

        }
    }

    public void deleteAppointmentData(String date){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL5 + "=\'" + date + "\';");
        db.close();
    }


    public void deleteAppointmentData(String date , String title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL5 + "=\'" + date + "\'"
                + " AND " + COL2 + "=\'" + title + "\';");
        db.close();
    }


    public String DBToString(){

        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1 "; // 1 means every condition is met


        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {

            if (cursor.getString(cursor.getColumnIndex("title")) != null) {
                dbString += cursor.getString(cursor.getColumnIndex("date"));
                dbString += "~";
                dbString += cursor.getString(cursor.getColumnIndex("time"));
                dbString += "~";
                dbString += cursor.getString(cursor.getColumnIndex("title"));
                dbString += "~";
                dbString += cursor.getString(cursor.getColumnIndex("details"));
                dbString += "\n";
            }
            cursor.moveToNext();
        }
        db.close();
        return dbString;
    }


    /**
     * Goes through the database and returns the result for a single day
     *
     * @return
     */
    public List<Data> displayAppointmentData(String date){

        List<Data> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL5 + "=\'" + date + "\'"
                + " ORDER BY " + COL3 + " ASC";


        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {

            if (cursor.getString(cursor.getColumnIndex("title")) != null) {

                Data data = new Data(cursor.getString(cursor.getColumnIndex("date")) ,
                        cursor.getString(cursor.getColumnIndex("time")) ,
                        cursor.getString(cursor.getColumnIndex("title")) ,
                        cursor.getString(cursor.getColumnIndex("details")) );
                list.add(data);
            }
            cursor.moveToNext();
        }
        db.close();
        return list;
    }


    public List<Data> displayAllData(){

        List<Data> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +";";


        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {

            if (cursor.getString(cursor.getColumnIndex("title")) != null) {

                Data appointment = new Data(cursor.getString(cursor.getColumnIndex("date")) ,
                        cursor.getString(cursor.getColumnIndex("time")) ,
                        cursor.getString(cursor.getColumnIndex("title")) ,
                        cursor.getString(cursor.getColumnIndex("details")) );
                list.add(appointment);
            }
            cursor.moveToNext();
        }
        db.close();
        return list;
    }

    public void clearTable(String TABLE_NAME) {

        SQLiteDatabase db = getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);

    }
}



//
//
//    public Cursor getData(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NAME;
//        Cursor data = db.rawQuery(query, null);
//        return  data;
//    }
//
//    public Cursor getItemID(String name){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT "+ COL1 + " FROM " + TABLE_NAME + " WHERE "+ COL2 +" = '" + name + "' ";
//
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }
//
////    public void updateData(int id, String newName, String oldName){
////        SQLiteDatabase db = this.getWritableDatabase();
////
////
////    }
//
//    public void editData(int id, String title, String time, String details){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + "= '"+ title + "', "+
//                 COL3 + "= '"+ time + "', " +
//                 COL4 + "= '"+ details + "' WHERE " + COL1 + "= " + id;
//
//    }




