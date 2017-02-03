package com.landfilleforms.android.landfille_forms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL("create table " + UsersTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                UsersTable.Cols.UUID + ", " +
                UsersTable.Cols.USERNAME + ", " +
                UsersTable.Cols.PASSWORD + ", " +
                UsersTable.Cols.FULLNAME + ")"
        );
        db.execSQL("create table " + InstantaneousDataTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                InstantaneousDataTable.Cols.UUID + ", " +
                InstantaneousDataTable.Cols.LOCATION + ", " +
                InstantaneousDataTable.Cols.INSPECTOR_NAME + ", " +
                InstantaneousDataTable.Cols.METHANE_READING + ", " +
                InstantaneousDataTable.Cols.START_DATE + ", " +
                InstantaneousDataTable.Cols.END_DATE + ", " +
                InstantaneousDataTable.Cols.GRID_ID + ", " +
                InstantaneousDataTable.Cols.IME_NUMBER + ")"
        );
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
