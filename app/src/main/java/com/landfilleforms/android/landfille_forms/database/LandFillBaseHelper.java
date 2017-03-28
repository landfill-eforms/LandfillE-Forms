package com.landfilleforms.android.landfille_forms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstantaneousDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.UsersTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ImeDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.WarmSpotDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IntegratedDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ProbeDataTable;

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

        db.execSQL("create table " + ImeDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ImeDataTable.Cols.UUID + "," +
                ImeDataTable.Cols.IME_NUMBER + "," +
                ImeDataTable.Cols.LOCATION + "," +
                ImeDataTable.Cols.GRID_ID + "," +
                ImeDataTable.Cols.DATE + "," +
                ImeDataTable.Cols.DESCRIPTION + "," +
                ImeDataTable.Cols.INSPECTOR_NAME + "," +
                ImeDataTable.Cols.INSPECTOR_USERNAME + "," +
                ImeDataTable.Cols.MAX_METHANE_READING + ")"
        );

        db.execSQL("create table " + WarmSpotDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                WarmSpotDataTable.Cols.UUID + "," +
                WarmSpotDataTable.Cols.LOCATION + "," +
                WarmSpotDataTable.Cols.GRID_ID + "," +
                WarmSpotDataTable.Cols.DATE + "," +
                WarmSpotDataTable.Cols.DESCRIPTION + "," +
                WarmSpotDataTable.Cols.ESTIMATED_SIZE + "," +
                WarmSpotDataTable.Cols.INSPECTOR_NAME + "," +
                WarmSpotDataTable.Cols.INSPECTOR_USERNAME + "," +
                WarmSpotDataTable.Cols.MAX_METHANE_READING + "," +
                WarmSpotDataTable.Cols.INSTRUMENT_SERIAL + ")"
        );

        db.execSQL("create table " + IntegratedDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                IntegratedDataTable.Cols.UUID + "," +
                IntegratedDataTable.Cols.LOCATION + "," +
                IntegratedDataTable.Cols.GRID_ID + "," +
                IntegratedDataTable.Cols.INSTRUMENT_SERIAL + "," +
                IntegratedDataTable.Cols.BARO_PRESSURE + "," +
                IntegratedDataTable.Cols.INSPECTOR_NAME + "," +
                IntegratedDataTable.Cols.INSPECTOR_USERNAME + "," +
                IntegratedDataTable.Cols.SAMPLE_ID + "," +
                IntegratedDataTable.Cols.BAG_NUMBER + "," +
                IntegratedDataTable.Cols.START_DATE + "," +
                IntegratedDataTable.Cols.END_DATE + "," +
                IntegratedDataTable.Cols.VOLUME_READING + "," +
                IntegratedDataTable.Cols.MAX_METHANE_READING + ")"
        );

        db.execSQL("create table " + ProbeDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ProbeDataTable.Cols.UUID + "," +
                ProbeDataTable.Cols.LOCATION + "," +
                ProbeDataTable.Cols.DATE + "," +
                ProbeDataTable.Cols.INSPECTOR_NAME + "," +
                ProbeDataTable.Cols.INSPECTOR_USERNAME + "," +
                ProbeDataTable.Cols.BARO_PRESSURE + "," +
                ProbeDataTable.Cols.PROBE_NUMBER + "," +
                ProbeDataTable.Cols.WATER_PRESSURE + "," +
                ProbeDataTable.Cols.METHANE_PERCENTAGE + "," +
                ProbeDataTable.Cols.REMARKS + ")"
        );
    }

    //When we add/remove columns. Change version number.
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
