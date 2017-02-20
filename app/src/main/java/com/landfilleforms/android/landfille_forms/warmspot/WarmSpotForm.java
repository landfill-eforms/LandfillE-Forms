package com.landfilleforms.android.landfille_forms.warmspot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.WarmSpotDataCursorWrapper;
import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.WarmSpotDataTable;
import com.landfilleforms.android.landfille_forms.model.WarmSpotData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * Created by Work on 2/17/2017.
 */

//Done

public class WarmSpotForm {
    public static WarmSpotForm sWarmSpotForm;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static WarmSpotForm get(Context context) {
        if (sWarmSpotForm == null) {
            sWarmSpotForm = new WarmSpotForm(context);
        }
        return sWarmSpotForm;
    }

    private WarmSpotForm(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    public void addWarmSpotData(WarmSpotData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(WarmSpotDataTable.NAME, null, values);
    }

    public void removeWarmSpotData(WarmSpotData d) {
        mDatabase.delete(WarmSpotDataTable.NAME, WarmSpotDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    //Data is already plural but I made it Datas to show that we're getting all the data from the table
    public List<WarmSpotData> getWarmSpotDatas() {
        List<WarmSpotData> warmSpotForm = new ArrayList<>();

        WarmSpotDataCursorWrapper cursor = queryWarmSpotData(null, null);//Having both values null effectively selects all

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                warmSpotForm.add(cursor.getWarmSpotData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return warmSpotForm;
    }

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
            return cursor.getWarmSpotData();
        } finally {
            cursor.close();
        }
    }

    public void updateWarmSpotData(WarmSpotData warmSpotData) {
        String uuidString = warmSpotData.getId().toString();
        ContentValues values = getContentValues(warmSpotData);

        mDatabase.update(WarmSpotDataTable.NAME, values,
                WarmSpotDataTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    private static ContentValues getContentValues(WarmSpotData warmSpotData) {
        ContentValues values = new ContentValues();
        values.put(WarmSpotDataTable.Cols.UUID, warmSpotData.getId().toString());
        values.put(WarmSpotDataTable.Cols.LOCATION, warmSpotData.getLocation());
        values.put(WarmSpotDataTable.Cols.GRID_ID, warmSpotData.getGridId());
        values.put(WarmSpotDataTable.Cols.DATE, warmSpotData.getDate().getTime());
        values.put(WarmSpotDataTable.Cols.DESCRIPTION, warmSpotData.getDescription());
        values.put(WarmSpotDataTable.Cols.ESTIMATED_SIZE, warmSpotData.getEstimatedSize());
        values.put(WarmSpotDataTable.Cols.INSPECTOR_NAME, warmSpotData.getInspectorFullName());
        values.put(WarmSpotDataTable.Cols.INSPECTOR_USERNAME, warmSpotData.getInspectorUserName());
        values.put(WarmSpotDataTable.Cols.INSTRUMENT_SERIAL, warmSpotData.getInstrumentSerial());
        values.put(WarmSpotDataTable.Cols.MAX_METHANE_READING, warmSpotData.getMaxMethaneReading());

        return values;
    }

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
