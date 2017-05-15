package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.IntegratedDataTable;
import com.landfilleforms.android.landfille_forms.database.cursorwrapper.IntegratedDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.model.IntegratedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * IntegratedDao.java
 * Purpose: Data access object class for IntegratedData. Instead of using raw SQL queries, we use this
 * class to access the DB to do basic CRUD operations on the integrated_data table.
 */
public class IntegratedDao {
    public static IntegratedDao sIntegratedDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static IntegratedDao get(Context context) {
        if (sIntegratedDao == null) {
            sIntegratedDao = new IntegratedDao(context);
        }
        return sIntegratedDao;
    }

    private IntegratedDao (Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Adds a IntegratedData to the DB.
     * @param d The InstantaneousData to be added.
     */
    public void addIntegratedData(IntegratedData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(IntegratedDataTable.NAME, null, values);
    }

    /**
     * Removes a IntegratedData to the DB.
     * @param d The InstantaneousData to be removed.
     */
    public void removeIntegratedData(IntegratedData d) {
        mDatabase.delete(IntegratedDataTable.NAME, IntegratedDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    /**
     * Retrieves a list of all IntegratedData in the DB.
     */
    public List<IntegratedData> getIntegratedDatas() {
        List <IntegratedData> integratedDatas = new ArrayList<>();

        IntegratedDataCursorWrapper cursor = queryIntegratedData(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                IntegratedData i = cursor.getIntegratedData();
                if(i.getInstrument() != null) {
                    i.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(i.getInstrument().getId())));
                }
                integratedDatas.add(i);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return integratedDatas;
    }

    /**
     * Retrieves a list of IntegratedData from the DB based on the location.
     * @param location
     */
    public List<IntegratedData> getIntegratedDatasByLocation(String[] location) {
        List <IntegratedData> integratedDatas = new ArrayList<>();

        IntegratedDataCursorWrapper cursor = queryIntegratedData(IntegratedDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                IntegratedData i = cursor.getIntegratedData();
                if(i.getInstrument() != null) {
                    i.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(i.getInstrument().getId())));
                }
                integratedDatas.add(i);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return integratedDatas;
    }

    /**
     * Retrieves a IntegratedData object from the integrated_data table.
     * @param id The UUID of the IntegratedData object to be retrieved.
     */
    public IntegratedData getIntegratedData(UUID id) {
        IntegratedDataCursorWrapper cursor = queryIntegratedData(
                IntegratedDataTable.Cols.UUID + "= ? ",
                new String[] {id.toString()}
        );
        try {
            if (cursor.getCount() ==0){
                return null;
            }
            cursor.moveToFirst();
            IntegratedData i = cursor.getIntegratedData();
            if(i.getInstrument() != null) {
                i.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(i.getInstrument().getId())));
            }
            return i;
        } finally {
            cursor.close();
        }
    }

    /**
     * Updates a entry from the integrated_data table.
     * @param integratedData The integrated data to be updated.
     */
    public void updateIntegratedData(IntegratedData integratedData) {
        String uuidString = integratedData.getId().toString();
        ContentValues values = getContentValues(integratedData);

        mDatabase.update(IntegratedDataTable.NAME, values,
                IntegratedDataTable.Cols.UUID + "= ?",
                new String[] {uuidString}
        );
    }

    /**
     * Updates a list of entries from the integrated_data table.
     * @param integratedDatas List of IntegratedData to be updated.
     */
    public void updateIntegratedDatas(List<IntegratedData> integratedDatas) {
        for (int i = 0; i < integratedDatas.size(); i++) {
            String uuidString = integratedDatas.get(i).getId().toString();
            ContentValues values = getContentValues(integratedDatas.get(i));

            mDatabase.update(IntegratedDataTable.NAME, values,
                    IntegratedDataTable.Cols.UUID + "= ?",
                    new String[] {uuidString});
        }
    }

    /**
     * Takes the content of an InstantaneousData so we can use them to update/add entries from/to the integrated_data
     * table in our database.
     * @param integratedData The IntegratedData object that content values are from.
     */
    private static ContentValues getContentValues(IntegratedData integratedData) {
        ContentValues values = new ContentValues();
        values.put(IntegratedDataTable.Cols.UUID, integratedData.getId().toString());
        values.put(IntegratedDataTable.Cols.LOCATION, integratedData.getLocation());
        values.put(IntegratedDataTable.Cols.GRID_ID, integratedData.getGridId());
        if(integratedData.getInstrument() != null){
            values.put(IntegratedDataTable.Cols.INSTRUMENT_ID, integratedData.getInstrument().getId());
        }
        values.put(IntegratedDataTable.Cols.BARO_PRESSURE, integratedData.getBarometricPressure());
        values.put(IntegratedDataTable.Cols.INSPECTOR_NAME, integratedData.getInspectorName());
        values.put(IntegratedDataTable.Cols.INSPECTOR_USERNAME, integratedData.getInspectorUserName());
        values.put(IntegratedDataTable.Cols.SAMPLE_ID, integratedData.getSampleId());
        values.put(IntegratedDataTable.Cols.BAG_NUMBER, integratedData.getBagNumber());
        values.put(IntegratedDataTable.Cols.START_DATE, integratedData.getStartDate().getTime());
        values.put(IntegratedDataTable.Cols.END_DATE, integratedData.getEndDate().getTime());
        values.put(IntegratedDataTable.Cols.VOLUME_READING, integratedData.getVolumeReading());
        values.put(IntegratedDataTable.Cols.MAX_METHANE_READING, integratedData.getMethaneReading());

        return values;
    }

    /**
     * Returns a cursor wrapper for the integrated_data query result set.
     * @param whereClause The where clause for the query.
     * @param whereArgs The where arguments for the query.
     */
    private IntegratedDataCursorWrapper queryIntegratedData(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
            IntegratedDataTable.NAME,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            null
            );
        return new IntegratedDataCursorWrapper(cursor);
    }
}
