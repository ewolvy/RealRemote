package com.mooo.ewolvy.realremote.aalist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;
import com.mooo.ewolvy.realremote.database.AirConditionersDBHelper;
import java.util.ArrayList;
import java.util.List;

public class AAData {
    public static List<AAItem> getListData(Context context){
        List<AAItem> data = new ArrayList<>();

        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        Cursor dbData = getDatabaseData(dbHelper);

        while (dbData.moveToNext()){
            AAItem item = new AAItem();
            item.setName(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_NAME)));
            item.setBrand(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_BRAND)));
            item.setServer(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_SERVER)));
            item.setPort(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_PORT)));
            item.setUsername(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_USERNAME)));
            item.setPassword(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_PASSWORD)));
            item.setCertificate(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_CERTIFICATE)));
            item.setAlias(dbData.getString(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_ALIAS)));
            item.setTemperature(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_TEMP)));
            item.setMode(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_MODE)));
            item.setFan(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_FAN)));
            item.setIs_on(dbData.getInt(dbData.getColumnIndex(AvailableAA.COLUMN_NAME_IS_ON)) == 1);
            data.add(item);
        }

        dbData.close();

        return data;
    }

    public static void saveListData (List<AAItem> data, Context context){
        AirConditionersDBHelper dbHelper = new AirConditionersDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AvailableAA.TABLE_NAME, null, null);
        for(int i = 0; i < data.size(); i++){
            ContentValues values = new ContentValues();

            values.put(AvailableAA.COLUMN_NAME_NAME, data.get(i).getName());
            values.put(AvailableAA.COLUMN_NAME_BRAND, data.get(i).getBrand());
            values.put(AvailableAA.COLUMN_NAME_SERVER, data.get(i).getServer());
            values.put(AvailableAA.COLUMN_NAME_PORT, data.get(i).getPort());
            values.put(AvailableAA.COLUMN_NAME_USERNAME, data.get(i).getUsername());
            values.put(AvailableAA.COLUMN_NAME_PASSWORD, data.get(i).getPassword());
            values.put(AvailableAA.COLUMN_NAME_CERTIFICATE, data.get(i).getCertificate());
            values.put(AvailableAA.COLUMN_NAME_ALIAS, data.get(i).getAlias());
            values.put(AvailableAA.COLUMN_NAME_TEMP, data.get(i).getTemperature());
            values.put(AvailableAA.COLUMN_NAME_MODE, data.get(i).getMode());
            values.put(AvailableAA.COLUMN_NAME_FAN, data.get(i).getFan());
            values.put(AvailableAA.COLUMN_NAME_IS_ON, (data.get(i).getIs_on()) ? 1 : 0);

            db.insert(AvailableAA.TABLE_NAME, null, values);
        }
    }


    private static Cursor getDatabaseData(AirConditionersDBHelper dbHelper){
        String[] projection = {
                AvailableAA.COLUMN_NAME_NAME,
                AvailableAA.COLUMN_NAME_BRAND,
                AvailableAA.COLUMN_NAME_SERVER,
                AvailableAA.COLUMN_NAME_PORT,
                AvailableAA.COLUMN_NAME_USERNAME,
                AvailableAA.COLUMN_NAME_PASSWORD,
                AvailableAA.COLUMN_NAME_CERTIFICATE,
                AvailableAA.COLUMN_NAME_ALIAS,
                AvailableAA.COLUMN_NAME_TEMP,
                AvailableAA.COLUMN_NAME_MODE,
                AvailableAA.COLUMN_NAME_FAN,
                AvailableAA.COLUMN_NAME_IS_ON
        };

        String orderBy = AvailableAA._ID;

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
}
