package com.landfilleforms.android.landfille_forms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstantaneousDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.UsersTable;

/**
 * Created by Work on 10/30/2016.
 */

public class LandFillBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "landfillBase.db";

    public LandFillBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        Log.d("Create:", "User Table");
        db.execSQL("create table " + UsersTable.NAME + " (" +
                UsersTable.Cols.ID + " integer primary key autoincrement, " +
                UsersTable.Cols.USERNAME + ", " +
                UsersTable.Cols.PASSWORD + ", " +
                UsersTable.Cols.FULLNAME + ")"
        );
<<<<<<< HEAD
        Log.d("Create:", "Instantaneous Table");
=======

>>>>>>> 4d8387fd21859511a75141235a15aaed4af59665
        db.execSQL("create table " + InstantaneousDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                InstantaneousDataTable.Cols.UUID + ", " +
                InstantaneousDataTable.Cols.LOCATION + ", " +
                InstantaneousDataTable.Cols.BARO_PRESSURE + ", " +
                InstantaneousDataTable.Cols.GRID_ID + ", " +
                InstantaneousDataTable.Cols.INSPECTOR_NAME + ", " +
                InstantaneousDataTable.Cols.INSPECTOR_USERNAME + ", " +
                InstantaneousDataTable.Cols.START_DATE + ", " +
                InstantaneousDataTable.Cols.END_DATE + ", " +
                InstantaneousDataTable.Cols.INSTRUMENT_SERIAL + ", " +
                InstantaneousDataTable.Cols.MAX_METHANE_READING + ", " +
                InstantaneousDataTable.Cols.IME_NUMBER + ")"
        );
        Log.d("AfterCreate:", "Instantaneous Table");
    }

    //When we add/remove columns. Change version number.
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
