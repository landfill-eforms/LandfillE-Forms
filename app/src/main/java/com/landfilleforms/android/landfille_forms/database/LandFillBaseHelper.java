package com.landfilleforms.android.landfille_forms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstantaneousDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.UsersTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstrumentsTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstrumentTypesTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ImeDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.WarmSpotDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IntegratedDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IseDataTable;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ProbeDataTable;

/**
 * LandFillBaseHelper.java
 * Purpose: Creates all the tables in our DB.
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
                UsersTable.Cols.ID + " integer primary key, " +
                UsersTable.Cols.USERNAME + ", " +
                UsersTable.Cols.PASSWORD + ", " +
                UsersTable.Cols.FIRST_NAME + ", " +
                UsersTable.Cols.MIDDLE_NAME + ", " +
                UsersTable.Cols.LAST_NAME + ", " +
                UsersTable.Cols.EMAIL_ADDRESS + ", " +
                UsersTable.Cols.EMPLOYEE_ID + ", " +
                UsersTable.Cols.ENABLED + ")"
        );

        db.execSQL("create table " + InstrumentTypesTable.NAME + " (" +
                InstrumentTypesTable.Cols.ID + " integer primary key, " +
                InstrumentTypesTable.Cols.TYPE + ", " +
                InstrumentTypesTable.Cols.MANUFACTURER + ", " +
                InstrumentTypesTable.Cols.DESCRIPTION + ", " +
                InstrumentTypesTable.Cols.INSTANTANEOUS + ", " +
                InstrumentTypesTable.Cols.PROBE + ", " +
                InstrumentTypesTable.Cols.METHANE_PERCENT + ", " +
                InstrumentTypesTable.Cols.METHANE_PPM + ", " +
                InstrumentTypesTable.Cols.HYDROGEN_SULFIDE_PPM + ", " +
                InstrumentTypesTable.Cols.OXYGEN_PERCENT + ", " +
                InstrumentTypesTable.Cols.CARBON_DIOXIDE_PERCENT + ", " +
                InstrumentTypesTable.Cols.NITROGEN_PERCENT + ", " +
                InstrumentTypesTable.Cols.PRESSURE + ")"
        );

        db.execSQL("create table " + InstrumentsTable.NAME + " (" +
                InstrumentsTable.Cols.ID + " integer primary key, " +
                InstrumentsTable.Cols.SERIAL_NUMBER + ", " +
                InstrumentsTable.Cols.INSTRUMENT_STATUS + ", " +
                InstrumentsTable.Cols.SITE + ", " +
                InstrumentsTable.Cols.DESCRIPTION + ", " +
                InstrumentsTable.Cols.INSTRUMENT_TYPE_ID + ", " +
                "FOREIGN KEY(" +InstrumentsTable.Cols.INSTRUMENT_TYPE_ID + ") REFERENCES "+ InstrumentTypesTable.NAME + "("+ InstrumentTypesTable.Cols.ID +")" + ")"
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
                InstantaneousDataTable.Cols.INSTRUMENT_ID + ", " +
                InstantaneousDataTable.Cols.MAX_METHANE_READING + ", " +
                InstantaneousDataTable.Cols.IME_NUMBER + ", " +
                "FOREIGN KEY(" +InstantaneousDataTable.Cols.INSTRUMENT_ID + ") REFERENCES "+ InstrumentsTable.NAME + "("+ InstrumentsTable.Cols.ID +")" + ")"
        );

        db.execSQL("create table " + ImeDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ImeDataTable.Cols.UUID + "," +
                ImeDataTable.Cols.IME_NUMBER + "," +
                ImeDataTable.Cols.LOCATION + "," +
                ImeDataTable.Cols.GRID_ID + "," +
                ImeDataTable.Cols.INSTRUMENT + "," +
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
                WarmSpotDataTable.Cols.INSTRUMENT_ID + "," +
                "FOREIGN KEY(" +WarmSpotDataTable.Cols.INSTRUMENT_ID + ") REFERENCES "+ InstrumentsTable.NAME + "("+ InstrumentsTable.Cols.ID +")" + ")"
        );

        db.execSQL("create table " + IntegratedDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                IntegratedDataTable.Cols.UUID + "," +
                IntegratedDataTable.Cols.LOCATION + "," +
                IntegratedDataTable.Cols.GRID_ID + "," +
                IntegratedDataTable.Cols.INSTRUMENT_ID + "," +
                IntegratedDataTable.Cols.BARO_PRESSURE + "," +
                IntegratedDataTable.Cols.INSPECTOR_NAME + "," +
                IntegratedDataTable.Cols.INSPECTOR_USERNAME + "," +
                IntegratedDataTable.Cols.SAMPLE_ID + "," +
                IntegratedDataTable.Cols.BAG_NUMBER + "," +
                IntegratedDataTable.Cols.START_DATE + "," +
                IntegratedDataTable.Cols.END_DATE + "," +
                IntegratedDataTable.Cols.VOLUME_READING + "," +
                IntegratedDataTable.Cols.MAX_METHANE_READING + "," +
                "FOREIGN KEY(" +IntegratedDataTable.Cols.INSTRUMENT_ID + ") REFERENCES "+ InstrumentsTable.NAME + "("+ InstrumentsTable.Cols.ID +")" + ")"
        );

        db.execSQL("create table " + IseDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                IseDataTable.Cols.UUID + "," +
                IseDataTable.Cols.ISE_NUMBER + "," +
                IseDataTable.Cols.LOCATION + "," +
                IseDataTable.Cols.GRID_ID + "," +
                IseDataTable.Cols.DATE + "," +
                IseDataTable.Cols.DESCRIPTION + "," +
                IseDataTable.Cols.INSPECTOR_NAME + "," +
                IseDataTable.Cols.INSPECTOR_USERNAME + "," +
                IseDataTable.Cols.MAX_METHANE_READING + ")"
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


    //The recommended way to update the DB when we make changes to the schema.
    //We actually never used this method. Every time we made changes to the schema, we just
    //cleared all the data/cache from the application setting.
    /**
     * The method to be called when we make changes to our DB
     * @param db Our DB.
     * @param oldVersion The old version number of our DB.
     * @param newVersion The old version number of our DB.
     */
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        //Code to be executed.
    }

}
