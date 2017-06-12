package com.mooo.ewolvy.realremote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;

public class AirConditionersDBHelper extends SQLiteOpenHelper{

    // Constants for database name and version. When upgrading the contract, version must be updated
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "AirConditioners.db";

    // String constant with SQL sentence for create table on first access
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            AvailableAA.TABLE_NAME + " (" +
            AvailableAA._ID + " INTEGER PRIMARY KEY," +
            AvailableAA.COLUMN_NAME_NAME + " TEXT," +
            AvailableAA.COLUMN_NAME_BRAND + " INTEGER," +
            AvailableAA.COLUMN_NAME_SERVER + " TEXT," +
            AvailableAA.COLUMN_NAME_PORT + " INTEGER," +
            AvailableAA.COLUMN_NAME_USERNAME + " TEXT," +
            AvailableAA.COLUMN_NAME_PASSWORD + " TEXT," +
            AvailableAA.COLUMN_NAME_CERTIFICATE + " TEXT," +
            AvailableAA.COLUMN_NAME_ALIAS + " TEXT," +
            AvailableAA.COLUMN_NAME_TEMP + " INTEGER," +
            AvailableAA.COLUMN_NAME_MODE + " INTEGER," +
            AvailableAA.COLUMN_NAME_FAN + " INTEGER," +
            AvailableAA.COLUMN_NAME_IS_ON + " INTEGER)";


    // Required constructor calling super
    public AirConditionersDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // On database creation just execute the SQL sentence to create it
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    // On database version update check if need to do something
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // We added columns Temp, Mode, Fan and Is_On. We must upgrade existing databases

        String ALTER_TABLE = "ALTER TABLE " + AvailableAA.TABLE_NAME + " ADD COLUMN ";
        //Check if really needs to be update
        if (newVersion == 2 && oldVersion == 1){
            sqLiteDatabase.execSQL(ALTER_TABLE + AvailableAA.COLUMN_NAME_TEMP + "INTEGER DEFAULT 27");
            sqLiteDatabase.execSQL(ALTER_TABLE + AvailableAA.COLUMN_NAME_MODE + "INTEGER DEFAULT 0");
            sqLiteDatabase.execSQL(ALTER_TABLE + AvailableAA.COLUMN_NAME_FAN + "INTEGER DEFAULT 0");
            sqLiteDatabase.execSQL(ALTER_TABLE + AvailableAA.COLUMN_NAME_IS_ON + "INTEGER DEFAULT 0");
        }
    }
}
