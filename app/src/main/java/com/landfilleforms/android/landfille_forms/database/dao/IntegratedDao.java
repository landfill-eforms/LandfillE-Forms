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
 * Created by Work on 3/27/2017.
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
        mDatabase = new LandFillBaseHelper(mContext).getReadableDatabase();
    }

    public void addIntegratedData(IntegratedData d) {
        ContentValues values = getContentValues(d);
        mDatabase.insert(IntegratedDataTable.NAME, null, values);
    }

    public void removeIntegratedData(IntegratedData d) {
        mDatabase.delete(IntegratedDataTable.NAME, IntegratedDataTable.Cols.UUID + "= ?", new String[] {d.getId().toString()});
    }

    public List<IntegratedData> getIntegratedDatas() {
        List <IntegratedData> integratedDatas = new ArrayList<>();

        IntegratedDataCursorWrapper cursor = queryIntegratedData(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                integratedDatas.add(cursor.getIntegratedData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return integratedDatas;
    }

    public List<IntegratedData> getIntegratedDatasByLocation(String[] location) {
        List <IntegratedData> integratedDatas = new ArrayList<>();

        IntegratedDataCursorWrapper cursor = queryIntegratedData(IntegratedDataTable.Cols.LOCATION + "= ? ", location);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                integratedDatas.add(cursor.getIntegratedData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return integratedDatas;
    }

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
            return cursor.getIntegratedData();
        } finally {
            cursor.close();
        }
    }

    public void updateIntegratedData(IntegratedData integratedData) {
        String uuidString = integratedData.getId().toString();
        ContentValues values = getContentValues(integratedData);

        mDatabase.update(IntegratedDataTable.NAME, values,
                IntegratedDataTable.Cols.UUID + "= ?",
                new String[] {uuidString}
        );
    }

    public void updateIntegratedDatas(List<IntegratedData> integratedDatas) {
        for (int i = 0; i < integratedDatas.size(); i++) {
            String uuidString = integratedDatas.get(i).getId().toString();
            ContentValues values = getContentValues(integratedDatas.get(i));

            mDatabase.update(IntegratedDataTable.NAME, values,
                    IntegratedDataTable.Cols.UUID + "= ?",
                    new String[] {uuidString});
        }
    }

    private static ContentValues getContentValues(IntegratedData integratedData) {
        ContentValues values = new ContentValues();
        values.put(IntegratedDataTable.Cols.UUID, integratedData.getId().toString());
        values.put(IntegratedDataTable.Cols.LOCATION, integratedData.getLocation());
        values.put(IntegratedDataTable.Cols.GRID_ID, integratedData.getGridId());
        values.put(IntegratedDataTable.Cols.INSTRUMENT_SERIAL, integratedData.getInstrumentSerialNumber());
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
