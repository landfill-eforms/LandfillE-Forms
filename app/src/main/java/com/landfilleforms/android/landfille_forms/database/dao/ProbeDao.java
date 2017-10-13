package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.ProbeDataTable;
import com.landfilleforms.android.landfille_forms.database.cursorwrapper.ProbeDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.model.ProbeData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ProbeDao.java
 * Purpose: Data access object class for ProbeData. Instead of using raw SQL queries, we use this
 * class to access the DB to do basic CRUD operations on the probe_data table.
 */
public class ProbeDao {
    public static ProbeDao sProbeDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ProbeDao get(Context context) {
        if (sProbeDao == null) {
            sProbeDao = new ProbeDao(context);
        }
        return sProbeDao;
    }

    public ProbeDao(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Adds a ProbeData to the DB.
     * @param d The ProbeData to be added.
     */
    public void addProbeData(ProbeData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(ProbeDataTable.NAME, null, values);
    }

    /**
     * Removes a ProbeData from the DB.
     * @param d The ProbeData to be deleted.
     */
    public void removeProbeData(ProbeData d) {
        mDatabase.delete(ProbeDataTable.NAME, ProbeDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    /**
     * Retrieves a list of all ProbeData in the DB.
     */
    public List<ProbeData> getProbeDatas() {
        List<ProbeData> probeDatas = new ArrayList<>();

        ProbeDataCursorWrapper cursor = queryProbeData(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                probeDatas.add(cursor.getProbeData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return probeDatas;
    }

    /**
     * Retrieves a list of ProbeData from the DB based on the location.
     * @param location A String array containing the location
     */
    public List<ProbeData> getProbeDatasByLocation(String[] location) {
        List<ProbeData> probeDatas = new ArrayList<>();

        ProbeDataCursorWrapper cursor = queryProbeData(ProbeDataTable.Cols.LOCATION + "= ?", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                probeDatas.add(cursor.getProbeData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return probeDatas;
    }

    /**
     * Retrieves a ProbeData object from the probe_data table.
     * @param id The UUID of the ProbeData object to be retrieved.
     */
    public ProbeData getProbeData(UUID id) {
        ProbeDataCursorWrapper cursor = queryProbeData(
                ProbeDataTable.Cols.UUID + "= ? ",
                new String[] {id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProbeData();
        } finally {
            cursor.close();
        }
    }

    /**
     * Updates an entry from the probe_data table.
     * @param probeData The probe data to be updated.
     */
    public void updateProbeData(ProbeData probeData) {
        String uuidString = probeData.getId().toString();
        ContentValues values = getContentValues(probeData);

        mDatabase.update(ProbeDataTable.NAME, values,
                ProbeDataTable.Cols.UUID +"= ?",
                new String[] {uuidString});
    }

    /**
     * Updates a list of entries from the probe_data table.
     * @param probeDatas List of ProbeData to be updated.
     */
    public void updateProbeDatas(List<ProbeData> probeDatas) {
        for(int i = 0; i < probeDatas.size(); i++) {
            String uuidString = probeDatas.get(i).getId().toString();
            ContentValues values = getContentValues(probeDatas.get(i));

            mDatabase.update(ProbeDataTable.NAME, values,
                    ProbeDataTable.Cols.UUID + "= ?",
                    new String[] {uuidString});
        }
    }

    /**
     * Takes the content of an ProbeData so we can use them to update/add entries from/to the probe_data
     * table in our database.
     * @param probeData The ProbeData object that content values are from.
     */
    private static ContentValues getContentValues(ProbeData probeData) {
        ContentValues values = new ContentValues();
        values.put(ProbeDataTable.Cols.UUID, probeData.getId().toString());
        values.put(ProbeDataTable.Cols.LOCATION, probeData.getLocation());
        values.put(ProbeDataTable.Cols.INSTRUMENT, probeData.getInstrument());
        values.put(ProbeDataTable.Cols.DATE, probeData.getDate().getTime());
        values.put(ProbeDataTable.Cols.INSPECTOR_NAME, probeData.getInspectorName());
        values.put(ProbeDataTable.Cols.INSPECTOR_USERNAME, probeData.getInspectorUserName());
        values.put(ProbeDataTable.Cols.BARO_PRESSURE, probeData.getBarometricPressure());
        values.put(ProbeDataTable.Cols.PROBE_NUMBER, probeData.getProbeNumber());
        values.put(ProbeDataTable.Cols.WATER_PRESSURE, probeData.getWaterPressure());
        values.put(ProbeDataTable.Cols.METHANE_PERCENTAGE, probeData.getMethanePercentage());
        values.put(ProbeDataTable.Cols.REMARKS, probeData.getRemarks());

        return values;
    }

    /**
     * Returns a cursor wrapper for the probe_data query result set.
     * @param whereClause The where clause for the query.
     * @param whereArgs The where arguments for the query.
     */
    private ProbeDataCursorWrapper queryProbeData(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ProbeDataTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ProbeDataCursorWrapper(cursor);
    }
}
