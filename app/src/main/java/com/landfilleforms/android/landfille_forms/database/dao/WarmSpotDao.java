package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.cursorwrapper.WarmSpotDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.WarmSpotDataTable;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * WarmSpotDao.java
 * Purpose: Data access object class for WarmSpotData. Instead of using raw SQL queries, we use this
 * class to access the DB to do basic CRUD operations on the warmspot_data table.
 */
public class WarmSpotDao {
    public static WarmSpotDao sWarmSpotDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static WarmSpotDao get(Context context) {
        if (sWarmSpotDao == null) {
            sWarmSpotDao = new WarmSpotDao(context);
        }
        return sWarmSpotDao;
    }

    private WarmSpotDao(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Adds a WarmSpotData to the DB.
     * @param d The WarmSpotData to be added.
     */
    public void addWarmSpotData(WarmSpotData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(WarmSpotDataTable.NAME, null, values);
    }

    /**
     * Removes a WarmSpotData from the DB.
     * @param d The WarmSpotData to be deleted.
     */
    public void removeWarmSpotData(WarmSpotData d) {
        mDatabase.delete(WarmSpotDataTable.NAME, WarmSpotDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    /**
     * Retrieves a list of all WarmSpotData in the DB.
     */
    public List<WarmSpotData> getWarmSpotDatas() {
        List<WarmSpotData> warmSpotForm = new ArrayList<>();

        WarmSpotDataCursorWrapper cursor = queryWarmSpotData(null, null);//Having both values null effectively selects all

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                WarmSpotData w = cursor.getWarmSpotData();
                if(w.getInstrument() != null) {
                    w.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(w.getInstrument().getId())));
                }
                warmSpotForm.add(w);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return warmSpotForm;
    }

    /**
     * Retrieves a list of WarmSpotData from the DB based on the location.
     * @param location A String array containing the location
     */
    public List<WarmSpotData> getWarmSpotDatasByLocation(String[] location) {
        List<WarmSpotData> warmSpotDatas = new ArrayList<>();

        WarmSpotDataCursorWrapper cursor = queryWarmSpotData(WarmSpotDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                WarmSpotData w = cursor.getWarmSpotData();
                if(w.getInstrument() != null) {
                    w.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(w.getInstrument().getId())));
                }
                warmSpotDatas.add(w);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return warmSpotDatas;
    }

    /**
     * Retrieves a WarmSpotData object from the warmspot_data table.
     * @param id The UUID of the WarmSpotData object to be retrieved.
     */
    public WarmSpotData getWarmSpotData(UUID id) {
        WarmSpotDataCursorWrapper cursor = queryWarmSpotData(
                WarmSpotDataTable.Cols.UUID + "= ? ",
                new String[] {id.toString()}
        );
        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            WarmSpotData w = cursor.getWarmSpotData();
            if(w.getInstrument() != null) {
                w.setInstrument(InstrumentDao.get(mContext).getInstrument(Integer.toString(w.getInstrument().getId())));
            }
            return w;
        } finally {
            cursor.close();
        }
    }

    /**
     * Updates an entry from the warmspot_data table.
     * @param warmSpotData The warmspot data to be updated.
     */
    public void updateWarmSpotData(WarmSpotData warmSpotData) {
        String uuidString = warmSpotData.getId().toString();
        ContentValues values = getContentValues(warmSpotData);

        mDatabase.update(WarmSpotDataTable.NAME, values,
                WarmSpotDataTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    /**
     * Takes the content of an WarmSpotData so we can use them to update/add entries from/to the warmspot_data
     * table in our database.
     * @param warmSpotData The WarmSpotData object that content values are from.
     */
    private static ContentValues getContentValues(WarmSpotData warmSpotData) {
        ContentValues values = new ContentValues();
        values.put(WarmSpotDataTable.Cols.UUID, warmSpotData.getId().toString());
        values.put(WarmSpotDataTable.Cols.LOCATION, warmSpotData.getLocation());
        values.put(WarmSpotDataTable.Cols.GRIDS, warmSpotData.getGrids());
        values.put(WarmSpotDataTable.Cols.DATE, warmSpotData.getDate().getTime());
        values.put(WarmSpotDataTable.Cols.DESCRIPTION, warmSpotData.getDescription());
        values.put(WarmSpotDataTable.Cols.ESTIMATED_SIZE, warmSpotData.getEstimatedSize());
        values.put(WarmSpotDataTable.Cols.INSPECTOR_NAME, warmSpotData.getInspectorFullName());
        values.put(WarmSpotDataTable.Cols.INSPECTOR_USERNAME, warmSpotData.getInspectorUserName());
        if(warmSpotData.getInstrument() != null){
            values.put(WarmSpotDataTable.Cols.INSTRUMENT_ID, warmSpotData.getInstrument().getId());
        }
        values.put(WarmSpotDataTable.Cols.MAX_METHANE_READING, warmSpotData.getMaxMethaneReading());

        return values;
    }

    /**
     * Returns a cursor wrapper for the warmspot_data query result set.
     * @param whereClause The where clause for the query.
     * @param whereArgs The where arguments for the query.
     */
    private WarmSpotDataCursorWrapper queryWarmSpotData(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                WarmSpotDataTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new WarmSpotDataCursorWrapper(cursor);
    }
}
