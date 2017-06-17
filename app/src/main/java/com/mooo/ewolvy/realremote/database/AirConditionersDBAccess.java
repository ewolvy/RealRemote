package com.mooo.ewolvy.realremote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mooo.ewolvy.realremote.aalist.AAItem;
import com.mooo.ewolvy.realremote.aaremotes.AAKaysun;
import com.mooo.ewolvy.realremote.aaremotes.AAProKlima;
import com.mooo.ewolvy.realremote.aaremotes.AASuper;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;

import java.util.ArrayList;
import java.util.List;

public class AirConditionersDBAccess {

    private AirConditionersDBAccess(){} //Empty private constructor to avoid instantiation

    // Helper intern method to get data from the database
    private static Cursor getDatabaseData(AirConditionersDBHelper dbHelper, String[] projection) {
        String orderBy = AvailableAA.COLUMN_NAME_POSITION;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.query(
                AvailableAA.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                orderBy
        );
    }

    // Get all rows of the database, but only the needed columns for the AAItem list
    public static List<AAItem> getAAItems (Context context){
        List<AAItem> data = new ArrayList<>();

        String[] projection = {
                AvailableAA._ID,
                AvailableAA.COLUMN_NAME_NAME,
                AvailableAA.COLUMN_NAME_BRAND,
                AvailableAA.COLUMN_NAME_SERVER,
                AvailableAA.COLUMN_NAME_PORT,
                AvailableAA.COLUMN_NAME_USERNAME,
                AvailableAA.COLUMN_NAME_PASSWORD,
                AvailableAA.COLUMN_NAME_CERTIFICATE,
                AvailableAA.COLUMN_NAME_ALIAS,
        };

        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        Cursor dbData = getDatabaseData(dbHelper, projection);

        while (dbData.moveToNext()){
            AAItem item = new AAItem();
            item.set_id(dbData.getInt(dbData.getColumnIndex(AvailableAA._ID)));
            item.setName(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_NAME)));
            item.setBrand(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_BRAND)));
            item.setServer(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_SERVER)));
            item.setPort(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_PORT)));
            item.setUsername(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_USERNAME)));
            item.setPassword(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_PASSWORD)));
            item.setCertificate(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_CERTIFICATE)));
            item.setAlias(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_ALIAS)));
            data.add(item);
        }

        dbData.close();

        return data;
    }

    // Add a new AA with standard temperature, mode, fan and on state
    public static long addItem (AAItem item, Context context){
        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(AvailableAA.COLUMN_NAME_NAME, item.getName());
        values.put(AvailableAA.COLUMN_NAME_BRAND, item.getBrand());
        values.put(AvailableAA.COLUMN_NAME_SERVER, item.getServer());
        values.put(AvailableAA.COLUMN_NAME_PORT, item.getPort());
        values.put(AvailableAA.COLUMN_NAME_USERNAME, item.getUsername());
        values.put(AvailableAA.COLUMN_NAME_PASSWORD, item.getPassword());
        values.put(AvailableAA.COLUMN_NAME_CERTIFICATE, item.getCertificate());
        values.put(AvailableAA.COLUMN_NAME_ALIAS, item.getAlias());
        values.put(AvailableAA.COLUMN_NAME_TEMP, item.getTemperature());
        values.put(AvailableAA.COLUMN_NAME_MODE, item.getMode());
        values.put(AvailableAA.COLUMN_NAME_FAN, item.getFan());
        values.put(AvailableAA.COLUMN_NAME_IS_ON, (item.getIs_on()) ? 1 : 0);
        values.put(AvailableAA.COLUMN_NAME_POSITION, item.getPosition());

        long id = db.insert(AvailableAA.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // Get all rows of the database, but only the needed columns for the AASuper list
    public static List<AASuper> getAASuper (Context context){
        List<AASuper> data = new ArrayList<>();

        String[] projection = {
                AvailableAA._ID,
                AvailableAA.COLUMN_NAME_NAME,
                AvailableAA.COLUMN_NAME_BRAND,
                AvailableAA.COLUMN_NAME_TEMP,
                AvailableAA.COLUMN_NAME_MODE,
                AvailableAA.COLUMN_NAME_FAN,
                AvailableAA.COLUMN_NAME_IS_ON,
                AvailableAA.COLUMN_NAME_POSITION
        };

        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        Cursor dbData = getDatabaseData(dbHelper, projection);

        while (dbData.moveToNext()){
            AASuper item;
            switch (dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_BRAND))) {
                case AirConditionersContract.AA_KAYSUN:
                    item = new AAKaysun(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_MODE)),
                            dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_FAN)),
                            dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_TEMP)),
                            dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_IS_ON)) == 1,
                            dbData.getInt(dbData.getColumnIndex(AvailableAA._ID)),
                            dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_NAME)));
                    data.add (item);
                    break;
                case AirConditionersContract.AA_PROKLIMA:
                    item = new AAProKlima(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_MODE)),
                            dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_FAN)),
                            dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_TEMP)),
                            dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_IS_ON)) == 1,
                            dbData.getInt(dbData.getColumnIndex(AvailableAA._ID)),
                            dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_NAME)));
                    data.add (item);
                    break;
            }
        }

        dbData.close();

        return data;
    }

    // Modify values of AAitem (not for position)
    public static void modifyAAItem (AAItem item, Context context){
        ContentValues newValues = new ContentValues();
        newValues.put(AvailableAA.COLUMN_NAME_NAME, item.getName());
        newValues.put(AvailableAA.COLUMN_NAME_BRAND, item.getBrand());
        newValues.put(AvailableAA.COLUMN_NAME_SERVER, item.getServer());
        newValues.put(AvailableAA.COLUMN_NAME_PORT, item.getPort());
        newValues.put(AvailableAA.COLUMN_NAME_USERNAME, item.getUsername());
        newValues.put(AvailableAA.COLUMN_NAME_PASSWORD, item.getPassword());
        newValues.put(AvailableAA.COLUMN_NAME_CERTIFICATE, item.getCertificate());
        newValues.put(AvailableAA.COLUMN_NAME_ALIAS, item.getAlias());

        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.update(AvailableAA.TABLE_NAME, newValues, "_id=" + item.get_id(), null);
        db.close();
    }

    // Modify position of a single item
    public static void modifyItemPosition (AAItem item, int newPosition, Context context){
        ContentValues values = new ContentValues();
        values.put (AvailableAA.COLUMN_NAME_POSITION, newPosition);

        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.update(AvailableAA.TABLE_NAME, values, "_id=" + item.get_id(), null);
        db.close();
    }

    public static void deleteAAItem (int id, Context context){
        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Before deleting the required item, we must change the position of all under it
        // Using SQL: UPDATE table SET position = position - 1 WHERE position < id
        String modifyPositionSQL = "UPDATE " + AvailableAA.TABLE_NAME;
        modifyPositionSQL = modifyPositionSQL + " SET " + AvailableAA.COLUMN_NAME_POSITION;
        modifyPositionSQL = modifyPositionSQL + " = " + AvailableAA.COLUMN_NAME_POSITION + " -1 ";
        modifyPositionSQL = modifyPositionSQL + " WHERE " + AvailableAA.COLUMN_NAME_POSITION;
        modifyPositionSQL = modifyPositionSQL + " > " + id;
        db.execSQL(modifyPositionSQL);

        String deleteWhereClause = "_id=?";
        String[] deleteWhereArgs = new String[] { String.valueOf(id) };

        db.delete(AvailableAA.TABLE_NAME, deleteWhereClause, deleteWhereArgs);
        db.close();
        Log.v("DBACCESS DELETEAAITEM", "Deleted item: " + id);
    }

    // Modify values of AASuper
    public static void modifyAASuper (int id, AASuper item, Context context){
        ContentValues newValues = new ContentValues();
        newValues.put(AvailableAA.COLUMN_NAME_TEMP, item.getCurrentTemp());
        newValues.put(AvailableAA.COLUMN_NAME_MODE, item.getMode());
        newValues.put(AvailableAA.COLUMN_NAME_FAN, item.getFan());
        newValues.put(AvailableAA.COLUMN_NAME_IS_ON, item.getIsOn());

        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.update(AvailableAA.TABLE_NAME, newValues, "_id=" + id, null);
        db.close();
    }
}