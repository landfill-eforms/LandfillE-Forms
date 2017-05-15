package com.landfilleforms.android.landfille_forms.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.landfilleforms.android.landfille_forms.model.InstantaneousData;
import com.landfilleforms.android.landfille_forms.database.LandFillDbSchema.InstantaneousDataTable;
import com.landfilleforms.android.landfille_forms.model.Instrument;

import java.util.Date;
import java.util.UUID;

/**
 * InstantaneousDataCursorWrapper.java
 * Purpose: Cursor wrapper class for the instantaneous_data table query result set.
 * It pulls the data from the current row that the cursor is at & uses the data
 * to construct a InstantaneousData Java object.
 */

public class InstantaneousDataCursorWrapper extends CursorWrapper {
    public InstantaneousDataCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /*
    * Returns a InstantaneousData object built using the data taken from each of the columns
    * from the current row.
    * */
    public InstantaneousData getInstantaneousData() {
        String uuidString = getString(getColumnIndex(InstantaneousDataTable.Cols.UUID));
        String location = getString(getColumnIndex(InstantaneousDataTable.Cols.LOCATION));
        double barometricPressure = getDouble(getColumnIndex(InstantaneousDataTable.Cols.BARO_PRESSURE));
        String gridId = getString(getColumnIndex(InstantaneousDataTable.Cols.GRID_ID));
        String inspectorName = getString(getColumnIndex(InstantaneousDataTable.Cols.INSPECTOR_NAME));
        String inspectorUserName = getString(getColumnIndex(InstantaneousDataTable.Cols.INSPECTOR_USERNAME));
        long startDate = getLong(getColumnIndex(InstantaneousDataTable.Cols.START_DATE));
        long endDate = getLong(getColumnIndex(InstantaneousDataTable.Cols.END_DATE));
        double methaneReading = getDouble(getColumnIndex(InstantaneousDataTable.Cols.MAX_METHANE_READING));
        String ime_number = getString(getColumnIndex(InstantaneousDataTable.Cols.IME_NUMBER));
        Instrument instrument = new Instrument(getInt(getColumnIndex(InstantaneousDataTable.Cols.INSTRUMENT_ID)));

        InstantaneousData instantaneousData = new InstantaneousData(UUID.fromString(uuidString));
        instantaneousData.setLandFillLocation(location);
        instantaneousData.setBarometricPressure(barometricPressure);
        instantaneousData.setGridId(gridId);
        instantaneousData.setInspectorName(inspectorName);
        instantaneousData.setInspectorUserName(inspectorUserName);
        instantaneousData.setStartDate(new Date(startDate));
        instantaneousData.setEndDate(new Date(endDate));
        instantaneousData.setInstrument(instrument);
        instantaneousData.setMethaneReading(methaneReading);
        instantaneousData.setImeNumber(ime_number);

        return instantaneousData;
    }
}
