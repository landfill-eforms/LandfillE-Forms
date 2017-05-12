package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstrumentTypesTable;
import com.landfilleforms.android.landfille_forms.model.InstrumentType;

/**
 * Created by Work on 5/10/2017.
 */

public class InstrumentTypeCursorWrapper extends CursorWrapper {
    public InstrumentTypeCursorWrapper(Cursor cursor) { super(cursor); }

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
