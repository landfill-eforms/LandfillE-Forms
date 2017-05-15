package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstrumentTypesTable;
import com.landfilleforms.android.landfille_forms.model.InstrumentType;

/**
 * InstrumentTypeCursorWrapper.java
 * Purpose: Cursor wrapper class for the instrument_types table query result set.
 * It pulls the data from the current row that the cursor is at & uses the data
 * to construct a InstrumentType Java object.
 */

public class InstrumentTypeCursorWrapper extends CursorWrapper {
    public InstrumentTypeCursorWrapper(Cursor cursor) { super(cursor); }

    /*
    * Returns a InstrumentType object built using the data taken from each of the columns
    * from the current row.
    * */
    public InstrumentType getInstrumentType() {
        int id = getInt(getColumnIndex(InstrumentTypesTable.Cols.ID));
        String type = getString(getColumnIndex(InstrumentTypesTable.Cols.TYPE));
        String manufacturer = getString(getColumnIndex(InstrumentTypesTable.Cols.MANUFACTURER));
        String description = getString(getColumnIndex(InstrumentTypesTable.Cols.DESCRIPTION));
        boolean instantaneous = getInt(getColumnIndex(InstrumentTypesTable.Cols.INSTANTANEOUS)) != 0;
        boolean probe = getInt(getColumnIndex(InstrumentTypesTable.Cols.PROBE)) != 0;
        boolean methanePercent = getInt(getColumnIndex(InstrumentTypesTable.Cols.METHANE_PERCENT)) != 0;
        boolean methanePpm = getInt(getColumnIndex(InstrumentTypesTable.Cols.METHANE_PPM)) != 0;
        boolean hydrogenSulfidePpm = getInt(getColumnIndex(InstrumentTypesTable.Cols.HYDROGEN_SULFIDE_PPM)) != 0;
        boolean oxygenPercent = getInt(getColumnIndex(InstrumentTypesTable.Cols.OXYGEN_PERCENT)) != 0;
        boolean carbonDioxidePercent = getInt(getColumnIndex(InstrumentTypesTable.Cols.CARBON_DIOXIDE_PERCENT)) != 0;
        boolean nitrogenPercent = getInt(getColumnIndex(InstrumentTypesTable.Cols.NITROGEN_PERCENT)) != 0;
        boolean pressure = getInt(getColumnIndex(InstrumentTypesTable.Cols.PRESSURE)) != 0;

        InstrumentType instrumentType = new InstrumentType(id,type,manufacturer,description,instantaneous,probe,methanePercent,methanePpm,hydrogenSulfidePpm,oxygenPercent,carbonDioxidePercent,nitrogenPercent,pressure);
        return instrumentType;
    }

}
