package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.cursorwrapper.InstantaneousDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstantaneousDataTable;
import com.landfilleforms.android.landfille_forms.model.InstantaneousData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Work on 10/30/2016.
 */

public class InstantaneousDao {
    public static InstantaneousDao sInstantaneousDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static InstantaneousDao get(Context context) {
        if (sInstantaneousDao == null) {
            sInstantaneousDao = new InstantaneousDao(context);
        }
        return sInstantaneousDao;
    }

    private InstantaneousDao(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    public void addInstantaneousData(InstantaneousData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(InstantaneousDataTable.NAME, null, values);
    }

    public void removeInstantaneousData(InstantaneousData d) {
        mDatabase.delete(InstantaneousDataTable.NAME, InstantaneousDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    //Data is already plural but I made it Datas to show that we're getting all the data from the table
    public List<InstantaneousData> getInstantaneousDatas() {
        List<InstantaneousData> instantaneousForm = new ArrayList<>();

        InstantaneousDataCursorWrapper cursor = queryInstantaneousData(null, null);//Having both values null effectively selects all

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                instantaneousForm.add(cursor.getInstantaneousData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instantaneousForm;
    }

    //TODO just testing, may need to change how this works
    public List<InstantaneousData> getInstantaneousDatasByLocation(String[] location) {
        List<InstantaneousData> instantaneousForm = new ArrayList<>();

        //Gotta create a WHERE clause
        InstantaneousDataCursorWrapper cursor = queryInstantaneousData(InstantaneousDataTable.Cols.LOCATION + "= ? ", location);//Having both values null effectively selects all

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                instantaneousForm.add(cursor.getInstantaneousData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instantaneousForm;
    }

    public List<InstantaneousData> getInstantaneousDatasByLocationGrid(String[] args) {
        List<InstantaneousData> instantaneousForm = new ArrayList<>();

        //Gotta create a WHERE clause
        InstantaneousDataCursorWrapper cursor = queryInstantaneousData(InstantaneousDataTable.Cols.LOCATION + "= ? AND " + InstantaneousDataTable.Cols.GRID_ID + "= ?", args);//Having both values null effectively selects all

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                instantaneousForm.add(cursor.getInstantaneousData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instantaneousForm;
    }


    public InstantaneousData getInstantaneousData(UUID id) {
        InstantaneousDataCursorWrapper cursor = queryInstantaneousData(
                InstantaneousDataTable.Cols.UUID + "= ? ",
                new String[] {id.toString()}
        );
        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getInstantaneousData();
        } finally {
            cursor.close();
        }
    }

    public void updateInstantaneousData(InstantaneousData instantaneousData) {
        String uuidString = instantaneousData.getId().toString();
        ContentValues values = getContentValues(instantaneousData);

        mDatabase.update(InstantaneousDataTable.NAME, values,
                InstantaneousDataTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    public void updateInstantaneousDatas(List<InstantaneousData> instantaneousDatas) {
        for (int i = 0; i < instantaneousDatas.size(); i++) {
            String uuidString = instantaneousDatas.get(i).getId().toString();
            ContentValues values = getContentValues(instantaneousDatas.get(i));

            mDatabase.update(InstantaneousDataTable.NAME, values,
                    InstantaneousDataTable.Cols.UUID + "= ?",
                    new String[] {uuidString});
        }
    }

    private static ContentValues getContentValues(InstantaneousData instantaneousData) {
        ContentValues values = new ContentValues();
        values.put(InstantaneousDataTable.Cols.UUID, instantaneousData.getId().toString());
        values.put(InstantaneousDataTable.Cols.LOCATION, instantaneousData.getLandFillLocation());
        values.put(InstantaneousDataTable.Cols.BARO_PRESSURE, instantaneousData.getBarometricPressure());
        values.put(InstantaneousDataTable.Cols.GRID_ID, instantaneousData.getGridId());
        values.put(InstantaneousDataTable.Cols.INSPECTOR_NAME, instantaneousData.getInspectorName());
        values.put(InstantaneousDataTable.Cols.INSPECTOR_USERNAME, instantaneousData.getInspectorUserName());
        values.put(InstantaneousDataTable.Cols.START_DATE, instantaneousData.getStartDate().getTime());
        values.put(InstantaneousDataTable.Cols.END_DATE, instantaneousData.getEndDate().getTime());
        values.put(InstantaneousDataTable.Cols.INSTRUMENT_SERIAL, instantaneousData.getInstrumentSerialNumber());
        values.put(InstantaneousDataTable.Cols.MAX_METHANE_READING, instantaneousData.getMethaneReading());
        values.put(InstantaneousDataTable.Cols.IME_NUMBER, instantaneousData.getImeNumber());

        return values;
    }

    private InstantaneousDataCursorWrapper queryInstantaneousData(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                InstantaneousDataTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new InstantaneousDataCursorWrapper(cursor);
    }
}
