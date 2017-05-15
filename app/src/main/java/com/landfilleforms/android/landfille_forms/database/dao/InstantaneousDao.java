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
 * InstantaneousDao.java
 * Purpose: Data access object class for InstantaneousData. Instead of using raw SQL queries, we use this
 * class to access the DB to do basic CRUD operations on the instantaneous_data table.
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

    /**
     * Adds a InstantaneousData to the DB.
     * @param d The InstantaneousData to be added.
     */
    public void addInstantaneousData(InstantaneousData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(InstantaneousDataTable.NAME, null, values);
    }

    /**
     * Removes a InstantaneousData from the DB.
     * @param d The InstantaneousData to be deleted.
     */
    public void removeInstantaneousData(InstantaneousData d) {
        mDatabase.delete(InstantaneousDataTable.NAME, InstantaneousDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    /**
     * Retrieves a list of all InstantaneousData in the DB.
     */
    public List<InstantaneousData> getInstantaneousDatas() {
        List<InstantaneousData> instantaneousForm = new ArrayList<>();
        InstantaneousDataCursorWrapper cursor = queryInstantaneousData(null, null);//Having both values null effectively selects all
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                InstantaneousData i = cursor.getInstantaneousData();
                if(i.getInstrument() != null) {
                    i.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(i.getInstrument().getId())));
                }
                instantaneousForm.add(i);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instantaneousForm;
    }

    /**
     * Retrieves a list of InstantaneousData from the DB based on the location.
     * @param location A String array containing the location
     */
    public List<InstantaneousData> getInstantaneousDatasByLocation(String[] location) {
        List<InstantaneousData> instantaneousForm = new ArrayList<>();
        InstantaneousDataCursorWrapper cursor = queryInstantaneousData(InstantaneousDataTable.Cols.LOCATION + "= ? ", location);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                InstantaneousData i = cursor.getInstantaneousData();
                if(i.getInstrument() != null) {
                    i.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(i.getInstrument().getId())));
                }
                instantaneousForm.add(i);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instantaneousForm;
    }

    /**
     * Retrieves a list of InstantaneousData based on the location & grid.
     * @param args String array containing the location and grid.
     */
    public List<InstantaneousData> getInstantaneousDatasByLocationGrid(String[] args) {
        List<InstantaneousData> instantaneousForm = new ArrayList<>();
        InstantaneousDataCursorWrapper cursor = queryInstantaneousData(InstantaneousDataTable.Cols.LOCATION + "= ? AND " + InstantaneousDataTable.Cols.GRID_ID + "= ?", args);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                InstantaneousData i = cursor.getInstantaneousData();
                if(i.getInstrument() != null) {
                    i.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(i.getInstrument().getId())));
                }
                instantaneousForm.add(i);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instantaneousForm;
    }

    /**
     * Retrieves a InstantaneousData object from the instantaneous_data table.
     * @param id The UUID of the InstantaneousData object to be retrieved.
     */
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
            InstantaneousData i = cursor.getInstantaneousData();
            if(i.getInstrument() != null) {
                i.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(i.getInstrument().getId())));
            }
            return i;
        } finally {
            cursor.close();
        }
    }

    /**
     * Updates an entry from the instantaneous_data table.
     * @param instantaneousData The instantaneous data to be updated.
     */
    public void updateInstantaneousData(InstantaneousData instantaneousData) {
        String uuidString = instantaneousData.getId().toString();
        ContentValues values = getContentValues(instantaneousData);

        mDatabase.update(InstantaneousDataTable.NAME, values,
                InstantaneousDataTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    /**
     * Updates a list of entries from the instantaneous_data table.
     * @param instantaneousDatas List of InstantaneousData to be updated.
     */
    public void updateInstantaneousDatas(List<InstantaneousData> instantaneousDatas) {
        for (int i = 0; i < instantaneousDatas.size(); i++) {
            String uuidString = instantaneousDatas.get(i).getId().toString();
            ContentValues values = getContentValues(instantaneousDatas.get(i));

            mDatabase.update(InstantaneousDataTable.NAME, values,
                    InstantaneousDataTable.Cols.UUID + "= ?",
                    new String[] {uuidString});
        }
    }

    /**
     * Takes the content of an InstantaneousData so we can use them to update/add entries from/to the instantaneous_data
     * table in our database.
     * @param instantaneousData The InstantaneousData object that content values are from.
     */
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
        if(instantaneousData.getInstrument() != null){
            values.put(InstantaneousDataTable.Cols.INSTRUMENT_ID, instantaneousData.getInstrument().getId());
        }
        values.put(InstantaneousDataTable.Cols.MAX_METHANE_READING, instantaneousData.getMethaneReading());
        values.put(InstantaneousDataTable.Cols.IME_NUMBER, instantaneousData.getImeNumber());

        return values;
    }

    /**
     * Returns a cursor wrapper for the instantaneous_data query result set.
     * @param whereClause The where clause for the query.
     * @param whereArgs The where arguments for the query.
     */
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
