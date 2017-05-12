package com.landfilleforms.android.landfille_forms.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.landfilleforms.android.landfille_forms.database.LandFillBaseHelper;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstrumentsTable;
import com.landfilleforms.android.landfille_forms.database.cursorwrapper.InstrumentCursorWrapper;
import com.landfilleforms.android.landfille_forms.model.Instrument;

import java.util.ArrayList;
import java.util.List;

public class InstrumentDao {
    public static InstrumentDao sInstrumentDao;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static InstrumentDao get(Context context) {
        if (sInstrumentDao == null) {
            sInstrumentDao = new InstrumentDao(context);
        }
        return sInstrumentDao;
    }

    private InstrumentDao(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LandFillBaseHelper(mContext).getWritableDatabase();
    }

    public List<Instrument> getInstruments() {
        List<Instrument> instruments = new ArrayList<>();

        InstrumentCursorWrapper cursor = queryInstruments(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Instrument instrument = cursor.getInstrument();
                instrument.setInstrumentType(InstrumentTypeDao.get(mContext).getInstrumentType(instrument.getInstrumentType().getId()));
                instruments.add(instrument);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instruments;
    }

    public List<Instrument> getInstrumentsBySiteForSurface(String site) {
        List<Instrument> instruments = new ArrayList<>();

        String[] siteArgs = {site.toUpperCase(), ""};

        InstrumentCursorWrapper cursor = queryInstruments(InstrumentsTable.Cols.SITE + " = ? " + "OR " + InstrumentsTable.Cols.SITE + " = ? ", siteArgs);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Instrument instrument = cursor.getInstrument();
                instrument.setInstrumentType(InstrumentTypeDao.get(mContext).getInstrumentType(instrument.getInstrumentType().getId()));
                if(instrument.getInstrumentStatus().equals("ACTIVE") && instrument.getInstrumentType().isInstantaneous()) {
                    instruments.add(instrument);
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instruments;
    }

    public List<Instrument> getInstrumentsBySiteForProbe(String site) {
        List<Instrument> instruments = new ArrayList<>();

        String[] siteArgs = {site.toUpperCase(), ""};

        InstrumentCursorWrapper cursor = queryInstruments(InstrumentsTable.Cols.SITE + " = ? " + "OR " + InstrumentsTable.Cols.SITE + "= ? ", siteArgs);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Instrument instrument = cursor.getInstrument();
                instrument.setInstrumentType(InstrumentTypeDao.get(mContext).getInstrumentType(instrument.getInstrumentType().getId()));
                if(instrument.getInstrumentStatus().equals("ACTIVE") && instrument.getInstrumentType().isProbe()) {
                    instruments.add(instrument);
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return instruments;
    }

    public Instrument getInstrument(String id) {
        InstrumentCursorWrapper cursor = queryInstruments(
                InstrumentsTable.Cols.ID + " =? ",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            Instrument instrument = cursor.getInstrument();
            instrument.setInstrumentType(InstrumentTypeDao.get(mContext).getInstrumentType(instrument.getInstrumentType().getId()));
            return instrument;
        } finally {
            cursor.close();
        }
    }

    public void addInstruments(List<Instrument> instruments) {
        for(Instrument i:instruments) {
            ContentValues values = getContentValues(i);
            mDatabase.insert(InstrumentsTable.NAME,null,values);
        }
    }

    private static ContentValues getContentValues(Instrument instrument) {
        ContentValues values = new ContentValues();
        values.put(InstrumentsTable.Cols.ID,instrument.getId());
        values.put(InstrumentsTable.Cols.SERIAL_NUMBER,instrument.getSerialNumber());
        values.put(InstrumentsTable.Cols.INSTRUMENT_STATUS,instrument.getInstrumentStatus());
        values.put(InstrumentsTable.Cols.SITE,instrument.getSite());
        values.put(InstrumentsTable.Cols.DESCRIPTION,instrument.getDescription());
        values.put(InstrumentsTable.Cols.INSTRUMENT_TYPE_ID,instrument.getInstrumentType().getId());

        return values;
    }

    private InstrumentCursorWrapper queryInstruments(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                InstrumentsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new InstrumentCursorWrapper(cursor);
    }

}
