package com.landfilleforms.android.landfille_forms.database.util;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema;

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
        cv.put(LandFillDbSchema.UsersTable.Cols.ID, 0);
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "android_admin");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "$2a$04$wheOtEm29FE1MSn3iwWQ6ulXHWY4sg5QXUasrEJm1JhTaxBrxGZIm");
        cv.put(LandFillDbSchema.UsersTable.Cols.FIRST_NAME, "Android");
        cv.put(LandFillDbSchema.UsersTable.Cols.MIDDLE_NAME, "");
        cv.put(LandFillDbSchema.UsersTable.Cols.LAST_NAME, "Admin");
        cv.put(LandFillDbSchema.UsersTable.Cols.EMAIL_ADDRESS, "3s.grantkang@gmail.com");
        cv.put(LandFillDbSchema.UsersTable.Cols.EMPLOYEE_ID, "");
        cv.put(LandFillDbSchema.UsersTable.Cols.ENABLED, true);
        list.add(cv);




/*        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "jhamilton");
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
		
		cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "nbolas");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "Nicol Bolas");
        list.add(cv);
		
		cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "cnalaar");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "Chandra Nalaar");
        list.add(cv);
		
		cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "jbeleren");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "Jace Beleren");
        list.add(cv);
		
		cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "lvess");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "Liliana Vess");
        list.add(cv);
		
		cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "ccolbert");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "Camille Colbert");
        list.add(cv);
		
		cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "akord");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "Arlenn Kord");
        list.add(cv);

        cv = new ContentValues();
        cv.put(LandFillDbSchema.UsersTable.Cols.USERNAME, "gyu");
        cv.put(LandFillDbSchema.UsersTable.Cols.PASSWORD, "password");
        cv.put(LandFillDbSchema.UsersTable.Cols.FULLNAME, "George Yu");
        list.add(cv);*/
		
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
