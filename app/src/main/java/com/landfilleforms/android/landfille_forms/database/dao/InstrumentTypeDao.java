package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstrumentTypesTable;
import com.landfilleforms.android.landfille_forms.database.cursorwrapper.InstrumentTypeCursorWrapper;
import com.landfilleforms.android.landfille_forms.model.InstrumentType;

import java.util.List;

public class InstrumentTypeDao {
    public static InstrumentTypeDao sInstrumentTypeDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static InstrumentTypeDao get(Context context) {
        if(sInstrumentTypeDao == null) {
            sInstrumentTypeDao = new InstrumentTypeDao(context);
        }
        return sInstrumentTypeDao;
    }

    private InstrumentTypeDao(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    public void addInstrumentTypes(List<InstrumentType> instrumentTypes) {
        for(InstrumentType it:instrumentTypes) {
            ContentValues values = getContentValues(it);
            mDatabase.insert(InstrumentTypesTable.NAME, null, values);
        }
    }

    public InstrumentType getInstrumentType(int id) {
        InstrumentTypeCursorWrapper cursor = queryInstrumentTypes(
                InstrumentTypesTable.Cols.ID + " = ? ",
                new String[] { Integer.toString(id) }
        );

        try {
            if(cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getInstrumentType();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(InstrumentType instrumentType) {
        ContentValues values = new ContentValues();
        values.put(InstrumentTypesTable.Cols.ID,instrumentType.getId());
        values.put(InstrumentTypesTable.Cols.TYPE,instrumentType.getId());
        values.put(InstrumentTypesTable.Cols.MANUFACTURER,instrumentType.getManufacturer());
        values.put(InstrumentTypesTable.Cols.DESCRIPTION,instrumentType.getDescription());
        values.put(InstrumentTypesTable.Cols.INSTANTANEOUS,instrumentType.isInstantaneous());
        values.put(InstrumentTypesTable.Cols.PROBE,instrumentType.isProbe());
        values.put(InstrumentTypesTable.Cols.METHANE_PERCENT,instrumentType.isMethanePercent());
        values.put(InstrumentTypesTable.Cols.METHANE_PPM,instrumentType.isMethanePpm());
        values.put(InstrumentTypesTable.Cols.HYDROGEN_SULFIDE_PPM,instrumentType.isHydrogenSulfidePpm());
        values.put(InstrumentTypesTable.Cols.OXYGEN_PERCENT,instrumentType.isOxygenPercent());
        values.put(InstrumentTypesTable.Cols.CARBON_DIOXIDE_PERCENT,instrumentType.isCarbonDioxidePercent());
        values.put(InstrumentTypesTable.Cols.NITROGEN_PERCENT,instrumentType.isNitrogenPercent());
        values.put(InstrumentTypesTable.Cols.PRESSURE,instrumentType.isPressure());

        return values;
    }

    private InstrumentTypeCursorWrapper queryInstrumentTypes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                InstrumentTypesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new InstrumentTypeCursorWrapper(cursor);
    }
}
