package com.landfilleforms.android.landfille_forms.database;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Work on 2/9/2017.
 */

public class TestUtil {
    public static void insertDummyUserData(SQLiteDatabase db) {
        if (db == null){
            return;
        }

        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "jhamilton");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "John Hamilton");
        list.add(cv);

        cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "wandrews");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "William Andrews");
        list.add(cv);

        cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "aquach");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "Alvin Quach");
        list.add(cv);

        try {
            db.beginTransaction();
            db.delete(LandFillDbSchema.UsersTable.NAME,null,null);
            for(ContentValues c:list){
                db.insert(LandFillDbSchema.UsersTable.NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }
    public static void insertDummyInstantaneousData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.IME_NUMBER, UUID.randomUUID().toString());
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.LOCATION, "Lopez");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.BARO_PRESSURE, 30.00);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.GRID_ID, 1);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.INSPECTOR_NAME, "William Andrews");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.START_DATE, "date");//CHANGE THIS TO 11/8/2016
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.END_DATE, "date");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.INSTRUMENT_SERIAL, "2345");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.MAX_METHANE_READING, 1000);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.IME_NUMBER, "LC-110816-01");
        list.add(cv);

        cv = new ContentValues();
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.IME_NUMBER, UUID.randomUUID().toString());
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.LOCATION, "Lopez");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.BARO_PRESSURE, 30.00);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.GRID_ID, 2);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.INSPECTOR_NAME, "William Andrews");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.START_DATE, "date");//CHANGE THIS TO 11/8/2016
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.END_DATE, "date");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.INSTRUMENT_SERIAL, "2345");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.MAX_METHANE_READING, 8000);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.IME_NUMBER, "LC-110816-02");
        list.add(cv);

        cv = new ContentValues();
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.IME_NUMBER, UUID.randomUUID().toString());
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.LOCATION, "Lopez");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.BARO_PRESSURE, 30.00);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.GRID_ID, 3);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.INSPECTOR_NAME, "William Andrews");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.START_DATE, "date");//CHANGE THIS TO 11/8/2016
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.END_DATE, "date");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.INSTRUMENT_SERIAL, "2345");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.MAX_METHANE_READING, 4000);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.IME_NUMBER, "LC-110816-01");
        list.add(cv);

        cv = new ContentValues();
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.IME_NUMBER, UUID.randomUUID().toString());
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.LOCATION, "Lopez");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.BARO_PRESSURE, 30.00);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.GRID_ID, 4);
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.INSPECTOR_NAME, "William Andrews");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.START_DATE, "date");//CHANGE THIS TO 11/8/2016
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.END_DATE, "date");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.INSTRUMENT_SERIAL, "2345");
        cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.MAX_METHANE_READING, 150);
        //cv.put(LandFillDbSchema.InstantaneousDataTable.Cols.IME_NUMBER, "");
        list.add(cv);

        try {
            db.beginTransaction();
            db.delete(LandFillDbSchema.InstantaneousDataTable.NAME,null,null);
            for(ContentValues c:list){
                db.insert(LandFillDbSchema.InstantaneousDataTable.NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }


    }

}
